package life.league.challenge.kotlin.data.network.model

import com.google.gson.annotations.SerializedName

data class PostApiModel(
        @SerializedName("id") val id: Long,
        @SerializedName("title") val title: String,
        @SerializedName("body") val body: String,
        @SerializedName("userId") val userId: Long
)