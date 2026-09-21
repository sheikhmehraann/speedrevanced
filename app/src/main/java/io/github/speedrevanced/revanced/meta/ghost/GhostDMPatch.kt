package io.github.speedrevanced.revanced.meta.ghost

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import io.github.speedrevanced.patch

val GhostDM = patch(
    name = "Ghost mode direct messages",
    description = "Prevents sending read receipts and typing status when viewing or replying to Instagram direct messages."
) {
    runCatching {
        val classNames = listOf(
            "com.instagram.direct.send.mutation.DirectSendSeenMutation",
            "com.instagram.direct.model.protobufmodel.IgThreadSeenMarkerMessage",
            "com.instagram.direct.model.protobufmodel.IgThreadDisappearingModeSeenMarkerMessage",
            "com.instagram.direct.model.protobufmodel.IgDThreadShhModeSeenMarkerMessage"
        )
        for (className in classNames) {
            val clazz = runCatching { classLoader.loadClass(className) }.getOrNull() ?: continue
            for (method in clazz.declaredMethods) {
                if (method.name.contains("send", ignoreCase = true) ||
                    method.name.contains("mark", ignoreCase = true) ||
                    method.name.contains("seen", ignoreCase = true)) {
                    runCatching {
                        XposedBridge.hookMethod(method, object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam) {
                                param.result = null
                            }
                        })
                    }
                }
            }
        }
    }
}
