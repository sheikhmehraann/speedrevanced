package io.github.speedrevanced.revanced.meta.ui

import de.robv.android.xposed.XC_MethodHook
import io.github.speedrevanced.patch

val RemoveMetaAI = patch(
    name = "Remove Meta AI",
    description = "Removes Meta AI suggestions, search entries, and options."
) {
    ::metaAIOptionFingerprint.hookMethod(object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) {
            val list = param.result as? MutableList<*> ?: return
            val iterator = list.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()?.toString()?.uppercase() ?: ""
                if (item.contains("GEN_AI") || item.contains("GENAI") || item.contains("META_AI") ||
                    item.contains("METAAI") || item.contains("ASK_META") || item.contains("CONTENT_DEEP_DIVE")) {
                    iterator.remove()
                }
            }
        }
    })
}
