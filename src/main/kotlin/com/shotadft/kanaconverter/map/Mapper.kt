/**
 * Copyright 2025 Shotadft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shotadft.kanaconverter.map

import com.shotadft.kanaconverter.map.builder.MapBuilder
import com.shotadft.kanaconverter.map.util.LinkedFastStrMap
import com.shotadft.kanaconverter.map.util.MapperUtil.linkedFastMapOf
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap

/**
 * @author Shotadft
 * @since 1.1
 */
object Mapper {
    private val builder = MapBuilder()

    @JvmStatic
    internal val k2rMap: LinkedFastStrMap by lazy { builder.build() }

    @JvmStatic
    internal val r2kMap: Object2ObjectLinkedOpenHashMap<String, String> by lazy { k2rMap.invert() }

    @JvmStatic
    internal fun LinkedFastStrMap.invert(): Object2ObjectLinkedOpenHashMap<String, String> =
        linkedFastMapOf<String, String>().also { rev ->
            this.forEach { (k, v) ->
                v.forEach { if (!rev.containsKey(it)) rev[it] = k }
            }
        }
}