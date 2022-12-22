package com.quanticheart.storyblok.conn

import com.quanticheart.storyblok.conn.mappers.MapperAboutLinks
import com.quanticheart.storyblok.conn.mappers.MapperBottomMenu
import com.quanticheart.storyblok.conn.model.BottomMenu
import com.quanticheart.storyblok.conn.model.Links
import com.quanticheart.storyblok.conn.response.CMSBottomMenu
import com.quanticheart.storyblok.conn.response.links.CMSLinkDetails
import com.quanticheart.storyblok.conn.response.links.CMSLinks
import com.quanticheart.storyblok.sdk.StoryblokBuilder
import com.quanticheart.storyblok.sdk.StoryblokStoryCallback
import com.quanticheart.storyblok.sdk.model.Result
import com.quanticheart.storyblok.sdk.model.Story
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//
// Created by Jonn Alves on 20/12/22.
//
class ConnStoryblok {

    private val storyblok = StoryblokBuilder
        .build("mfLvigZlFn...")
        .withEditMode(true)

    fun getBottonMenu(callback: (List<BottomMenu>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            storyblok.getStory(
                "botton-navigation",
                object : StoryblokStoryCallback<CMSBottomMenu> {
                    override fun onResponse(result: Result<Story<CMSBottomMenu>>) {
                        callback(MapperBottomMenu().map(result.result.story.content?.menu))
                    }

                    override fun onFailure(exception: Exception?, response: String?) {

                    }
                })
        }
    }

    fun getLinks(callback: (List<Links>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            storyblok.getStory(
                "about-pages/about-page",
                object : StoryblokStoryCallback<CMSLinks> {
                    override fun onResponse(result: Result<Story<CMSLinks>>) {
                        callback(MapperAboutLinks().map(result.result.story.content?.links))
                    }

                    override fun onFailure(exception: Exception?, response: String?) {

                    }
                })
        }
    }

    fun getLinkDetails(slug: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            storyblok.getStory(
                slug,
                object : StoryblokStoryCallback<CMSLinkDetails> {
                    override fun onResponse(result: Result<Story<CMSLinkDetails>>) {
                        callback(result.result.story.content?.page ?: "")
                    }

                    override fun onFailure(exception: Exception?, response: String?) {

                    }
                })
        }
    }
}