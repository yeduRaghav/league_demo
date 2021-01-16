package life.league.challenge.kotlin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import life.league.challenge.kotlin.data.network.LeagueApi
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesLeagueApi() : LeagueApi {
        return LeagueApi.create()
    }

}