package life.league.challenge.kotlin.ui.homescreen

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
    val loginResult = MutableLiveData<LoginState>()

    fun getLoginResult(): LiveData<LoginState> = loginResult

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
        loginResult.postValue(LoginState.Failure(failureReason))
    }

    @VisibleForTesting
    fun onApiCallSuccess(response: LoginApiResponse) {
        val result = response.apiKey?.let {
            sessionManager.setApiKey(it)
            LoginState.Success
        } ?: LoginState.Failure(FailureReason.BadResponseData)
        loginResult.postValue(result)
    }
}


sealed class LoginState {
    object Success : LoginState()
    data class Failure(val reason: FailureReason) : LoginState()
}

//todo: should I be global ?
sealed class FailureReason {
    object InvalidCredentials : FailureReason()
    object BadResponseData : FailureReason()
    object NetworkIssue : FailureReason()
    object GenericIssue : FailureReason()
}


@VisibleForTesting
fun ApiError.getLoginFailureReason(): FailureReason {
    return when (this) {
        is ApiError.Unreachable -> FailureReason.NetworkIssue
        else -> FailureReason.GenericIssue
    }
}



