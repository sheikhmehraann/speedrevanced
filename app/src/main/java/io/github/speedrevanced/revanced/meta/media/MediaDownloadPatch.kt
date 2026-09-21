package io.github.speedrevanced.revanced.meta.media

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val MediaDownload = patch(
    name = "Media Download Hooks",
    description = "Enables downloading of Instagram stories, reels, posts, and voice notes directly to storage"
) {
    // Media downloading utility hook for Instagram Feed & Reel media models
    runCatching {
        // Hook media long-click or context menu actions to expose download option
        val mediaClazz = runCatching {
            classLoader.loadClass("com.instagram.feed.media.Media")
        }.getOrNull()

        if (mediaClazz != null) {
            // Ensure media download capabilities are not restricted by account/author flags
            val methods = mediaClazz.declaredMethods
            for (m in methods) {
                if (m.name.contains("isDownloadAllowed", ignoreCase = true) ||
                    m.name.contains("canDownload", ignoreCase = true)
                ) {
                    runCatching {
                        XposedHelpers.findAndHookMethod(
                            mediaClazz,
                            m.name,
                            *m.parameterTypes,
                            object : XC_MethodHook() {
                                override fun beforeHookedMethod(param: MethodHookParam) {
                                    param.result = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
