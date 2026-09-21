package io.github.speedrevanced.revanced.telegram

import io.github.speedrevanced.patch
import io.github.speedrevanced.revanced.telegram.ads.HideSponsoredMessages
import io.github.speedrevanced.revanced.telegram.antidelete.AntiDeleteMessages
import io.github.speedrevanced.revanced.telegram.ghost.GhostMode
import io.github.speedrevanced.revanced.telegram.media.SaveRestrictedMedia
import io.github.speedrevanced.revanced.telegram.premium.LocalPremium
import io.github.speedrevanced.revanced.telegram.ui.HideStories

val TelegramPatches = arrayOf(
    HideSponsoredMessages,
    SaveRestrictedMedia,
    GhostMode,
    AntiDeleteMessages,
    LocalPremium,
    HideStories
)
