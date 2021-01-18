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

/**
 * ViewModel for HomeScreen
 */
class HomeScreenViewModel @ViewModelInject constructor(
        private val loginUseCase: LoginUseCase,
        private val feedUseCase: FeedUseCase
) : ViewModel() {

    private val screenState = MutableLiveData<HomeScreenState>()
    fun getScreenState(): LiveData<HomeScreenState> = screenState

    init {
        setupLoginUseCase()
        setupFeedUseCase()
    }

    fun loadHomeScreen() {
        if (loginUseCase.isLoggedIn()) {
            loadFeed()
        } else {
            login()
        }
    }


    fun postAuthorClicked(feedItem: FeedItem) {
        feedItem.user
    }

    private fun loadFeed() {
        screenState.postValue(HomeScreenState.Loading)
        loginUseCase.cancel()
        feedUseCase.loadFeed()
    }

    private fun login() {
        screenState.postValue(HomeScreenState.Loading)
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
            is LoginState.Failure -> screenState.postValue(HomeScreenState.Error(state.reason))
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
            is FeedLoadState.Success -> screenState.postValue(HomeScreenState.Loaded(state.feed))
            is FeedLoadState.Failure -> screenState.postValue(HomeScreenState.Error(state.reason))
        }
    }

}

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    data class Error(val reason: FailureReason) : HomeScreenState()
    data class Loaded(val feed: List<FeedItem>) : HomeScreenState()
}