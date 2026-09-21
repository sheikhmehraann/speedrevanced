package io.github.speedrevanced.revanced.meta.ghost

import de.robv.android.xposed.XC_MethodHook
import io.github.speedrevanced.patch

val GhostDM = patch(
    name = "Ghost mode direct messages",
    description = "Prevents sending read receipts and typing status when viewing or replying to Instagram direct messages."
) {
    runCatching {
        ::dmSeenFingerprint.hookMethod(object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                param.result = null
            }
        })
    }
    runCatching {
        ::dmTypingFingerprint.hookMethod(object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                param.result = null
            }
        })
    }
}
