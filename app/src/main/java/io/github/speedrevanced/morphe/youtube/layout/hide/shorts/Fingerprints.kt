package io.github.speedrevanced.morphe.youtube.layout.hide.shorts

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.InstructionLocation.MatchAfterWithin
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.fieldAccess
import io.github.speedrevanced.morphe.findFieldDirect
import io.github.speedrevanced.morphe.literal
import io.github.speedrevanced.morphe.methodCall
import io.github.speedrevanced.morphe.opcode

internal object ShortsExperimentalPlayerFeatureFlagFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        literal(45677719L)
    )
)


internal object RenderNextUIFeatureFlagFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf(),
    filters = listOf(
        literal(45649743L)
    )
)

internal object DoubleTapToLikeLogicFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf("Landroid/view/MotionEvent;"),
    filters = listOf(
        literal(255),
        methodCall("Landroid/view/MotionEvent;->getEventTime()J"),
        methodCall("Ljava/lang/Math;->hypot(DD)D"),
        fieldAccess(
            opcode = Opcode.IGET_BOOLEAN,
            definingClass = "this",
            location = MatchAfterWithin(25)
        ),
        opcode(Opcode.IF_EQZ, location = MatchAfterWithin(5))
    )
)

val isDoubleTapField = findFieldDirect {
    DoubleTapToLikeLogicFingerprint.instructionMatches[3].instruction.fieldRef!!
}
