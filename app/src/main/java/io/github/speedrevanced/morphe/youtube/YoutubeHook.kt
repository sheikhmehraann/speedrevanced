package io.github.speedrevanced.morphe.youtube

import android.app.Activity
import app.morphe.extension.shared.Utils
import io.github.libxposed.api.XposedInterface
import io.github.speedrevanced.ExtensionResourceHook
import io.github.speedrevanced.addModuleAssets
import io.github.speedrevanced.injectHostClassLoaderToSelf
import io.github.speedrevanced.injectSelfClassLoaderToHost
import io.github.speedrevanced.morphe.shared.misc.CheckRecycleBitmapMediaSession
import io.github.speedrevanced.morphe.shared.misc.debugging.experimentalBooleanFeatureFlagFingerprint
import io.github.speedrevanced.morphe.youtube.ad.HideAds
import io.github.speedrevanced.morphe.youtube.interaction.copyvideolink.CopyVideoLinkButtonPatch
import io.github.speedrevanced.morphe.youtube.interaction.downloads.Downloads
import io.github.speedrevanced.morphe.youtube.interaction.swipecontrols.SwipeControls
import io.github.speedrevanced.morphe.youtube.layout.buttons.action.HideVideoActionButtons
import io.github.speedrevanced.morphe.youtube.layout.buttons.navigation.NavigationBar
import io.github.speedrevanced.morphe.youtube.layout.captions.AutoCaptions
import io.github.speedrevanced.morphe.youtube.layout.hide.general.HideLayoutComponents
import io.github.speedrevanced.morphe.youtube.layout.hide.shorts.HideShortsComponents
import io.github.speedrevanced.morphe.youtube.layout.shortsnoresume.DisableShortsResumingOnStartup
import io.github.speedrevanced.morphe.youtube.layout.sponsorblock.SponsorBlock
import io.github.speedrevanced.morphe.youtube.layout.thumbnails.AlternativeThumbnailsPatch
import io.github.speedrevanced.morphe.youtube.layout.thumbnails.BypassImageRegionRestrictionsPatch
import io.github.speedrevanced.morphe.youtube.misc.backgroundplayback.BackgroundPlayback
import io.github.speedrevanced.morphe.youtube.misc.debugging.EnableDebugging
import io.github.speedrevanced.morphe.youtube.misc.privacy.SanitizeSharingLinks
import io.github.speedrevanced.morphe.youtube.misc.settings.SettingsHook
import io.github.speedrevanced.morphe.youtube.shared.YOUTUBE_MAIN_ACTIVITY_CLASS_TYPE
import io.github.speedrevanced.morphe.youtube.video.audio.ForceOriginalAudio
import io.github.speedrevanced.morphe.youtube.video.codecs.DisableVideoCodecs
import io.github.speedrevanced.morphe.youtube.video.quality.VideoQuality
import io.github.speedrevanced.morphe.youtube.video.speed.PlaybackSpeed
import io.github.speedrevanced.patch
import io.github.speedrevanced.atLast
import org.luckypray.dexkit.wrap.DexMethod

val ExtensionHook = patch(name = "<ExtensionHook>") {
    injectHostClassLoaderToSelf(this::class.java.classLoader!!, classLoader)
    injectSelfClassLoaderToHost(this::class.java.classLoader!!, classLoader)
    DexMethod("$YOUTUBE_MAIN_ACTIVITY_CLASS_TYPE->onCreate(Landroid/os/Bundle;)V").hookMethod {
        before {
            val mainActivity = it.thisObject as Activity
            mainActivity.addModuleAssets()
            Utils.setContext(mainActivity)
        }
    }

    ExtensionResourceHook.run(this)
}

private val overrides = mutableMapOf<Long, (Boolean) -> Boolean>()

fun insertLiteralOverride(id: Long, override: Boolean = false) {
    overrides[id] = { override }
}

fun insertLiteralOverride(id: Long, override: (Boolean) -> Boolean) {
    overrides[id] = override
}

val FeatureOverride = patch {
    ::experimentalBooleanFeatureFlagFingerprint.hookMethod {
        priority = XposedInterface.PRIORITY_DEFAULT - 1
        after {
            val id = it.args.atLast(2) as Long
            val orig = it.result as Boolean
            val match = overrides[id] ?: return@after
            it.result = match(orig)
        }
    }
}

val YouTubePatches = arrayOf(
    ExtensionHook,
    BackgroundPlayback,
    SanitizeSharingLinks,
    HideAds,
    SponsorBlock,
    CopyVideoLinkButtonPatch,
    Downloads,
    HideShortsComponents,
    DisableShortsResumingOnStartup,
    NavigationBar,
    SwipeControls,
    VideoQuality,
    HideLayoutComponents,
    HideVideoActionButtons,
    PlaybackSpeed,
    AutoCaptions,
    EnableDebugging,
    ForceOriginalAudio,
    DisableVideoCodecs,
    AlternativeThumbnailsPatch,
    BypassImageRegionRestrictionsPatch,
    CheckRecycleBitmapMediaSession,
    // make sure settingsHook at end to build preferences
    SettingsHook,
    FeatureOverride
)