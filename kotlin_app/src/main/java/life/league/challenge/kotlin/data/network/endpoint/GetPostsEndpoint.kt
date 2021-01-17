package life.league.challenge.kotlin.data.network.endpoint

import life.league.challenge.kotlin.data.network.LeagueApi
import life.league.challenge.kotlin.data.network.model.PostApiModel
import retrofit2.Call
import javax.inject.Inject

class GetPostsEndpoint @Inject constructor(
        private val api: LeagueApi
) : BaseEndpoint<List<PostApiModel>>() {

    var accessToken: String? = null //todo: fix me


    override fun getCall(): Call<List<PostApiModel>> {
        return api.getPosts(accessToken)
    }

}