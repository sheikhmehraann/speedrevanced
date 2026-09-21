package io.github.speedrevanced.morphe.youtube.misc.engagement

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.fieldAccess
import io.github.speedrevanced.morphe.findClassDirect
import io.github.speedrevanced.morphe.findFieldDirect
import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.youtube.shared.EngagementPanelControllerFingerprint

internal object EngagementPanelUpdateFingerprint : Fingerprint(
    classFingerprint = EngagementPanelControllerFingerprint,
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("L", "Z"),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            type = "Landroid/app/Activity;"
        )
    )
)

val panelInitFingerprint = findMethodDirect {
    panelClass().findMethod {
        matcher {
            name = "<init>"
        }
    }.single()
}

val panelIdField = findFieldDirect {
    panelClass().fields.single { it.typeName == "java.lang.String" }
}

val panelClass = findClassDirect {
    EngagementPanelControllerFingerprint.instructionMatches[3].instruction.classRef!!
}