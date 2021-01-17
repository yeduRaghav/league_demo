package life.league.challenge.kotlin.ui.homescreen.usecase.login

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope

/**
 * This Usecase takes care of logging-in
 */
interface LoginUseCase {
    fun getLoginResult(): LiveData<LoginState>
    fun setScope(scope: CoroutineScope)
    fun isLoggedIn(): Boolean
    fun login(userName: String, password: String)
    fun cancel()
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