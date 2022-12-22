package com.quanticheart.storyblok.conn.response

import com.google.gson.annotations.SerializedName

data class CMSBottomMenuItem(
    @SerializedName("_uid") val _uid: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("title") val title: String,
    @SerializedName("deeplink") val deeplink: String,
    @SerializedName("component") val component: String,
    @SerializedName("_editable") val _editable: String
)