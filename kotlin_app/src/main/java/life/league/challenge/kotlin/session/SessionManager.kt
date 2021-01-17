package life.league.challenge.kotlin.session

/**
 * SessionManager is responsible for storing user's data related to the session.
 */
interface SessionManager {
    //clears all session value, call on logout
    fun purge()
    fun setApiKey(apiKey: String)
    fun getApiKey(): String?
}