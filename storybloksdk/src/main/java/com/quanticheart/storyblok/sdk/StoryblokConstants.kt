package com.quanticheart.storyblok.sdk

object StoryblokConstants {
    const val ERROR_TAG = "Storyblok"
    const val ERROR_TEXT = "Sorry for the inconvience. Something broken was sent by the server"

    const val API_PROTOCOL = "https"
    const val API_ENDPOINT = "api.storyblok.com"
    const val API_VERSION = "v2"

    const val SDK_VERSION = "0.1"
    const val SDK_USER_AGENT = "storyblok-sdk-android/$SDK_VERSION"

    const val VERSION_PUBLISHED = "published"
    const val VERSION_DRAFT = "draft"

    const val ENDPOINT_STORIES = "stories" // the base endpoint for stories
    const val ENDPOINT_SPACE = "spaces/me" // the endpoint for the current space
    const val ENDPOINT_LINKS = "links" // the endpoint for links
    const val ENDPOINT_TAGS = "tags" // the endpoint for tags
    const val ENDPOINT_DATASOURCE = "datasources" // the endpoint for datasources
    const val ENDPOINT_DATASOURCE_ENTRIES = "datasource_entries" // the endpoint for datasource entries
}
