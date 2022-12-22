package com.quanticheart.storyblok.sdk.model

import com.google.gson.annotations.SerializedName

data class StoryData<T>(
    @SerializedName("name") val name: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("published_at") val published_at: String,
    @SerializedName("id") val id: Int,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("content") val content: T? = null,
    @SerializedName("slug") val slug: String,
    @SerializedName("full_slug") val full_slug: String,
    @SerializedName("sort_by_date") val sort_by_date: String,
    @SerializedName("position") val position: Int,
    @SerializedName("tag_list") val tag_list: List<String>,
    @SerializedName("is_startpage") val is_startpage: Boolean,
    @SerializedName("parent_id") val parent_id: Int,
    @SerializedName("meta_data") val meta_data: String,
    @SerializedName("group_id") val group_id: String,
    @SerializedName("first_published_at") val first_published_at: String,
    @SerializedName("release_id") val release_id: String,
    @SerializedName("lang") val lang: String,
    @SerializedName("path") val path: String,
    @SerializedName("alternates") val alternates: List<String>,
    @SerializedName("default_full_slug") val default_full_slug: String,
    @SerializedName("translated_slugs") val translated_slugs: String
)