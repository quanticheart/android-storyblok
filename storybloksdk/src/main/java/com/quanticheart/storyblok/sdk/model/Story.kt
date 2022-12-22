package com.quanticheart.storyblok.sdk.model

import com.google.gson.annotations.SerializedName

data class Story<T>(
    @SerializedName("story") val story: StoryData<T>,
    @SerializedName("cv") val cv: Int,
//    @SerializedName("rels") val rels : List<String>,
//    @SerializedName("links") val links : List<String>
)