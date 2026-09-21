package io.github.speedrevanced.revanced.googlephotos

import io.github.speedrevanced.revanced.googlephotos.misc.backup.EnableDCIMFoldersBackupControl
import io.github.speedrevanced.revanced.googlephotos.misc.features.SpoofFeaturesPatch

val GooglePhotosPatches = arrayOf(
    SpoofFeaturesPatch,
    EnableDCIMFoldersBackupControl,
)