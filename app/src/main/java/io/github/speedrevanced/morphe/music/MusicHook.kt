package io.github.speedrevanced.morphe.music

import io.github.speedrevanced.ExtensionResourceHook
import io.github.speedrevanced.morphe.music.ad.HideAds
import io.github.speedrevanced.morphe.music.audio.exclusiveaudio.EnableExclusiveAudioPlayback
import io.github.speedrevanced.morphe.music.layout.upgradebutton.HideUpgradeButton
import io.github.speedrevanced.morphe.music.misc.backgroundplayback.BackgroundPlayback
import io.github.speedrevanced.morphe.music.misc.debugging.EnableDebugging
import io.github.speedrevanced.morphe.music.misc.privacy.SanitizeSharingLinks
import io.github.speedrevanced.morphe.music.misc.settings.SettingsHook
import io.github.speedrevanced.morphe.shared.misc.CheckRecycleBitmapMediaSession

val YTMusicPatches = arrayOf(
    ExtensionResourceHook,
    BackgroundPlayback,
    HideUpgradeButton,
    HideAds,
    EnableExclusiveAudioPlayback,
    CheckRecycleBitmapMediaSession,
    EnableDebugging,
    SanitizeSharingLinks,
    SettingsHook
)