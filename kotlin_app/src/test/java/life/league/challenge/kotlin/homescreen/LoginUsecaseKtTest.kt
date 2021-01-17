package life.league.challenge.kotlin.homescreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import life.league.challenge.kotlin.data.network.endpoint.ApiError
import life.league.challenge.kotlin.data.network.endpoint.LoginEndpoint
import life.league.challenge.kotlin.data.network.model.LoginApiResponse
import life.league.challenge.kotlin.session.SessionManager
import life.league.challenge.kotlin.util.getOrAwaitValue
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class LoginUsecaseKtTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `ApiError_getLoginFailureReason() returns NetworkIssue for Unreachable`() {
        Assert.assertTrue(ApiError.Unreachable(null).getLoginFailureReason() == NetworkIssue)
    }

    @Test
    fun `BadResponseData is returned if apiKey is null in LoginApiResponse`() {
        val endpoint = Mockito.mock(LoginEndpoint::class.java)
        val sessionManager = Mockito.mock(SessionManager::class.java)
        val useCase = LoginUsecase(sessionManager, endpoint)

        useCase.onApiCallSuccess(LoginApiResponse(null))

        val result = useCase.loginResult.getOrAwaitValue()
        assert(result is Failure && result.reason is BadResponseData)
    }

    @Test
    fun `SessionManager_setApiKey is called if apiKey is notNull in LoginApiResponse`() {
        val endpoint = Mockito.mock(LoginEndpoint::class.java)
        val sessionManager = Mockito.spy(SessionManager::class.java)
        val useCase = LoginUsecase(sessionManager, endpoint)
        val apiKey = "real Api key"
        useCase.onApiCallSuccess(LoginApiResponse(apiKey))
        Mockito.verify(sessionManager).setApiKey(apiKey)
    }


}