package com.quanticheart.storyblok.conn.response.links

import com.google.gson.annotations.SerializedName

data class CMSLinkDetails(
    @SerializedName("_uid") val _uid: String,
    @SerializedName("page") val page: String,
    @SerializedName("component") val component: String,
    @SerializedName("_editable") val _editable: String
)