package io.github.speedrevanced.revanced.telegram.ads

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val HideSponsoredMessages = patch(
    name = "Hide sponsored messages",
    description = "Removes sponsored and promoted channel messages across Telegram and Nekogram."
) {
    runCatching {
        val messagesControllerClass = runCatching {
            classLoader.loadClass("org.telegram.messenger.MessagesController")
        }.getOrNull() ?: return@runCatching

        // Hook getSponsoredMessages or putSponsoredMessages
        for (method in messagesControllerClass.declaredMethods) {
            if (method.name.contains("SponsoredMessage", ignoreCase = true) ||
                method.name.contains("getSponsoredMessages", ignoreCase = true)) {
                XposedBridge.hookMethod(method, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (method.returnType == java.util.ArrayList::class.java ||
                            method.returnType == java.util.List::class.java) {
                            param.result = java.util.ArrayList<Any>()
                        }
                    }
                })
            }
        }
    }
}
