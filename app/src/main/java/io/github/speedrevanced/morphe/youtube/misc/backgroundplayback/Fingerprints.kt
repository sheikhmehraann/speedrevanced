package io.github.speedrevanced.morphe.youtube.misc.backgroundplayback

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.OpcodesFilter
import io.github.speedrevanced.morphe.accessFlags
import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.findMethodListDirect
import io.github.speedrevanced.morphe.fingerprint
import io.github.speedrevanced.morphe.literal
import io.github.speedrevanced.morphe.parameters
import io.github.speedrevanced.morphe.resourceMappings
import io.github.speedrevanced.morphe.returns

val prefBackgroundAndOfflineCategoryId get() = resourceMappings["string", "pref_background_and_offline_category"]

object BackgroundPlaybackManagerFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = listOf("L"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.CONST_4,
        Opcode.IF_EQZ,
        Opcode.IGET,
        Opcode.AND_INT_LIT16,
        Opcode.IF_EQZ,
        Opcode.IGET_OBJECT,
        Opcode.IF_NEZ,
        Opcode.SGET_OBJECT,
        Opcode.IGET,
        Opcode.CONST,
        Opcode.IF_NE,
        Opcode.IGET_OBJECT,
        Opcode.IF_NEZ,
        Opcode.SGET_OBJECT,
        Opcode.IGET,
        Opcode.IF_NE,
        Opcode.IGET_OBJECT,
        Opcode.CHECK_CAST,
        Opcode.GOTO,
        Opcode.SGET_OBJECT,
        Opcode.GOTO,
        Opcode.CONST_4,
        Opcode.IF_EQZ,
        Opcode.IGET_BOOLEAN,
        Opcode.IF_EQZ,
    )
)

val backgroundPlaybackSettingsFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("Ljava/lang/String;")
    parameters()
    opcodes(
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IF_EQZ,
    )
    literal { prefBackgroundAndOfflineCategoryId }
}

val backgroundPlaybackSettingsSubFingerprint = findMethodDirect {
    backgroundPlaybackSettingsFingerprint().invokes.filter { it.returnTypeName == "boolean" }[1]
}

object KidsBackgroundPlaybackPolicyControllerFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("I", "L", "L"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.CONST_4,
        Opcode.IF_NE,
        Opcode.SGET_OBJECT,
        Opcode.IF_NE,
        Opcode.IGET,
        Opcode.CONST_4,
        Opcode.IF_NE,
        Opcode.IGET_OBJECT,
    )
)

val backgroundPlaybackManagerShortsFingerprint = findMethodListDirect {
    /*
    * two matches in versions 21.02.32
    * It doesn't seem to be an A/B test;
    * it seems to be a different method that checks an additional property to determine the result.
    * */
    findMethod {
        matcher {
            accessFlags(AccessFlags.PUBLIC, AccessFlags.STATIC)
            returns("Z")
            parameters("L")
            literal { 151635310 }
        }
    }
}
