package io.github.speedrevanced.revanced.telegram.ui

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import io.github.speedrevanced.patch

val HideStories = patch(
    name = "Hide stories",
    description = "Removes the top stories bar from the chats list in Telegram and Nekogram."
) {
    runCatching {
        val messagesControllerClass = runCatching {
            classLoader.loadClass("org.telegram.messenger.MessagesController")
        }.getOrNull() ?: return@runCatching

        for (method in messagesControllerClass.declaredMethods) {
            if (method.name.contains("storiesEnabled", ignoreCase = true) ||
                method.name == "hasStories") {
                if (method.returnType == java.lang.Boolean.TYPE) {
                    XposedBridge.hookMethod(method, object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            param.result = false
                        }
                    })
                }
            }
        }
    }
}
