package io.github.speedrevanced.morphe.youtube.layout.shortsnoresume

import io.github.speedrevanced.RequireAppVersion
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.InstructionLocation
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.StringComparisonType
import io.github.speedrevanced.morphe.checkCast
import io.github.speedrevanced.morphe.literal
import io.github.speedrevanced.morphe.methodCall
import io.github.speedrevanced.morphe.opcode
import io.github.speedrevanced.morphe.string

@RequireAppVersion("21.03.00")
internal object UserWasInShortsEvaluateAnchorFingerprint: Fingerprint(
    returnType = "Z",
    filters = listOf(
        literal(1073815471),
        literal(1073815469)
    )
)

/**
 * 21.03+
 */
@RequireAppVersion("21.03.00")
internal object UserWasInShortsEvaluateFingerprint : Fingerprint(
    classFingerprint = UserWasInShortsEvaluateAnchorFingerprint,
    filters = listOf(
        methodCall(
            opcode = Opcode.INVOKE_DIRECT_RANGE,
            name = "<init>",
            parameters = listOf("L", "Z", "Z", "L", "Z")
        ),
//        methodCall( // 21.30+
//            opcode = Opcode.INVOKE_DIRECT_RANGE,
//            name = "<init>",
//            parameters = listOf("L", "L", "L", "L", "L", "L",  "Ljava/lang/String;"),
//            location = InstructionLocation.MatchAfterWithin(50)
//        )
    )
)

/**
 * 20.02+
 */
@RequireAppVersion("20.02.00", "21.03.00")
internal object UserWasInShortsListenerFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Ljava/lang/Object;"),
    filters = listOf(
        checkCast("Ljava/lang/Boolean;"),
        methodCall(smali = "Ljava/lang/Boolean;->booleanValue()Z", location = InstructionLocation.MatchAfterImmediately()),
        opcode(Opcode.MOVE_RESULT, InstructionLocation.MatchAfterImmediately()),
        string("ShortsStartup SetUserWasInShortsListener", StringComparisonType.CONTAINS, InstructionLocation.MatchAfterWithin(30))
    )
)
