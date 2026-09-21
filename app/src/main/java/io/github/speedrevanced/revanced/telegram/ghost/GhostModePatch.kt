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
            if (name == "markDialogAsRead" || name.contains("sendMessagesRead", ignoreCase = true) ||
                name.contains("sendTyping", ignoreCase = true)) {
                XposedBridge.hookMethod(method, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.result = null
                    }
                })
            }
        }
    }
}
