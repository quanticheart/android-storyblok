package com.quanticheart.storyblok.sdk.model

import okhttp3.Headers

class Result<Model>(var header: Headers?, var result: Model)
