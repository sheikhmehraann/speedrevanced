package io.github.speedrevanced.revanced.meta.feed

import de.robv.android.xposed.XC_MethodHook
import io.github.speedrevanced.patch

val HideSuggestedFeedItems = patch(
    name = "Hide suggested feed items",
    description = "Removes suggested posts, clips netego, suggested users, and cross-promo units from the feed."
) {
    ::feedItemParserFingerprint.hookMethod(object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) {
            val result = param.result ?: return
            var hasMedia = false
            for (f in result.javaClass.declaredFields) {
                val typeName = f.type.simpleName
                if (typeName == "Media") {
                    try {
                        f.isAccessible = true
                        if (f.get(result) != null) {
                            hasMedia = true
                            break
                        }
                    } catch (_: Throwable) {}
                }
            }
            if (!hasMedia) {
                param.result = null
            }
        }
    })
}
