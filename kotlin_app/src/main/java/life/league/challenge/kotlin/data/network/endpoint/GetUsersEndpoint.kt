package life.league.challenge.kotlin.data.network.endpoint

import life.league.challenge.kotlin.data.network.LeagueApi
import life.league.challenge.kotlin.data.network.endpoint.base.BaseEndpoint
import life.league.challenge.kotlin.data.network.model.UserApiModel
import javax.inject.Inject

class GetUsersEndpoint @Inject constructor(private val api: LeagueApi)
    : BaseEndpoint<List<UserApiModel>>() {

    override fun getCall() = api.getUsers()
}