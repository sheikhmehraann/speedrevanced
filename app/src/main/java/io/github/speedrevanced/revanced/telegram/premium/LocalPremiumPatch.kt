package io.github.speedrevanced.revanced.telegram.premium

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import io.github.speedrevanced.patch

val LocalPremium = patch(
    name = "Local premium features",
    description = "Enables client-side features like premium app icons, double limits, and premium emoji rendering."
) {
    runCatching {
        val userObjectClass = runCatching {
            classLoader.loadClass("org.telegram.messenger.UserObject")
        }.getOrNull()

        if (userObjectClass != null) {
            val userConfigClass = runCatching { classLoader.loadClass("org.telegram.messenger.UserConfig") }.getOrNull()
            for (method in userObjectClass.declaredMethods) {
                if (method.name == "isUserSelf") continue
                if (method.name == "isPremiumUser" || method.name == "hasPremium") {
                    if (method.returnType == java.lang.Boolean.TYPE) {
                        XposedBridge.hookMethod(method, object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam) {
                                val arg = param.args.firstOrNull() ?: return
                                // Only grant premium if it's the self user
                                val isSelf = runCatching {
                                    val isSelfMethod = userObjectClass.getDeclaredMethod("isUserSelf", arg.javaClass)
                                    isSelfMethod.isAccessible = true
                                    isSelfMethod.invoke(null, arg) as? Boolean
                                }.getOrNull() ?: false

                                if (isSelf) {
                                    param.result = true
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}
