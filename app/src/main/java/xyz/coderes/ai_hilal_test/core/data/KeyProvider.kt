package xyz.coderes.ai_hilal_test.core.data


//This is absolute not secure way to store keys. But for test project and temporary key is Ok
object KeyProvider {
    private const val encodedKey = "YTgwYTk2ODFjYjMwZjc2OTMxOTg2N2JjNTNlOTUyYzRjYWI0NTIxZjgwZjRjOWZmZDZkNTEzOGY1NDExODU5Yw=="

    fun getApiKey(): String {
        return String(android.util.Base64.decode(encodedKey, android.util.Base64.DEFAULT))
    }
}