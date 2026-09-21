package io.github.speedrevanced.revanced.spotify.player

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val DisableForcedShuffle = patch(
    name = "Disable Forced Shuffle",
    description = "Disables forced shuffle mode to allow on-demand track selection"
) {
    runCatching {
        val playerRestrictionsClazz = runCatching {
            classLoader.loadClass("com.spotify.player.model.PlayerRestrictions")
        }.getOrNull()

        if (playerRestrictionsClazz != null) {
            // disallowTogglingShuffleReasons -> return empty set
            runCatching {
                XposedHelpers.findAndHookMethod(
                    playerRestrictionsClazz,
                    "disallowTogglingShuffleReasons",
                    XC_MethodReplacement.returnConstant(emptySet<Any>())
                )
            }
            // disallowTogglingRepeatContextReasons -> return empty set
            runCatching {
                XposedHelpers.findAndHookMethod(
                    playerRestrictionsClazz,
                    "disallowTogglingRepeatContextReasons",
                    XC_MethodReplacement.returnConstant(emptySet<Any>())
                )
            }
            // disallowTogglingRepeatTrackReasons -> return empty set
            runCatching {
                XposedHelpers.findAndHookMethod(
                    playerRestrictionsClazz,
                    "disallowTogglingRepeatTrackReasons",
                    XC_MethodReplacement.returnConstant(emptySet<Any>())
                )
            }
        }
    }
}
