package life.league.challenge.kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import life.league.challenge.kotlin.data.network.LeagueApi
import life.league.challenge.kotlin.data.network.interceptor.AuthorisationInterceptor
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesLeagueApi(authorisationInterceptor: AuthorisationInterceptor): LeagueApi {
        return LeagueApi.create(authorisationInterceptor)
    }

}