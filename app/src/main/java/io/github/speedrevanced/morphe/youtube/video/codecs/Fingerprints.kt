package io.github.speedrevanced.morphe.youtube.video.codecs

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint

internal object Vp9CapabilityFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    strings = listOf(
        "vp9_supported",
        "video/x-vnd.on2.vp9"
    )
)
