package io.github.speedrevanced.revanced.meta

import io.github.speedrevanced.revanced.meta.ads.HideAds
import io.github.speedrevanced.revanced.meta.developer.DeveloperOptions
import io.github.speedrevanced.revanced.meta.feed.HideSuggestedFeedItems
import io.github.speedrevanced.revanced.meta.ghost.GhostDM
import io.github.speedrevanced.revanced.meta.ghost.GhostStorySeen
import io.github.speedrevanced.revanced.meta.privacy.SanitizeInstagramLinks
import io.github.speedrevanced.revanced.meta.privacy.ScreenshotBypass
import io.github.speedrevanced.revanced.meta.ui.RemoveMetaAI

val MetaPatches = arrayOf(
    HideAds,
    HideSuggestedFeedItems,
    RemoveMetaAI,
    GhostStorySeen,
    GhostDM,
    ScreenshotBypass,
    DeveloperOptions,
    SanitizeInstagramLinks
)