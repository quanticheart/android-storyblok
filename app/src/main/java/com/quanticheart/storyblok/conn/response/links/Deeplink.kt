package com.quanticheart.storyblok.conn.response.links

import com.google.gson.annotations.SerializedName

data class Deeplink(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String,
    @SerializedName("linktype") val linktype: String,
    @SerializedName("fieldtype") val fieldtype: String,
    @SerializedName("cached_url") val cached_url: String
)