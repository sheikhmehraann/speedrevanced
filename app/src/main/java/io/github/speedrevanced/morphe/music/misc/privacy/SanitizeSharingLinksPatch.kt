package io.github.speedrevanced.morphe.music.misc.privacy

import io.github.speedrevanced.patch
import io.github.speedrevanced.morphe.music.misc.settings.PreferenceScreen
import io.github.speedrevanced.morphe.shared.misc.privacy.SanitizeSharingLinks

val SanitizeSharingLinks = patch(
    name = "Sanitize sharing links",
    description = "Removes the tracking query parameters from shared links."
) {
    SanitizeSharingLinks(
        preferenceScreen = PreferenceScreen.MISC,
        replaceMusicLinksWithYouTube = true
    )
}