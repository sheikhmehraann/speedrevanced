package io.github.speedrevanced.revanced.googlephotos.misc.backup

import io.github.speedrevanced.morphe.Fingerprint

internal object isDCIMFolderBackupControlMethod : Fingerprint(
    strings = listOf(
        "/dcim",
        "/mars_files/"
    ),
    parameters = listOf("Ljava/lang/String;"),
    returnType = "Z"
)