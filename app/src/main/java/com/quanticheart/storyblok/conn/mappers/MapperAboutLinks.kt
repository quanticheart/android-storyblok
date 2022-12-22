package com.quanticheart.storyblok.conn.mappers

import com.quanticheart.storyblok.conn.model.Links
import com.quanticheart.storyblok.conn.response.links.CMSLink

//
// Created by Jonn Alves on 20/12/22.
//
class MapperAboutLinks {
    fun map(data: List<CMSLink>?): List<Links> {
        return data?.map {
            Links(
                it.deeplink.cached_url,
                it.title,
                it.image
            )
        } ?: run {
            emptyList()
        }
    }
}