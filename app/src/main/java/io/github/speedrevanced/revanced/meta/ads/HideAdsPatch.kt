package io.github.speedrevanced.revanced.meta.ads

import de.robv.android.xposed.XC_MethodHook
import io.github.speedrevanced.patch

val HideAds = patch(
    name = "Hide ads",
) {
    runCatching {
        ::adInjectorFingerprint.hookMethod(object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                param.result = false
            }
        })
    }
}