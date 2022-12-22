package com.quanticheart.storyblok.conn.response.links

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id") val id: String,
    @SerializedName("alt") val alt: String,
    @SerializedName("name") val name: String,
    @SerializedName("focus") val focus: String,
    @SerializedName("title") val title: String,
    @SerializedName("filename") val filename: String,
    @SerializedName("copyright") val copyright: String,
    @SerializedName("fieldtype") val fieldtype: String
)