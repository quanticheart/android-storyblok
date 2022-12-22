package com.quanticheart.storyblok.sdk

import com.quanticheart.storyblok.sdk.model.Result
import com.quanticheart.storyblok.sdk.model.Story

//
// Created by Jonn Alves on 20/12/22.
//
interface StoryblokStoryCallback<Model> {
    fun onResponse(result: Result<Story<Model>>)
    fun onFailure(exception: Exception?, response: String?)
}