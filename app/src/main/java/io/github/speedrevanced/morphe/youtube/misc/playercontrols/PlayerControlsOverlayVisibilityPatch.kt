package io.github.speedrevanced.morphe.youtube.misc.playercontrols

import app.morphe.extension.youtube.patches.PlayerControlsVisibilityHookPatch
import io.github.speedrevanced.patch

val PlayerControlsOverlayVisibility = patch {
    PlayerControlsVisibilityEntityModelInit.hookMethod {
        val getPlayerControlsVisibilityMethod =
            PlayerControlsVisibilityEntityModelFingerprint.method
        after {
            PlayerControlsVisibilityHookPatch.setPlayerControlsVisibility(
                getPlayerControlsVisibilityMethod(it.thisObject) as Enum<*>?
            )
        }
    }
}