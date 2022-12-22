package com.quanticheart.storyblok.conn.mappers

import com.quanticheart.storyblok.conn.model.BottomMenu
import com.quanticheart.storyblok.conn.response.CMSBottomMenuItem

//
// Created by Jonn Alves on 20/12/22.
//
class MapperBottomMenu {
    fun map(data: List<CMSBottomMenuItem>?): List<BottomMenu> {
        return data?.map {
            BottomMenu(
                it.deeplink,
                it.title,
                it.icon
            )
        } ?: run {
            emptyList()
        }
    }
}