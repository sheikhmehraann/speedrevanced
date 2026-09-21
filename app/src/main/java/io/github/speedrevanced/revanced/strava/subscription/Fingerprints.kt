package io.github.speedrevanced.revanced.strava.subscription

import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.fingerprint
import org.luckypray.dexkit.query.enums.StringMatchType

val getSubscribedFingerprint = fingerprint {
    opcodes(Opcode.IGET_BOOLEAN)
    classMatcher { className(".SubscriptionDetailResponse", StringMatchType.EndsWith) }
    methodMatcher { name = "getSubscribed" }
}