package com.quanticheart.storyblok.sdk

import com.quanticheart.storyblok.sdk.model.Result

//
// Created by Jonn Alves on 20/12/22.
//
interface StoryblokCallback<Model> {
    fun onResponse(result: Result<Model>)
    fun onFailure(exception: Exception?, response: String?)
}