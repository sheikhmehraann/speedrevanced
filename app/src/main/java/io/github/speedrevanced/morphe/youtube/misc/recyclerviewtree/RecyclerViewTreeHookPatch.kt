package io.github.speedrevanced.morphe.youtube.misc.recyclerviewtree

import android.support.v7.widget.RecyclerView
import io.github.speedrevanced.patch
import io.github.speedrevanced.scopedHook

val addRecyclerViewTreeHook = mutableListOf<(RecyclerView) -> Unit>()

val recyclerViewTreeHook = patch {
    ::recyclerViewTreeObserverFingerprint.hookMethod(scopedHook(::RecyclerView_addOnScrollListener.member) {
        before {
            val recyclerView = it.thisObject as RecyclerView
            addRecyclerViewTreeHook.forEach { hook ->
                hook(recyclerView)
            }
        }
    })
}