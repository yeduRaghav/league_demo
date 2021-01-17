package life.league.challenge.kotlin.session

import androidx.annotation.VisibleForTesting
import javax.inject.Inject

class DefaultSessionManager @Inject constructor() : SessionManager {

    @VisibleForTesting
    var _apiKey: String? = null

    override fun purge() {
        _apiKey = null
    }

    override fun setApiKey(apiKey: String) {
        this._apiKey = apiKey
    }

    override fun getApiKey() = _apiKey

}