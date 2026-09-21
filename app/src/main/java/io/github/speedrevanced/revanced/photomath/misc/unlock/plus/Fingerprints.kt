package io.github.speedrevanced.revanced.photomath.misc.unlock.plus

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.fingerprint
import org.luckypray.dexkit.query.enums.StringMatchType

val isPlusUnlockedFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("Z")
    strings("genius")
    classMatcher { className(".User", StringMatchType.EndsWith) }
}