package io.github.speedrevanced.revanced.photomath.misc.unlock.bookpoint

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.fingerprint

val isBookpointEnabledFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("Z")
    parameters()
    strings(
        "NoGeoData",
        "NoCountryInGeo",
        "RemoteConfig",
        "GeoRCMismatch"
    )
}