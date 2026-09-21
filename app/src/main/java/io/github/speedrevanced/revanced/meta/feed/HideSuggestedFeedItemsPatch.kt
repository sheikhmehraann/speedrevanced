package io.github.speedrevanced.revanced.meta.feed

import de.robv.android.xposed.XC_MethodHook
import io.github.speedrevanced.patch
import java.lang.reflect.Field

val HideSuggestedFeedItems = patch(
    name = "Hide suggested feed items",
    description = "Removes suggested posts, clips netego, suggested users, and cross-promo units from the feed."
) {
    runCatching {
        ::feedItemParserFingerprint.hookMethod(object : XC_MethodHook() {
            private var mediaField: Field? = null
            private var fieldResolved = false

            override fun afterHookedMethod(param: MethodHookParam) {
                val result = param.result ?: return
                try {
                    if (!fieldResolved) {
                        for (f in result.javaClass.declaredFields) {
                            if (f.type.simpleName == "Media") {
                                f.isAccessible = true
                                mediaField = f
                                break
                            }
                        }
                        fieldResolved = true
                    }
                    val mf = mediaField
                    if (mf != null && mf.get(result) == null) {
                        param.result = null
                    }
                } catch (_: Throwable) {}
            }
        })
    }
}
