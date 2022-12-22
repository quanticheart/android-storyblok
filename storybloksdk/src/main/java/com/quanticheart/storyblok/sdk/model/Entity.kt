package com.quanticheart.storyblok.sdk.model

import org.json.JSONObject

open class Entity{
    var id: Long = 0
    var name: String? = null
    var uuid: String? = null
    var slug: String? = null
    var isStartpage: Boolean = false
}
