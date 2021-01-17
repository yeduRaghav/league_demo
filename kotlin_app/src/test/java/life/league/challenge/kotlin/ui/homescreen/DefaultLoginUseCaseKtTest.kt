package life.league.challenge.kotlin.ui.homescreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import life.league.challenge.kotlin.data.network.endpoint.LoginEndpoint
import life.league.challenge.kotlin.data.network.endpoint.base.ApiError
import life.league.challenge.kotlin.data.network.model.LoginApiResponse
import life.league.challenge.kotlin.session.SessionManager
import life.league.challenge.kotlin.ui.homescreen.usecase.login.DefaultLoginUsecase
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.homescreen.usecase.login.LoginState
import life.league.challenge.kotlin.ui.homescreen.usecase.login.getLoginFailureReason
import life.league.challenge.kotlin.util.getOrAwaitValue
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class DefaultLoginUseCaseKtTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun `isLoggedIn returns false if apiKey is null`() {
        val endpoint = Mockito.mock(LoginEndpoint::class.java)
        val sessionManager = Mockito.mock(SessionManager::class.java)
        Mockito.`when`(sessionManager.getApiKey()).then { return@then null }
        val useCase = DefaultLoginUsecase(sessionManager, endpoint)
        assert(!useCase.isLoggedIn())
    }

    @Test
    fun `isLoggedIn returns false if apiKey is notNull`() {
        val endpoint = Mockito.mock(LoginEndpoint::class.java)
        val sessionManager = Mockito.mock(SessionManager::class.java)
        Mockito.`when`(sessionManager.getApiKey()).then { return@then "" }
        val useCase = DefaultLoginUsecase(sessionManager, endpoint)
        assert(useCase.isLoggedIn())
    }


    @Test
    fun `ApiError_getLoginFailureReason() returns NetworkIssue for Unreachable`() {
        Assert.assertTrue(ApiError.Unreachable(null).getLoginFailureReason() == FailureReason.NetworkIssue)
    }

    @Test
    fun `BadResponseData is returned if apiKey is null in LoginApiResponse`() {
        val endpoint = Mockito.mock(LoginEndpoint::class.java)
        val sessionManager = Mockito.mock(SessionManager::class.java)
        val useCase = DefaultLoginUsecase(sessionManager, endpoint)

        useCase.onApiCallSuccess(LoginApiResponse(null))

        val result = useCase.loginResult.getOrAwaitValue()
        assert(result is LoginState.Failure && result.reason is FailureReason.BadResponseData)
    }

    @Test
    fun `SessionManager_setApiKey is called if apiKey is notNull in LoginApiResponse`() {
        val endpoint = Mockito.mock(LoginEndpoint::class.java)
        val sessionManager = Mockito.spy(SessionManager::class.java)
        val useCase = DefaultLoginUsecase(sessionManager, endpoint)
        val apiKey = "real Api key"
        useCase.onApiCallSuccess(LoginApiResponse(apiKey))
        Mockito.verify(sessionManager).setApiKey(apiKey)
    }


}