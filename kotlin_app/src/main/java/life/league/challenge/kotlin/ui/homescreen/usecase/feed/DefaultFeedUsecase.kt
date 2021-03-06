package life.league.challenge.kotlin.ui.homescreen.usecase.feed

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import life.league.challenge.kotlin.data.network.endpoint.GetPostsEndpoint
import life.league.challenge.kotlin.data.network.endpoint.GetUsersEndpoint
import life.league.challenge.kotlin.data.network.endpoint.base.ApiError
import life.league.challenge.kotlin.data.network.model.PostApiModel
import life.league.challenge.kotlin.data.network.model.UserApiModel
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.model.FeedItem
import life.league.challenge.kotlin.ui.model.Post
import life.league.challenge.kotlin.ui.model.User
import life.league.challenge.kotlin.util.Either
import javax.inject.Inject

class DefaultFeedUseCase @Inject constructor(
        private val postsEndpoint: GetPostsEndpoint,
        private val usersEndpoint: GetUsersEndpoint
) : FeedUseCase {
    private lateinit var scope: CoroutineScope
    private var feedJob: Job? = null
    private val loadState = MutableLiveData<FeedLoadState>()

    override fun setScope(scope: CoroutineScope) {
        this.scope = scope
    }

    override fun getLoadState(): LiveData<FeedLoadState> = loadState

    override fun cancel() {
        feedJob?.cancel()
    }

    override fun loadFeed() {
        feedJob = scope.launch {
            val postResponse = async { postsEndpoint.execute() }
            val userResponse = async { usersEndpoint.execute() }
            handleResponse(postResponse.await(), userResponse.await())
        }
    }

    private suspend fun handleResponse(postResponse: PostsApiResponse, userResponse: UsersApiResponse) {
        if (postResponse is Either.Failure) {
            return onApiError(postResponse.error)
        }
        if (userResponse is Either.Failure) {
            return onApiError(userResponse.error)
        }
        if (postResponse is Either.Success && userResponse is Either.Success) {
            handleOnApiCallSuccess(postResponse.value, userResponse.value)
        }
    }

    private fun onApiError(apiError: ApiError) {
        loadState.postValue(FeedLoadState.Failure(apiError.getFeedFailureReason()))
    }

    private suspend fun handleOnApiCallSuccess(postsResponse: List<PostApiModel>, usersResponse: List<UserApiModel>) {
        val feedItems = postsResponse.toFeedItems(usersResponse.toUiModels())
        loadState.postValue(FeedLoadState.Success(feedItems))
    }

}


private typealias PostsApiResponse = Either<ApiError, List<PostApiModel>>
private typealias UsersApiResponse = Either<ApiError, List<UserApiModel>>


@VisibleForTesting
suspend fun List<PostApiModel>.toFeedItems(users: Map<Long, User>): List<FeedItem> {
    return withContext(Dispatchers.Default) {
        mapNotNull {
            users[it.userId]?.let { user -> FeedItem(user, it.toUiModel()) }
        }
    }
}

@VisibleForTesting
fun PostApiModel.toUiModel(): Post {
    return Post(postId = id, title = title, description = body)
}

@VisibleForTesting
suspend fun List<UserApiModel>.toUiModels(): Map<Long, User> {
    return withContext(Dispatchers.Default) {
        associate { it.id to it.toUiModel() }
    }
}

@VisibleForTesting
fun UserApiModel.toUiModel(): User {
    return User(
            userId = id,
            userName = username,
            name = name,
            avatar = avatar,
            phone = phone,
            email = email,
            website = website
    )
}

@VisibleForTesting
fun ApiError.getFeedFailureReason(): FailureReason {
    return when (this) {
        is ApiError.Unreachable -> FailureReason.NetworkIssue
        is ApiError.ClientError.Unauthorised -> FailureReason.InvalidCredentials
        else -> FailureReason.GenericIssue
    }
}



