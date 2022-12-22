package com.quanticheart.storyblok.conn.response

import com.google.gson.annotations.SerializedName

data class CMSBottomMenu(
    @SerializedName("_uid") val _uid: String,
    @SerializedName("menu") val menu: List<CMSBottomMenuItem>,
    @SerializedName("component") val component: String,
    @SerializedName("_editable") val _editable: String
)