package io.github.speedrevanced.morphe.shared.ad

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.ResourceType
import io.github.speedrevanced.morphe.findFieldDirect
import io.github.speedrevanced.morphe.methodCall
import io.github.speedrevanced.morphe.resourceLiteral


internal object LithoDialogBuilderFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("[B", "L"),
    filters = listOf(
        methodCall(
            opcode = Opcode.INVOKE_VIRTUAL,
            name = "show"
        ),
        resourceLiteral(ResourceType.STYLE, "SlidingDialogAnimation"),
    )
)

val LithoDialogField = findFieldDirect {
    LithoDialogBuilderFingerprint.let {
        val dialogClass =
            it.instructionMatches.first().instruction.methodRef!!.declaredClass!!.descriptor

        it().instructions.reversed().first { instruction ->
            instruction.opcode == Opcode.IPUT_OBJECT.ordinal &&
                    instruction.fieldRef!!.typeSign == dialogClass
        }.fieldRef!!
    }
}

private object CustomDialogOnBackPressedParentFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("L"),
    filters = listOf(
        methodCall("Landroid/app/Dialog;->onBackPressed()V")
    ),
    custom = {
        declaredClass {
            superClass {
                descriptor = "Landroid/app/Dialog;"
            }
        }
    }
)

internal object CustomDialogOnBackPressedFingerprint : Fingerprint(
    classFingerprint = CustomDialogOnBackPressedParentFingerprint,
    name = "onBackPressed",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf(),
)