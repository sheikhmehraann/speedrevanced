package io.github.speedrevanced.revanced.meta.ads

import de.robv.android.xposed.XC_MethodReplacement
import io.github.speedrevanced.patch

val HideAds = patch(
    name = "Hide ads",
) {
    ::adInjectorFingerprint.hookMethod(XC_MethodReplacement.returnConstant(false))
}