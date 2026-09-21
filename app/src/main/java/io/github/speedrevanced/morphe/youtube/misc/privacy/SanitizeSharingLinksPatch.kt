package io.github.speedrevanced.morphe.youtube.misc.privacy

import io.github.speedrevanced.morphe.shared.misc.privacy.SanitizeSharingLinks
import io.github.speedrevanced.morphe.youtube.misc.settings.PreferenceScreen
import io.github.speedrevanced.patch

val SanitizeSharingLinks = patch(
    name = "Sanitize sharing links",
    description = "Removes the tracking query parameters from shared links."
) {
    SanitizeSharingLinks(
        preferenceScreen = PreferenceScreen.MISC,
        replaceLinksWithShortener = true
    )
}