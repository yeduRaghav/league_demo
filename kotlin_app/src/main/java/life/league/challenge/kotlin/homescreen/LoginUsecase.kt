package life.league.challenge.kotlin.homescreen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.data.network.endpoint.LoginEndpoint
import life.league.challenge.kotlin.data.network.endpoint.base.ApiError
import life.league.challenge.kotlin.data.network.model.LoginApiResponse
import life.league.challenge.kotlin.session.SessionManager
import life.league.challenge.kotlin.util.Either
import javax.inject.Inject

/**
 * This Usecase takes care of logging-in
 */
class LoginUsecase @Inject constructor(
        private val sessionManager: SessionManager,
        private val loginEndpoint: LoginEndpoint
) {

    private var loginJob: Job? = null
    private lateinit var scope: CoroutineScope

    @VisibleForTesting
    val loginResult = MutableLiveData<LoginResult>()

    fun getLoginResult(): LiveData<LoginResult> = loginResult

    /**
     * Make sure scope is set before invoking login()
     * */
    fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }

    fun login(userName: String, password: String) {
        loginEndpoint.setCredentials(userName, password)
        attemptLogin()
    }

    private fun attemptLogin() {
        loginJob?.cancel()
        loginJob = scope.launch {
            when (val response = loginEndpoint.execute()) {
                is Either.Failure -> onApiCallFailure(response.error)
                is Either.Success -> onApiCallSuccess(response.value)
            }
        }
    }

    private fun onApiCallFailure(apiError: ApiError) {
        val failureReason = apiError.getLoginFailureReason()
        loginResult.postValue(Failure(failureReason))
    }

    @VisibleForTesting
    fun onApiCallSuccess(response: LoginApiResponse) {
        val result = response.apiKey?.let {
            sessionManager.setApiKey(it)
            Success
        } ?: Failure(BadResponseData)
        loginResult.postValue(result)
    }
}

@VisibleForTesting
fun ApiError.getLoginFailureReason(): LoginFailureReason {
    return when (this) {
        is ApiError.Unreachable -> NetworkIssue
        else -> GenericIssue
    }
}

sealed class LoginResult
object Success : LoginResult()
data class Failure(val reason: LoginFailureReason) : LoginResult()

sealed class LoginFailureReason
object NetworkIssue : LoginFailureReason()
object BadResponseData : LoginFailureReason()
object GenericIssue : LoginFailureReason()


