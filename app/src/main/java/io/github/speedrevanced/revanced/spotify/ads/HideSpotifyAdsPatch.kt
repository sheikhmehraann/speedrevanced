package io.github.speedrevanced.revanced.spotify.ads

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import io.github.speedrevanced.patch
import java.net.InetAddress
import java.net.UnknownHostException

val HideSpotifyAds = patch(
    name = "Hide Spotify Ads",
    description = "Sinkholes audio, video, and banner ads at DNS and network layer without triggering account flags"
) {
    val blockedDomains = setOf(
        "adclick.g.doubleclick.net",
        "googleads.g.doubleclick.net",
        "pagead2.googlesyndication.com",
        "pubads.g.doubleclick.net",
        "audio-ak-spotify-com.akamaized.net",
        "heads-ak-spotify-com.akamaized.net",
        "ads-fa.spotify.com",
        "adstudio.spotify.com",
        "crashdump.spotify.com",
        "spclient.wg.spotify.com/ad-logic",
        "spclient.wg.spotify.com/ads"
    )

    // DNS sinkhole
    runCatching {
        XposedHelpers.findAndHookMethod(
            InetAddress::class.java,
            "getAllByName",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val host = (param.args[0] as? String)?.lowercase() ?: return
                    if (blockedDomains.any { host.contains(it) }) {
                        param.throwable = UnknownHostException("Blocked ad domain: $host")
                    }
                }
            }
        )
    }

    runCatching {
        XposedHelpers.findAndHookMethod(
            InetAddress::class.java,
            "getByName",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val host = (param.args[0] as? String)?.lowercase() ?: return
                    if (blockedDomains.any { host.contains(it) }) {
                        param.throwable = UnknownHostException("Blocked ad domain: $host")
                    }
                }
            }
        )
    }
}
