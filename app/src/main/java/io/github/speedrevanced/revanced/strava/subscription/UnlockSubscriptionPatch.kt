package io.github.speedrevanced.revanced.strava.subscription

import io.github.speedrevanced.patch

val UnlockSubscription = patch(
    name = "Unlock subscription features",
    description = "Unlocks \"Routes\", \"Matched Runs\" and \"Segment Efforts\".",
) {
    ::getSubscribedFingerprint.hookMethod {
        before { param ->
            param.result = true
        }
    }
}