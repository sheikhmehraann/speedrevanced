package io.github.speedrevanced.revanced.photos.editor

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val UnlockEditingTools = patch(
    name = "Unlock Premium Editing Tools",
    description = "Enables Google One / Pixel exclusive tools like Magic Eraser, Portrait Light, Sky, Color Pop, and HDR"
) {
    // Hook Google Photos feature check services & flags
    val editingFeatureStrings = setOf(
        "MAGIC_ERASER",
        "PORTRAIT_LIGHT",
        "COLOR_POP",
        "HDR",
        "SKY_PALETTE",
        "DYNAMIC_COLOR"
    )

    runCatching {
        val appPkgMgrClazz = classLoader.loadClass("android.app.ApplicationPackageManager")
        XposedHelpers.findAndHookMethod(
            appPkgMgrClazz,
            "hasSystemFeature",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val feature = param.args[0] as? String ?: return
                    if (feature.contains("PIXEL") || feature.contains("GOOGLE") || feature.contains("PHOTOS")) {
                        param.result = true
                    }
                }
            }
        )
    }
}
