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
package com.shotadft.kanaconverter.util

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
internal object MapperUtil {
    /**
     * Builds a new [Object2ObjectLinkedOpenHashMap] by populating a mutable map
     * using the given [builderAction] and returning a new map with the same key-value pairs.
     *
     * The map passed as a receiver to the [builderAction] is valid only inside that function.
     * Using it outside of the function produces an unspecified behavior.
     *
     * Entries of the map are iterated in the order they were added by the [builderAction].
     *
     * @author Shotadft
     * @since 1.1
     */
    @Suppress("LEAKED_IN_PLACE_LAMBDA", "WRONG_INVOCATION_KIND")
    @OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)
    inline fun <K, V> buildLinkedFastMap(
        @BuilderInference builderAction: Object2ObjectLinkedOpenHashMap<K, V>.() -> Unit
    ): Object2ObjectLinkedOpenHashMap<K, V> {
        contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
        mutableSetOf("")
        return Object2ObjectLinkedOpenHashMap<K, V>().apply(builderAction)
    }

    /**
     * Returns an empty new [ObjectOpenHashSet].
     * The returned set does not preserve the element iteration order.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectOpenSetOf(): ObjectOpenHashSet<T> = ObjectOpenHashSet<T>()

    /**
     * Returns a new [ObjectOpenHashSet] with the given elements.
     * Elements of the set are iterated in no particular order.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectOpenSetOf(vararg elements: T): ObjectOpenHashSet<T> =
        ObjectOpenHashSet<T>(elements.size).apply { addAll(elements) }

    /**
     * Returns an empty new [ObjectLinkedOpenHashSet].
     * The returned set preserves the element iteration order.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectLinkedOpenSetOf(): ObjectLinkedOpenHashSet<T> = ObjectLinkedOpenHashSet<T>()

    /**
     * Returns a new [ObjectLinkedOpenHashSet] with the given elements.
     * Elements of the set are iterated in the order they were specified.
     *
     * @author Shotadft
     * @since 1.1
     */
    fun <T> objectLinkedOpenSetOf(vararg elements: T): ObjectLinkedOpenHashSet<T> =
        ObjectLinkedOpenHashSet<T>(elements.size).apply { addAll(elements) }
}