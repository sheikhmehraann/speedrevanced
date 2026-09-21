package io.github.speedrevanced.morphe.youtube.layout.shortsnoresume

import app.morphe.extension.youtube.patches.DisableShortsResumingOnStartupPatch
import io.github.speedrevanced.morphe.shared.misc.settings.preference.SwitchPreference
import io.github.speedrevanced.morphe.youtube.insertLiteralOverride
import io.github.speedrevanced.morphe.youtube.misc.playservice.is_21_03_or_greater
import io.github.speedrevanced.morphe.youtube.misc.settings.PreferenceScreen
import io.github.speedrevanced.patch
import io.github.speedrevanced.scopedHook

val DisableShortsResumingOnStartup = patch(
    name = "Disable Shorts resuming on startup",
    description = "Adds an option to disable Shorts from resuming on app startup when Shorts were last being watched.",
) {
    PreferenceScreen.SHORTS.addPreferences(
        SwitchPreference("morphe_disable_shorts_resuming_on_startup"),
    )

    if (is_21_03_or_greater) {
        UserWasInShortsEvaluateFingerprint.hookMethod(
            scopedHook(
                UserWasInShortsEvaluateAnchorFingerprint.method
            ) {
                after {
                    it.result =
                        DisableShortsResumingOnStartupPatch.disableShortsResumingOnStartup(it.result as Boolean)
                }
            })
    } else {
        // TODO
    }

    insertLiteralOverride(
        45358360L,
        DisableShortsResumingOnStartupPatch::disableShortsResumingOnStartup
    )
}
