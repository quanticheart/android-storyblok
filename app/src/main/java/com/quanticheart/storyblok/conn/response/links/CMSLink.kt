package com.quanticheart.storyblok.conn.response.links

import com.google.gson.annotations.SerializedName

data class CMSLink(
    @SerializedName("_uid") val _uid: String,
    @SerializedName("image") val image: String,
    @SerializedName("title") val title: String,
    @SerializedName("deeplink") val deeplink: Deeplink,
    @SerializedName("component") val component: String,
    @SerializedName("_editable") val _editable: String
)