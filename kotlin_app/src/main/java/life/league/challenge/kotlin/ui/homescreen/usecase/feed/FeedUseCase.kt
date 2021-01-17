package life.league.challenge.kotlin.ui.homescreen.usecase.feed

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.model.FeedItem

/**
 * UseCase takes care of showing a feed of posts
 */
interface FeedUseCase {
    fun setScope(scope: CoroutineScope)
    fun getLoadState(): LiveData<FeedLoadState>
    fun loadFeed()
    fun cancel()
}

sealed class FeedLoadState {
    data class Success(val feed: List<FeedItem>) : FeedLoadState()
    data class Failure(val reason: FailureReason) : FeedLoadState()
}