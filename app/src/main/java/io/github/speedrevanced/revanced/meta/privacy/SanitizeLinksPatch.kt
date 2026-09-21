package io.github.speedrevanced.revanced.meta.privacy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch

val SanitizeInstagramLinks = patch(
    name = "Sanitize sharing links",
    description = "Removes tracking query parameters (igsh, utm_source, etc.) from shared links."
) {
    // Hook Intent.createChooser and Intent.putExtra for ACTION_SEND
    try {
        XposedHelpers.findAndHookMethod(
            Intent::class.java,
            "putExtra",
            String::class.java,
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val key = param.args[0] as? String ?: return
                    val value = param.args[1] as? String ?: return
                    if (key == Intent.EXTRA_TEXT && value.contains("instagram.com")) {
                        param.args[1] = cleanInstagramUrl(value)
                    }
                }
            }
        )

        // Also hook ClipboardManager to sanitize copied links
        XposedHelpers.findAndHookMethod(
            ClipboardManager::class.java,
            "setPrimaryClip",
            ClipData::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val clipData = param.args[0] as? ClipData ?: return
                    if (clipData.itemCount > 0) {
                        val text = clipData.getItemAt(0).text?.toString() ?: return
                        if (text.contains("instagram.com")) {
                            val cleaned = cleanInstagramUrl(text)
                            param.args[0] = ClipData.newPlainText("text", cleaned)
                        }
                    }
                }
            }
        )
    } catch (e: Throwable) {
        XposedBridge.log(e)
    }
}

private fun cleanInstagramUrl(text: String): String {
    return try {
        val regex = Regex("""https?://(?:www\.)?instagram\.com/[^\s]+""")
        regex.replace(text) { matchResult ->
            val uri = Uri.parse(matchResult.value)
            val builder = uri.buildUpon().clearQuery()
            for (param in uri.queryParameterNames) {
                if (param != "igsh" && !param.startsWith("utm_")) {
                    for (v in uri.getQueryParameters(param)) {
                        builder.appendQueryParameter(param, v)
                    }
                }
            }
            builder.build().toString()
        }
    } catch (_: Throwable) {
        text
    }
}
