package io.github.speedrevanced.revanced.spotify.player

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val UnlimitedSkips = patch(
    name = "Unlimited Skips",
    description = "Enables unlimited track skips and seeking on Spotify free tier"
) {
    // Hook player restrictions to allow unlimited next/prev skipping
    runCatching {
        val playerRestrictionsClazz = runCatching {
            classLoader.loadClass("com.spotify.player.model.PlayerRestrictions")
        }.getOrNull()

        if (playerRestrictionsClazz != null) {
            // disallowSkippingNextReasons -> return empty set
            runCatching {
                XposedHelpers.findAndHookMethod(
                    playerRestrictionsClazz,
                    "disallowSkippingNextReasons",
                    XC_MethodReplacement.returnConstant(emptySet<Any>())
                )
            }
            // disallowSkippingPrevReasons -> return empty set
            runCatching {
                XposedHelpers.findAndHookMethod(
                    playerRestrictionsClazz,
                    "disallowSkippingPrevReasons",
                    XC_MethodReplacement.returnConstant(emptySet<Any>())
                )
            }
            // disallowSeekingReasons -> return empty set
            runCatching {
                XposedHelpers.findAndHookMethod(
                    playerRestrictionsClazz,
                    "disallowSeekingReasons",
                    XC_MethodReplacement.returnConstant(emptySet<Any>())
                )
            }
        }
    }
}
