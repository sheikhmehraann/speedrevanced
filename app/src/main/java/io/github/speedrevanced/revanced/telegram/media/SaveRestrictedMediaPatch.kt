package io.github.speedrevanced.revanced.telegram.media

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val SaveRestrictedMedia = patch(
    name = "Save restricted media",
    description = "Enables saving and copying protected media from restricted channels and secret chats."
) {
    runCatching {
        val chatObjectClass = runCatching {
            classLoader.loadClass("org.telegram.messenger.ChatObject")
        }.getOrNull()

        if (chatObjectClass != null) {
            for (method in chatObjectClass.declaredMethods) {
                if (method.name == "isNotInChat" || method.name == "hasAdminRights") continue
                if (method.name.contains("NoForwards", ignoreCase = true) ||
                    method.name.contains("canSendStickers", ignoreCase = false)) {
                    if (method.returnType == java.lang.Boolean.TYPE) {
                        XposedBridge.hookMethod(method, object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam) {
                                if (method.name.contains("NoForwards", ignoreCase = true)) {
                                    param.result = false
                                }
                            }
                        })
                    }
                }
            }
        }

        val messageObjectClass = runCatching {
            classLoader.loadClass("org.telegram.messenger.MessageObject")
        }.getOrNull()

        if (messageObjectClass != null) {
            for (method in messageObjectClass.declaredMethods) {
                if (method.name == "canForward" || method.name == "canShare" || method.name == "canSaveToGallery") {
                    if (method.returnType == java.lang.Boolean.TYPE) {
                        XposedBridge.hookMethod(method, object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam) {
                                param.result = true
                            }
                        })
                    }
                }
            }
        }
    }
}
