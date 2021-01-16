package life.league.challenge.kotlin.data.network.endpoint

import life.league.challenge.kotlin.data.network.LeagueApi
import life.league.challenge.kotlin.data.network.model.LoginApiResponse
import retrofit2.Call
import javax.inject.Inject

/**
 * Login Api Endpoint abstraction
 */
class LoginEndpoint @Inject constructor(private val api: LeagueApi) : BaseEndpoint<LoginApiResponse>() {

    private var credentials: String? = null

    fun setData(credentials: String?) {
        this.credentials = credentials
    }

    override fun getCall(): Call<LoginApiResponse> {
        return api.login(credentials)
    }
}