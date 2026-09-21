package io.github.speedrevanced.morphe.music.layout.upgradebutton

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.fingerprint

internal val pivotBarConstructorFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
    returns("V")
    parameters("L", "Z")
    opcodes(
        Opcode.INVOKE_INTERFACE,
        Opcode.GOTO,
        Opcode.IPUT_OBJECT,
        Opcode.RETURN_VOID
    )
}
