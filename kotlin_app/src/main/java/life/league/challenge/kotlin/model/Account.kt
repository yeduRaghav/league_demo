package life.league.challenge.kotlin.model

import com.google.gson.annotations.SerializedName

@Deprecated("")
data class Account(@SerializedName("api_key") val apiKey: String? = null)
