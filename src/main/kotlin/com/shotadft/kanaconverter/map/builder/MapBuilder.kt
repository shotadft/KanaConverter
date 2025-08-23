package com.shotadft.kanaconverter.map.builder

import com.shotadft.kanaconverter.io.CacheManager
import com.shotadft.kanaconverter.util.LinkedFastStrMap
import com.shotadft.kanaconverter.util.MapperUtil.buildLinkedFastStrMap

internal class MapBuilder {
    fun build(): LinkedFastStrMap = buildLinkedFastStrMap {
        cacheManager.load(CACHE_NAME)
    }

    companion object {
        private const val CACHE_NAME = "MAPCACHE.bin"
        private val cacheManager = CacheManager()
    }
}