package com.shotadft.kanaconverter.util

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

typealias StrSet = ObjectOpenHashSet<String>
typealias LinkedFastStrMap = Object2ObjectLinkedOpenHashMap<String, StrSet>

internal object MapperUtil {
    @Suppress("LEAKED_IN_PLACE_LAMBDA", "WRONG_INVOCATION_KIND")
    @OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)
    inline fun buildLinkedFastStrMap(
        @BuilderInference builderAction: LinkedFastStrMap.() -> Unit
    ): LinkedFastStrMap {
        contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
        return LinkedFastStrMap().apply(builderAction)
    }
}