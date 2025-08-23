package com.shotadft.kanaconverter.util

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

typealias StrSet = ObjectOpenHashSet<String>
typealias LinkedFastStrMap = Object2ObjectLinkedOpenHashMap<String, StrSet>

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
    inline fun <K, V> buildLinkedFastStrMap(
        @BuilderInference builderAction: Object2ObjectLinkedOpenHashMap<K, V>.() -> Unit
    ): Object2ObjectLinkedOpenHashMap<K, V> {
        contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
        return Object2ObjectLinkedOpenHashMap<K, V>().apply(builderAction)
    }
}