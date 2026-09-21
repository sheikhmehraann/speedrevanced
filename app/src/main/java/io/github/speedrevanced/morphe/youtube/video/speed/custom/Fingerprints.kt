package io.github.speedrevanced.morphe.youtube.video.speed.custom

import io.github.speedrevanced.RequireAppVersion
import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.Opcode
import io.github.speedrevanced.morphe.findFieldDirect
import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.fingerprint
import io.github.speedrevanced.morphe.literal
import io.github.speedrevanced.morphe.parameters
import io.github.speedrevanced.morphe.returns
import io.github.speedrevanced.morphe.youtube.shared.SpeedLimiterFingerprint

internal val speedArrayGeneratorFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.STATIC)
    returns("[L")
    parameters("L")
    strings("0.0#")
}

// found in com.google.android.libraries.youtube.innertube.model.media.PlayerConfigModel
val speedsFloatArrayField = findFieldDirect {
    speedArrayGeneratorFingerprint().usingFields.single {
        it.field.typeSign == "[F"
    }.field
}

@RequireAppVersion("20.34.00")
internal object ServerSideMaxSpeedFeatureFlagFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    filters = listOf(
        literal(45719140L)
    )
)

val clampFloatFingerprint = findMethodDirect {
    SpeedLimiterFingerprint().invokes.findMethod {
        matcher {
            parameters("F", "F", "F")
            returns("F")
        }
    }.single()
}
