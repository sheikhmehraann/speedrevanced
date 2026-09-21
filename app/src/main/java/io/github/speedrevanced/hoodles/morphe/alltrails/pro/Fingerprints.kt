package io.github.speedrevanced.hoodles.morphe.alltrails.pro

import io.github.speedrevanced.morphe.Fingerprint

object IsProFingerprint : Fingerprint(
    name = "isPro",
    returnType = "Z",
)

object GetSubscriptionTierFingerprint : Fingerprint(
    name = "getSubscriptionTier",
    returnType = "Ljava/lang/String;",
)
