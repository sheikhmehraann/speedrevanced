package io.github.speedrevanced.revanced.telegram.ghost

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import io.github.speedrevanced.patch

val GhostMode = patch(
    name = "Ghost mode",
    description = "Hides read receipts and typing status when viewing chats and messages."
) {
    runCatching {
        val messagesControllerClass = runCatching {
            classLoader.loadClass("org.telegram.messenger.MessagesController")
        }.getOrNull() ?: return@runCatching

        for (method in messagesControllerClass.declaredMethods) {
            val name = method.name
            // Only hook outgoing network read receipt senders, do NOT break local markDialogAsRead
            if (name.contains("sendMessagesRead", ignoreCase = true) ||
                name.contains("sendTyping", ignoreCase = true)) {
                XposedBridge.hookMethod(method, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val rt = method.returnType
                        when {
                            rt == java.lang.Boolean.TYPE || rt == java.lang.Boolean::class.java -> param.result = false
                            rt == java.lang.Integer.TYPE || rt == java.lang.Integer::class.java -> param.result = 0
                            rt == java.lang.Long.TYPE || rt == java.lang.Long::class.java -> param.result = 0L
                            rt == java.lang.Void.TYPE -> param.result = null
                            else -> {
                                // Do not alter result for unknown complex return types to avoid crash
                            }
                        }
                    }
                })
            }
        }
    }
}
