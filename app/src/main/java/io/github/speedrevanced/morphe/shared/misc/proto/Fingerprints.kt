package io.github.speedrevanced.morphe.shared.misc.proto

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.RestrictQuery
import io.github.speedrevanced.morphe.checkCast
import io.github.speedrevanced.morphe.methodCall
import io.github.speedrevanced.morphe.string

@RestrictQuery
internal object NewElementProtoParserFingerprint : Fingerprint(
    classFingerprint = ProtoStuffReflectionFingerprint,
    accessFlags = listOf(AccessFlags.STATIC),
    parameters = listOf("L"),
    returnType = "[B",
    filters = listOf(
        checkCast("[B")
    )
)

private object ProtoStuffReflectionFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.STATIC),
    parameters = listOf(),
    returnType = "Ljava/lang/reflect/Field;",
    filters = listOf(
        string("buf"),
        methodCall(
            opcode = Opcode.INVOKE_VIRTUAL,
            name = "getDeclaredField"
        ),
        methodCall(
            opcode = Opcode.INVOKE_VIRTUAL,
            name = "setAccessible"
        )
    )
)
