package io.github.speedrevanced.morphe.shared

import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.StringComparisonType
import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.methodCall
import io.github.speedrevanced.morphe.string

internal object SpannableStringBuilderFingerprint : Fingerprint(
    returnType = "Ljava/lang/CharSequence;",
    filters = listOf(
        methodCall(
            opcode = Opcode.INVOKE_INTERFACE,
            returnType = "Ljava/lang/String;",
            parameters = emptyList()
        ),
        methodCall(
            opcode = Opcode.INVOKE_STATIC,
            smali = "Landroid/text/SpannableString;->valueOf(Ljava/lang/CharSequence;)Landroid/text/SpannableString;"
        ),
        methodCall(
            opcode = Opcode.INVOKE_STATIC,
            returnType = "V",
            parameters = listOf(
                "Landroid/text/SpannableString;",
                "Ljava/lang/Object;",
                "I",
                "Z",
                "I"
            )
        ),
        string(
            "Failed to set PB Style Run Extension in TextComponentSpec.",
            comparison = StringComparisonType.STARTS_WITH
        )
    )
)

val spannableStringBuilderGetSpannedMethod = findMethodDirect {
    SpannableStringBuilderFingerprint.instructionMatches[0].instruction.methodRef!!
}