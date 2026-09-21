package io.github.speedrevanced.revanced.strava.upselling

import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.fingerprint
import org.luckypray.dexkit.query.enums.StringMatchType

val getModulesFingerprint = fingerprint {
    opcodes(Opcode.IGET_OBJECT)
    methodMatcher { name = "getModules" }
    classMatcher { className(".GenericLayoutEntry", StringMatchType.EndsWith) }
}
