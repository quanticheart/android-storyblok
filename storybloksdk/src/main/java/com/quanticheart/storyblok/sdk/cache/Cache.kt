package com.quanticheart.storyblok.sdk.cache

import org.json.JSONObject

interface Cache {
    val cacheVersion: Long
    fun withCacheVersion(cacheVersion: Long): Cache?
    fun delete(key: String?): Cache?
    fun save(response: JSONObject?, key: String?): Cache?
    fun load(key: String?): JSONObject?
}