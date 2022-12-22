package com.quanticheart.storyblok.conn.response.links

import com.google.gson.annotations.SerializedName

data class CMSLinks(
    @SerializedName("_uid") val _uid: String,
    @SerializedName("links") val links: List<CMSLink>,
    @SerializedName("component") val component: String,
    @SerializedName("_editable") val _editable: String
)