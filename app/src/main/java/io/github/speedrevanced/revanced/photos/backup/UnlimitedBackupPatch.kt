package io.github.speedrevanced.revanced.photos.backup

import android.os.Build
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val UnlimitedBackup = patch(
    name = "Unlimited Original Quality Backup",
    description = "Spoofs device to Pixel XL (marlin) for lifetime unlimited original quality Google Photos backup"
) {
    // 1. Spoof Build fields inside Google Photos
    runCatching {
        val buildClazz = Build::class.java
        XposedHelpers.setStaticObjectField(buildClazz, "MANUFACTURER", "Google")
        XposedHelpers.setStaticObjectField(buildClazz, "BRAND", "google")
        XposedHelpers.setStaticObjectField(buildClazz, "DEVICE", "marlin")
        XposedHelpers.setStaticObjectField(buildClazz, "PRODUCT", "marlin")
        XposedHelpers.setStaticObjectField(buildClazz, "MODEL", "Pixel XL")
        XposedHelpers.setStaticObjectField(
            buildClazz,
            "FINGERPRINT",
            "google/marlin/marlin:10/QP1A.191005.007.A3/5972272:user/release-keys"
        )
    }

    // 2. Hook hasSystemFeature on ApplicationPackageManager
    val pixelFeatures = setOf(
        "com.google.android.feature.PIXEL_EXPERIENCE",
        "com.google.android.apps.photos.PIXEL_2016_PRELOAD",
        "com.google.android.feature.PIXEL_2016_EXPERIENCE",
        "com.google.android.feature.PIXEL_2017_EXPERIENCE",
        "com.google.android.feature.PIXEL_2018_EXPERIENCE",
        "com.google.android.feature.PIXEL_2019_EXPERIENCE",
        "com.google.android.feature.PIXEL_2020_EXPERIENCE",
        "com.google.android.feature.PIXEL_2021_EXPERIENCE"
    )

    runCatching {
        val appPkgMgrClazz = classLoader.loadClass("android.app.ApplicationPackageManager")
        XposedHelpers.findAndHookMethod(
            appPkgMgrClazz,
            "hasSystemFeature",
            String::class.java,
            Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val feature = param.args[0] as? String ?: return
                    if (pixelFeatures.contains(feature)) {
                        param.result = true
                    }
                }
            }
        )

        XposedHelpers.findAndHookMethod(
            appPkgMgrClazz,
            "hasSystemFeature",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val feature = param.args[0] as? String ?: return
                    if (pixelFeatures.contains(feature)) {
                        param.result = true
                    }
                }
            }
        )
    }
}
