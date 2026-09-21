package io.github.speedrevanced

import io.github.speedrevanced.morphe.music.YTMusicPatches
import io.github.speedrevanced.morphe.youtube.YouTubePatches
import io.github.speedrevanced.revanced.meta.MetaPatches
import io.github.speedrevanced.revanced.photos.GooglePhotosPatches
import io.github.speedrevanced.revanced.spotify.SpotifyPatches
import io.github.speedrevanced.revanced.telegram.TelegramPatches

class AppPatchInfo(val appName: String, val packageName: String, val patches: Array<Patch>)

val appPatchConfigurations = listOf(
    AppPatchInfo("YouTube", "com.google.android.youtube", YouTubePatches),
    AppPatchInfo("YT Music", "com.google.android.apps.youtube.music", YTMusicPatches),
    AppPatchInfo("Instagram", "com.instagram.android", MetaPatches),
    AppPatchInfo("Threads", "com.instagram.barcelona", MetaPatches),
    AppPatchInfo("Telegram", "org.telegram.messenger", TelegramPatches),
    AppPatchInfo("Telegram Web", "org.telegram.messenger.web", TelegramPatches),
    AppPatchInfo("Plus Messenger", "org.telegram.plus", TelegramPatches),
    AppPatchInfo("Nekogram", "tw.nekomimi.nekogram", TelegramPatches),
    AppPatchInfo("NekoX", "nekox.messenger", TelegramPatches),
    AppPatchInfo("Spotify", "com.spotify.music", SpotifyPatches),
    AppPatchInfo("Google Photos", "com.google.android.apps.photos", GooglePhotosPatches),
)

val patchesByPackage = appPatchConfigurations.associate { it.packageName to it.patches }
