package xyz.coderes.ai_hilal_test.core.data

import xyz.coderes.ai_hilal_test.BuildConfig


fun constructUrl(url: String): String {
    return when {
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)
        else -> BuildConfig.BASE_URL + url
    }.apply {
        val apiKey = KeyProvider.getApiKey()
        return "$this?apiKey=$apiKey"
    }
}