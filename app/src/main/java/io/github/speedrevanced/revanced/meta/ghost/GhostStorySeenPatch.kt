package io.github.speedrevanced.revanced.meta.ghost

import de.robv.android.xposed.XC_MethodHook
import io.github.speedrevanced.patch

val GhostStorySeen = patch(
    name = "Ghost mode stories",
    description = "Prevents sending seen receipts when viewing stories."
) {
    runCatching {
        ::storySeenFingerprint.hookMethod(object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                param.result = null
            }
        })
    }
}


