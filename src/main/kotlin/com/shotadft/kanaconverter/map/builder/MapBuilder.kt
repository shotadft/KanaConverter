package com.shotadft.kanaconverter.map.builder

import com.shotadft.kanaconverter.io.CacheManager
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.CACHE_NAME
import com.shotadft.kanaconverter.util.LinkedFastStrMap
import com.shotadft.kanaconverter.util.MapperUtil.buildLinkedFastStrMap

/**
 * @author Shotadft
 * @since 1.1
 */
internal class MapBuilder {
    /**
     * Builds a new [LinkedFastStrMap], optionally loading from a cache for improved performance.
     *
     * This function first attempts to load an existing map from the cache named [CACHE_NAME].
     * If the cache load is successful, the cached map is used directly. Otherwise, a new
     * map is built from a base map and then saved to the cache for future use.
     *
     * @return A new [LinkedFastStrMap] instance.
     * @author Shotadft
     * @since 1.1
     */
    fun build(): LinkedFastStrMap = buildLinkedFastStrMap {
        val map = runCatching { cacheManager.load(CACHE_NAME) }
            .onFailure { it.printStackTrace() }
            .getOrNull() ?: buildBaseMap().apply {

        }.also { cacheManager.save(CACHE_NAME, it) }

        putAll(map)
    }

    private fun buildBaseMap(): LinkedFastStrMap = buildLinkedFastStrMap {

    }

    companion object {
        private const val CACHE_NAME = "MAPCACHE.bin"
        private val cacheManager = CacheManager()
    }
}