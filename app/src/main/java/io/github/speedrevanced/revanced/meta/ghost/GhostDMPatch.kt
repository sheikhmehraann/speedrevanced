package io.github.speedrevanced.revanced.meta.ghost

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val GhostDM = patch(
    name = "Ghost mode direct messages",
    description = "Prevents sending read receipts and typing status when viewing or replying to Instagram direct messages."
) {
    runCatching {
        // Intercept DirectSendSeenMutation and DirectMarkThreadSeen
        val directMutationClass = runCatching {
            classLoader.loadClass("com.instagram.direct.send.mutation.DirectSendSeenMutation")
        }.getOrNull()

        if (directMutationClass != null) {
            for (method in directMutationClass.declaredMethods) {
                XposedBridge.hookMethod(method, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.result = null
                    }
                })
            }
        }
    }
}
