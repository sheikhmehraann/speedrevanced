package io.github.speedrevanced.revanced.telegram.antidelete

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import io.github.speedrevanced.patch

val AntiDeleteMessages = patch(
    name = "Anti-delete messages",
    description = "Preserves deleted messages in local chat history when revoked by other users."
) {
    runCatching {
        val messagesStorageClass = runCatching {
            classLoader.loadClass("org.telegram.messenger.MessagesStorage")
        }.getOrNull()

        if (messagesStorageClass != null) {
            for (method in messagesStorageClass.declaredMethods) {
                if (method.name == "markMessagesAsDeleted") {
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
