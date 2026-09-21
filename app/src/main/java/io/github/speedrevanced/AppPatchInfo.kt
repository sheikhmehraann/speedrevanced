package io.github.speedrevanced

import io.github.speedrevanced.hoodles.morphe.alltrails.AllTrailsPatches
import io.github.speedrevanced.morphe.music.YTMusicPatches
import io.github.speedrevanced.morphe.reddit.RedditPatches
import io.github.speedrevanced.morphe.youtube.YouTubePatches
import io.github.speedrevanced.revanced.googlephotos.GooglePhotosPatches
import io.github.speedrevanced.revanced.meta.MetaPatches
import io.github.speedrevanced.revanced.photomath.PhotomathPatches
import io.github.speedrevanced.revanced.strava.StravaPatches

class AppPatchInfo(val appName: String, val packageName: String, val patches: Array<Patch>)

val appPatchConfigurations = listOf(
    AppPatchInfo("YouTube", "com.google.android.youtube", YouTubePatches),
    AppPatchInfo("YT Music", "com.google.android.apps.youtube.music", YTMusicPatches),
    AppPatchInfo("Reddit", "com.reddit.frontpage", RedditPatches),
    AppPatchInfo("Google Photos", "com.google.android.apps.photos", GooglePhotosPatches),
    AppPatchInfo("Photomath", "com.microblink.photomath", PhotomathPatches),
    AppPatchInfo("Instagram", "com.instagram.android", MetaPatches),
    AppPatchInfo("Threads", "com.instagram.barcelona", MetaPatches),
    AppPatchInfo("Strava", "com.strava", StravaPatches),
    AppPatchInfo("AllTrails", "com.alltrails.alltrails", AllTrailsPatches),
)

val patchesByPackage = appPatchConfigurations.associate { it.packageName to it.patches }
