package life.league.challenge.kotlin.data.network.endpoint

import life.league.challenge.kotlin.data.network.LeagueApi
import life.league.challenge.kotlin.data.network.endpoint.base.BaseEndpoint
import life.league.challenge.kotlin.data.network.model.PostApiModel
import javax.inject.Inject

class GetPostsEndpoint @Inject constructor(private val api: LeagueApi) : BaseEndpoint<List<PostApiModel>>() {

    override fun getCall() = api.getPosts()

}