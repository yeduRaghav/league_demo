package life.league.challenge.kotlin.ui.homescreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.FeedLoadState
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.FeedUseCase
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import life.league.challenge.kotlin.ui.homescreen.usecase.login.LoginState
import life.league.challenge.kotlin.ui.homescreen.usecase.login.LoginUseCase
import life.league.challenge.kotlin.ui.model.FeedItem
import life.league.challenge.kotlin.ui.model.User

/**
 * ViewModel for HomeScreen
 */
class HomeScreenViewModel @ViewModelInject constructor(
        private val loginUseCase: LoginUseCase,
        private val feedUseCase: FeedUseCase
) : ViewModel() {

    private val feedState = MutableLiveData<FeedState>()
    private val userToShow = MutableLiveData<User>()

    fun getFeedState(): LiveData<FeedState> = feedState
    fun getClickedPost(): LiveData<User> = userToShow

    init {
        setupLoginUseCase()
        setupFeedUseCase()
    }

    fun onFeedScreenReady() {
        reloadFeed()
    }

    fun reloadFeed() {
        if (loginUseCase.isLoggedIn()) {
            loadFeed()
        } else {
            login()
        }
    }

    fun onUserClicked(user: User) {
        userToShow.postValue(user)
    }

    private fun loadFeed() {
        feedState.postValue(FeedState.Loading)
        loginUseCase.cancel()
        feedUseCase.loadFeed()
    }

    private fun login() {
        feedState.postValue(FeedState.Loading)
        feedUseCase.cancel()
        loginUseCase.login("", "") // leaving params as blank for now,no story for proper login flow
    }

    private fun setupLoginUseCase() {
        loginUseCase.apply {
            setScope(viewModelScope)
            getLoginResult().observeForever { onLoginUseCaseStateUpdated(it) }
        }
    }

    private fun onLoginUseCaseStateUpdated(state: LoginState) {
        when (state) {
            is LoginState.Success -> loadFeed()
            is LoginState.Failure -> feedState.postValue(FeedState.Error(state.reason))
        }
    }

    private fun setupFeedUseCase() {
        feedUseCase.apply {
            setScope(viewModelScope)
            getLoadState().observeForever { onFeedUseCaseStateUpdated(it) }
        }
    }

    private fun onFeedUseCaseStateUpdated(state: FeedLoadState) {
        when (state) {
            is FeedLoadState.Success -> feedState.postValue(FeedState.Loaded(state.feed))
            is FeedLoadState.Failure -> feedState.postValue(FeedState.Error(state.reason))
        }
    }

}

sealed class FeedState {
    object Loading : FeedState()
    data class Error(val reason: FailureReason) : FeedState()
    data class Loaded(val feed: List<FeedItem>) : FeedState()
}
