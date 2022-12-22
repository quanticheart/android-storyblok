@file:Suppress("unused")

package com.quanticheart.storyblok.sdk

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quanticheart.storyblok.sdk.StoryblokConstants.API_ENDPOINT
import com.quanticheart.storyblok.sdk.StoryblokConstants.API_PROTOCOL
import com.quanticheart.storyblok.sdk.StoryblokConstants.API_VERSION
import com.quanticheart.storyblok.sdk.StoryblokConstants.ENDPOINT_DATASOURCE
import com.quanticheart.storyblok.sdk.StoryblokConstants.ENDPOINT_LINKS
import com.quanticheart.storyblok.sdk.StoryblokConstants.ENDPOINT_STORIES
import com.quanticheart.storyblok.sdk.StoryblokConstants.ENDPOINT_TAGS
import com.quanticheart.storyblok.sdk.StoryblokConstants.ERROR_TAG
import com.quanticheart.storyblok.sdk.StoryblokConstants.ERROR_TEXT
import com.quanticheart.storyblok.sdk.StoryblokConstants.SDK_USER_AGENT
import com.quanticheart.storyblok.sdk.StoryblokConstants.VERSION_DRAFT
import com.quanticheart.storyblok.sdk.StoryblokConstants.VERSION_PUBLISHED
import com.quanticheart.storyblok.sdk.cache.Cache
import com.quanticheart.storyblok.sdk.model.Link
import com.quanticheart.storyblok.sdk.model.Result
import com.quanticheart.storyblok.sdk.model.Story
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class StoryblokBuilder {
    companion object {
        private lateinit var token: String
        private var SINGLETON: StoryblokBuilder? = null

        fun build(token: String): StoryblokBuilder {
            Companion.token = token
            if (SINGLETON == null) {
                SINGLETON = StoryblokBuilder()
            }
            return SINGLETON as StoryblokBuilder
        }
    }

    var logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient()
        .newBuilder()
        .addInterceptor(logging)
        .build()

    private var cache: Cache? = null
    private var editMode = false

    fun withCache(cache: Cache): StoryblokBuilder {
        this.cache = cache
        return this
    }

    fun withEditMode(editMode: Boolean): StoryblokBuilder {
        this.editMode = editMode
        return this
    }

    fun toJsonObjectAndCache(key: String, result: String): JSONObject? {
        try {
            val jsonObject = JSONObject(result)
            cache?.save(jsonObject, key)
            return jsonObject
        } catch (e: JSONException) {
            Log.e(ERROR_TAG, "$ERROR_TEXT ::: $e")
        }
        return null
    }

    fun buildCacheKey(vararg `val`: String?): String {
        if (cache == null) {
            return ""
        }
        val res = StringBuilder()
        for (v in `val`) {
            res.append(v)
        }
        return res.toString()
    }

    fun reCacheOnPublish(key: String) {
        if (cache == null) {
            return
        }
        val storyblokPublished = "storyblokPublishedProject"
        val item = cache?.load(key)
        if (item != null) {
            try {
                if (item.has("story") && item.getJSONObject("story")
                        .get("id") === storyblokPublished
                ) {
                    cache?.delete(key)
                }
            } catch (e: JSONException) {
                Log.e(ERROR_TAG, ERROR_TEXT)
            }

            // Always refresh cache of links
            cache?.delete(ENDPOINT_LINKS)
            setCacheVersion()
        }
    }

    private fun setCacheVersion() {
        cache?.withCacheVersion(System.currentTimeMillis())
    }

    private val requestBuild: Request.Builder
        get() = Request.Builder()

    fun buildRequest(url: HttpUrl.Builder): Request {
        return requestBuild
            .url(url.build())
            .header("User-Agent", SDK_USER_AGENT)
            .build()
    }

    fun buildUrl(method: String): HttpUrl.Builder {
        return HttpUrl.Builder()
            .scheme(API_PROTOCOL)
            .host(API_ENDPOINT)
            .addPathSegment(API_VERSION)
            .addPathSegment("cdn")
            .addPathSegment(method)
            .addQueryParameter("token", token)
            .addQueryParameter("version", if (editMode) VERSION_DRAFT else VERSION_PUBLISHED)
    }

    inline fun <reified T> getStory(slug: String, callback: StoryblokStoryCallback<T>) {
        val cacheKey = buildCacheKey(ENDPOINT_STORIES, "slug", slug)
        reCacheOnPublish(cacheKey)

        client
            .newCall(buildRequest(buildUrl(ENDPOINT_STORIES).addPathSegments(slug)))
            .enqueue(object :
                Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Storyblok Error", e.message ?: "Error")
                    callback.onFailure(e, null)
                }

                override fun onResponse(call: Call, response: Response) {
                    handleStoryConnection<T>(cacheKey, response, callback)
                }
            })
    }

    fun getStories(
        startsWith: String?,
        withTag: String?,
        sortBy: String?,
        perPage: Int?,
        page: Int?,
        callback: StoryblokCallback<Link>
    ) {
        val cacheKey = buildCacheKey(
            ENDPOINT_STORIES,
            "starts_with",
            startsWith,
            "with_tag",
            withTag,
            "sort_by",
            sortBy,
            "per_page",
            perPage.toString(),
            "page",
            page.toString()
        )
        reCacheOnPublish(cacheKey)

        client.newCall(
            buildRequest(
                buildUrl(ENDPOINT_STORIES)
                    .addQueryParameter("starts_with", startsWith)
                    .addQueryParameter("with_tag", withTag)
                    .addQueryParameter("sort_by", sortBy)
                    .addQueryParameter("per_page", perPage?.toString())
                    .addQueryParameter("page", page?.toString())
                    .addQueryParameter("cache_version", cache?.cacheVersion?.toString())
            )
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e, null)
            }

            override fun onResponse(call: Call, response: Response) {
                handleConnection(cacheKey, response, callback)
            }
        })
    }

    fun getTags(
        startsWith: String?,
        callback: StoryblokCallback<Link>
    ) {
        val cacheKey = buildCacheKey(ENDPOINT_TAGS, "starts_with", startsWith)
        reCacheOnPublish(cacheKey)

        client.newCall(
            buildRequest(
                buildUrl(ENDPOINT_TAGS).addQueryParameter(
                    "starts_with",
                    startsWith
                )
            )
        ).enqueue(object :
            Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e, null)
            }

            override fun onResponse(call: Call, response: Response) {
                handleConnection(cacheKey, response, callback)
            }
        })
    }

    fun getLinks(callback: StoryblokCallback<Link>) {
        val cacheKey = buildCacheKey(ENDPOINT_LINKS)
        reCacheOnPublish(cacheKey)

        client.newCall(buildRequest(buildUrl(ENDPOINT_LINKS))).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e, null)
            }

            override fun onResponse(call: Call, response: Response) {
                handleConnection(cacheKey, response, callback)
            }
        })
    }

    fun getDatasource(
        datasource: String?,
        callback: StoryblokCallback<Link>
    ) {
        val cacheKey = buildCacheKey(ENDPOINT_DATASOURCE, "datasource", datasource)
        reCacheOnPublish(cacheKey)

        client.newCall(
            buildRequest(
                buildUrl(ENDPOINT_DATASOURCE).addQueryParameter(
                    "datasource",
                    datasource
                )
            )
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e, null)
            }

            override fun onResponse(call: Call, response: Response) {
                handleConnection(cacheKey, response, callback)
            }
        })
    }

    private inline fun <reified T> handleConnection(
        cacheKey: String,
        response: Response,
        callback: StoryblokCallback<T>
    ) {
        try {
            if (response.code >= 300) {
                Log.d("Storyblok Response", "Status Code = ${response.code}")
                callback.onFailure(null, response.body?.string())
            } else {
                Log.d("Storyblok", "Success connection")
                val json = response.body?.string() ?: ""
                if (json.isNotEmpty())
                    toJsonObjectAndCache(cacheKey, json)
                val obj2 = Gson().fromJson(json, T::class.java)
                callback.onResponse(Result(response.headers, obj2))
            }
        } catch (e: Exception) {
            Log.e("Storyblok Error", e.message ?: "Error")
            callback.onFailure(e, null)
        }
    }

    inline fun <reified T> handleStoryConnection(
        cacheKey: String,
        response: Response,
        callback: StoryblokStoryCallback<T>
    ) {
        try {
            if (response.code >= 300) {
                Log.d("Storyblok Response", "Status Code = ${response.code}")
                callback.onFailure(null, response.body?.string())
            } else {
                val collectionType = object : TypeToken<Story<T>>() {}.type
                Log.d("Storyblok", "Success connection")
                val json = response.body?.string() ?: ""
                if (json.isNotEmpty())
                    toJsonObjectAndCache(cacheKey, json)
                val obj2 = Gson().fromJson<Story<T>>(json, collectionType)
                callback.onResponse(Result(response.headers, obj2))
            }
        } catch (e: Exception) {
            Log.e("Storyblok Error", e.message ?: "Error")
            callback.onFailure(e, null)
        }
    }
}
