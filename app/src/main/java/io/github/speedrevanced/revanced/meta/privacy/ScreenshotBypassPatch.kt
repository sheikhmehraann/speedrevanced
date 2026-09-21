package io.github.speedrevanced.revanced.meta.privacy

import android.view.Window
import android.view.WindowManager
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val ScreenshotBypass = patch(
    name = "Bypass screenshot restriction",
    description = "Removes FLAG_SECURE and suppresses screenshot notifications in vanishing DMs and view-once media."
) {
    runCatching {
        // Clear FLAG_SECURE from Window
        XposedHelpers.findAndHookMethod(
            Window::class.java,
            "setFlags",
            java.lang.Integer.TYPE,
            java.lang.Integer.TYPE,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    var flags = param.args[0] as Int
                    val mask = param.args[1] as Int
                    if ((mask and WindowManager.LayoutParams.FLAG_SECURE) != 0) {
                        flags = flags and WindowManager.LayoutParams.FLAG_SECURE.inv()
                        param.args[0] = flags
                    }
                }
            }
        )
    }
}
