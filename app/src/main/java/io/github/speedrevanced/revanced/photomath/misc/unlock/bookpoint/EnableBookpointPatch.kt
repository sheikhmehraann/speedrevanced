package io.github.speedrevanced.revanced.photomath.misc.unlock.bookpoint

import de.robv.android.xposed.XC_MethodReplacement
import io.github.speedrevanced.patch

val EnableBookpoint = patch(
    description = "Enables textbook access",
) {
    ::isBookpointEnabledFingerprint.hookMethod(XC_MethodReplacement.returnConstant(true))
}