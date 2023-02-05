package com.example.titkov.data.cache

interface CachedResource {
    val cache: MutableMap<String, Any>

    class Delegate : CachedResource {
        override val cache = mutableMapOf<String, Any>()
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <R> CachedResource.cacheResult(
    key: String,
    forceUpdate: Boolean = false,
    calculation: () -> R
): R {
    if (!forceUpdate && cache[key] != null) return cache[key] as R
    try {
        return calculation().also { result ->
            (cache as MutableMap<String, R>)[key] = result
        }
    } catch (throwable: Throwable) {
        throw throwable
    }
}