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
package com.shotadft.kanaconverter.map.util

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

internal typealias StrSet = ObjectOpenHashSet<String>
internal typealias LinkedFastStrMap = Object2ObjectLinkedOpenHashMap<String, StrSet>

/**
 * @author Shotadft
 * @since 1.1
 */
@Suppress("Unused")
internal object MapperUtil {
    /**
     * Builds a new [Object2ObjectLinkedOpenHashMap] by populating a mutable map
     * using the given [builderAction] and returning a new map with the same key-value pairs.
     *
     * The map passed as a receiver to the [builderAction] is valid only inside that function.
     * Using it outside the function produces an unspecified behavior.
     *
     * Entries of the map are iterated in the order they were added by the [builderAction].
     *
     * @author Shotadft
     * @since 1.1
     */
    @Suppress("LEAKED_IN_PLACE_LAMBDA", "WRONG_INVOCATION_KIND")
    @OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)
    inline fun <K, V> buildLinkedFastMap(
        builderAction: Object2ObjectLinkedOpenHashMap<K, V>.() -> Unit
    ): Object2ObjectLinkedOpenHashMap<K, V> {
        contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
        return linkedFastMapOf<K, V>().apply(builderAction)
    }

    /**
     * Returns an empty new [Object2ObjectLinkedOpenHashMap].
     *
     * The returned map preserves the entry iteration order.
     * @author Shotadft
     * @since 1.1
     */
    fun <K, V> linkedFastMapOf(): Object2ObjectLinkedOpenHashMap<K, V> = Object2ObjectLinkedOpenHashMap()

    /**
     * Returns a new [Object2ObjectLinkedOpenHashMap] with the specified contents, given as a list of pairs
     * where the first component is the key and the second is the value.
     *
     * If multiple pairs have the same key, the resulting map will contain the value from the last of those pairs.
     *
     * Entries of the map are iterated in the order they were specified.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <K, V> linkedFastMapOf(vararg pairs: Pair<K, V>): Object2ObjectLinkedOpenHashMap<K, V> =
        Object2ObjectLinkedOpenHashMap<K, V>(mapCapacity(pairs.size)).apply { putAll(pairs) }

    /**
     * Returns an empty new [ObjectOpenHashSet].
     * The returned set does not preserve the element iteration order.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectOpenSetOf(): ObjectOpenHashSet<T> = ObjectOpenHashSet()

    /**
     * Returns a new [ObjectOpenHashSet] with the given elements.
     * Elements of the set are iterated in no particular order.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectOpenSetOf(vararg elements: T): ObjectOpenHashSet<T> =
        ObjectOpenHashSet<T>(mapCapacity(elements.size)).apply { addAll(elements) }

    /**
     * Returns an empty new [ObjectLinkedOpenHashSet].
     * The returned set preserves the element iteration order.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectLinkedOpenSetOf(): ObjectLinkedOpenHashSet<T> = ObjectLinkedOpenHashSet()

    /**
     * Returns a new [ObjectLinkedOpenHashSet] with the given elements.
     * Elements of the set are iterated in the order they were specified.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectLinkedOpenSetOf(vararg elements: T): ObjectLinkedOpenHashSet<T> =
        ObjectLinkedOpenHashSet<T>(mapCapacity(elements.size)).apply { addAll(elements) }

    /**
     * Calculate the initial capacity of a map, based on Guava's
     * [com.google.common.collect.Maps.capacity](https://github.com/google/guava/blob/v28.2/guava/src/com/google/common/collect/Maps.java#L325)
     * approach.
     */
    @PublishedApi
    internal fun mapCapacity(expectedSize: Int): Int = when {
        expectedSize < 0 -> expectedSize
        expectedSize < 3 -> expectedSize + 1
        expectedSize < INT_MAX_POWER_OF_TWO -> ((expectedSize / 0.75F) + 1.0F).toInt()
        else -> Int.MAX_VALUE
    }

    private const val INT_MAX_POWER_OF_TWO: Int = 1 shl (Int.SIZE_BITS - 2)
}