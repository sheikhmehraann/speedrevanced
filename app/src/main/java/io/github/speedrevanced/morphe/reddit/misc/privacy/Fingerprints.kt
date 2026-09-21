package io.github.speedrevanced.morphe.reddit.misc.privacy

import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.Fingerprint
import io.github.speedrevanced.morphe.methodCall

internal object ShareLinkFormatterFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    returnType = "Ljava/lang/String;",
    parameters = listOf("Ljava/lang/String;", "Ljava/util/Map;"),
    filters = listOf(
        methodCall(smali = "Landroid/net/Uri${'$'}Builder;->clearQuery()Landroid/net/Uri${'$'}Builder;")
    )
)
