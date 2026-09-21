package io.github.speedrevanced.morphe.reddit.misc.privacy

import app.morphe.extension.reddit.patches.SanitizeSharingLinksPatch
import io.github.speedrevanced.patch

val SanitizeSharingLinks = patch(
    name = "Sanitize sharing links",
    description = "Adds an option to sanitize sharing links by removing tracking query parameters."
) {
    ShareLinkFormatterFingerprint.hookMethod {
        before {
            if (SanitizeSharingLinksPatch.stripQueryParameters()) {
                it.result = it.args[0]
            }
        }
    }
}