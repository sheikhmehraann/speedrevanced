package io.github.speedrevanced.revanced.meta.developer

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val DeveloperOptions = patch(
    name = "Unlock developer options",
    description = "Enables Instagram internal developer options and MetaConfig settings."
) {
    runCatching {
        val userSessionClass = runCatching {
            classLoader.loadClass("com.instagram.service.session.UserSession")
        }.getOrNull()

        if (userSessionClass != null) {
            for (method in userSessionClass.declaredMethods) {
                if (method.name.contains("isEmployee", ignoreCase = true) ||
                    method.name.contains("isDeveloper", ignoreCase = true)) {
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
