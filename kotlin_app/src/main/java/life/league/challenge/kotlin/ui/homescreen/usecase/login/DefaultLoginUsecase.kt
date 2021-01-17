package life.league.challenge.kotlin.ui.homescreen.usecase.login

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
class DefaultLoginUsecase @Inject constructor(
        private val sessionManager: SessionManager,
        private val loginEndpoint: LoginEndpoint
) : LoginUseCase {

    private var loginJob: Job? = null
    private lateinit var scope: CoroutineScope

    @VisibleForTesting
    val loginResult = MutableLiveData<LoginState>()

    override fun getLoginResult(): LiveData<LoginState> = loginResult

    /**
     * Make sure scope is set before invoking login()
     * */
    override fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }

    override fun isLoggedIn(): Boolean {
        return sessionManager.getApiKey() != null
    }

    override fun login(userName: String, password: String) {
        loginEndpoint.setCredentials(userName, password)
        attemptLogin()
    }

    override fun cancel() {
        loginJob?.cancel()
    }

    private fun attemptLogin() {
        cancel()
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


@VisibleForTesting
fun ApiError.getLoginFailureReason(): FailureReason {
    return when (this) {
        is ApiError.Unreachable -> FailureReason.NetworkIssue
        else -> FailureReason.GenericIssue
    }
}



