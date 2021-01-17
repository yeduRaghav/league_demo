package life.league.challenge.kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import life.league.challenge.kotlin.data.network.endpoint.GetPostsEndpoint
import life.league.challenge.kotlin.data.network.endpoint.GetUsersEndpoint
import life.league.challenge.kotlin.data.network.endpoint.LoginEndpoint
import life.league.challenge.kotlin.session.SessionManager
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.DefaultFeedUseCase
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.FeedUseCase
import life.league.challenge.kotlin.ui.homescreen.usecase.login.DefaultLoginUsecase
import life.league.challenge.kotlin.ui.homescreen.usecase.login.LoginUseCase

/**
 * Provides UseCases
 */
@InstallIn(ApplicationComponent::class)
@Module
class UseCaseModule {

    @Provides
    fun providesLoginUseCase(sessionManager: SessionManager, loginEndpoint: LoginEndpoint): LoginUseCase {
        return DefaultLoginUsecase(sessionManager, loginEndpoint)
    }

    @Provides
    fun providesFeedUseCase(postsEndpoint: GetPostsEndpoint, usersEndpoint: GetUsersEndpoint): FeedUseCase {
        return DefaultFeedUseCase(postsEndpoint, usersEndpoint)
    }


}