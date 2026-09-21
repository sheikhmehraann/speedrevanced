package io.github.speedrevanced.revanced.meta.feed

import de.robv.android.xposed.XC_MethodHook
import io.github.speedrevanced.patch
import java.lang.reflect.Field

val HideSuggestedFeedItems = patch(
    name = "Hide suggested feed items",
    description = "Removes suggested posts, clips netego, suggested users, and cross-promo units from the feed.",
    use = false
) {
    runCatching {
        ::feedItemParserFingerprint.hookMethod(object : XC_MethodHook() {
            private var mediaField: Field? = null
            private var fieldResolved = false

            override fun afterHookedMethod(param: MethodHookParam) {
                val result = param.result ?: return
                try {
                    var hasMedia = false
                    var isThreadsUnit = false
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
                        } else if (typeName.contains("Threads") || typeName.startsWith("TextApp") || typeName.startsWith("XDTTextApp")) {
                            try {
                                f.isAccessible = true
                                if (f.get(result) != null) {
                                    isThreadsUnit = true
                                }
                            } catch (_: Throwable) {}
                        }
                    }

                    if (hasMedia) return // real post — never hide

                    // Suggested unit / netego / threads
                    param.result = null
                } catch (_: Throwable) {}
            }
        })
    }
}
