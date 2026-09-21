package io.github.speedrevanced.morphe.shared.misc.litho.node

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.methodCall
import io.github.speedrevanced.morphe.youtube.layout.hide.general.ParseElementFromBufferFingerprint

internal object TreeNodeResultListFingerprint : Fingerprint(
    classFingerprint = ParseElementFromBufferFingerprint,
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL),
    returnType = "Ljava/util/List;",
    filters = listOf(
        methodCall(name = "nCopies", opcode = Opcode.INVOKE_STATIC),
    )
)
