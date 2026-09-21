package io.github.speedrevanced.revanced.strava

import io.github.speedrevanced.revanced.strava.subscription.UnlockSubscription
import io.github.speedrevanced.revanced.strava.upselling.DisableSubscriptionSuggestions

val StravaPatches = arrayOf(
    UnlockSubscription,
    DisableSubscriptionSuggestions
)