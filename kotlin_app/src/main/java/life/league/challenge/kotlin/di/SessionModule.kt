package life.league.challenge.kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import life.league.challenge.kotlin.session.DefaultSessionManager
import life.league.challenge.kotlin.session.SessionManager
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class SessionModule {

    @Provides
    @Singleton
    fun providesSessionManager(): SessionManager {
        return DefaultSessionManager()
    }

}