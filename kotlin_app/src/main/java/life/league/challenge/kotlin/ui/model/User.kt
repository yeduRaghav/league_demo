package life.league.challenge.kotlin.ui.model

/**
 * Describes a user
 * */
data class User(
        val userId: Long,
        val userName: String,
        val avatar: String,
        val phone: String,
        val email: String,
        val website: String
)