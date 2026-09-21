package io.github.speedrevanced.morphe.music.audio.exclusiveaudio

import de.robv.android.xposed.XC_MethodReplacement
import io.github.speedrevanced.morphe.music.misc.playservice.is_9_32_or_greater
import io.github.speedrevanced.morphe.music.misc.playservice.versionCheckPatch
import io.github.speedrevanced.patch

val EnableExclusiveAudioPlayback = patch(
    name = "Enable exclusive audio playback",
    description = "Enables the option to play audio without video.",
) {
    dependsOn(
        versionCheckPatch
    )
    val fingerprint = if (is_9_32_or_greater) {
        AllowExclusiveAudioPlaybackFingerprint
    } else {
        AllowExclusiveAudioPlaybackLegacyFingerprint
    }

    fingerprint.hookMethod(XC_MethodReplacement.returnConstant(true))
}