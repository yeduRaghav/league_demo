package life.league.challenge.kotlin.data.network.endpoint

import life.league.challenge.kotlin.data.network.LeagueApi
import life.league.challenge.kotlin.data.network.model.UserApiModel
import life.league.challenge.kotlin.session.SessionManager
import retrofit2.Call
import javax.inject.Inject

class GetUsersEndpoint @Inject constructor(
        private val sessionManager: SessionManager,
        private val api: LeagueApi
) : BaseEndpoint<List<UserApiModel>>() {

    override fun getCall(): Call<List<UserApiModel>> {
        return api.getUsers(sessionManager.getApiKey())
    }

}