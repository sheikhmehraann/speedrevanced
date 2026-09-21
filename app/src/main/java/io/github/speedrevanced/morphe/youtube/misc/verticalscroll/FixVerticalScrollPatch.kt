package io.github.speedrevanced.morphe.youtube.misc.verticalscroll

import de.robv.android.xposed.XC_MethodReplacement.returnConstant
import io.github.speedrevanced.morphe.youtube.insertLiteralOverride
import io.github.speedrevanced.morphe.youtube.misc.playservice.VersionCheck
import io.github.speedrevanced.morphe.youtube.misc.playservice.is_21_18_or_greater
import io.github.speedrevanced.patch

val FixVerticalScroll = patch(
    description = "Fixes issues with refreshing the feed when the first component is of type EmptyComponent."
) {
    dependsOn(VersionCheck)

    if (is_21_18_or_greater) {
        // Can cause issues with scrolling.
        insertLiteralOverride(45782902L)
    }

    ::canScrollVerticallyFingerprint.hookMethod(returnConstant(false))
}