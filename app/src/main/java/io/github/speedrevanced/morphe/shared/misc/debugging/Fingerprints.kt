package io.github.speedrevanced.morphe.shared.misc.debugging

import android.annotation.SuppressLint
import io.github.speedrevanced.TargetApp
import io.github.speedrevanced.morphe.AccessFlags
import io.github.speedrevanced.morphe.accessFlags
import io.github.speedrevanced.morphe.findClassDirect
import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.parameters
import io.github.speedrevanced.morphe.parametersMatch
import io.github.speedrevanced.morphe.returns
import io.github.speedrevanced.morphe.strings

internal val experimentFlagUtilFingerprint = findClassDirect {
    findMethod {
        matcher {
            accessFlags(AccessFlags.PUBLIC)
            returns("L")
            strings("Unable to parse proto typed experiment flag: ")
            paramCount(min = 2)
        }
    }.filter {
        it.parameters.let { params ->
            parametersMatch(
                params, listOf("L", "J", "[B")
            ) || parametersMatch(
                params, listOf("L", "J")
            ) || parametersMatch( // 21.35+
                params, listOf("J", "[B")
            )
        }
    }.map { it.declaredClass }.distinct().single()!!
}

@SuppressLint("NonUniqueDexKitData")
internal val experimentalBooleanFeatureFlagFingerprint = findMethodDirect {
    findMethod {
        matcher {
            declaredClass(experimentFlagUtilFingerprint().name)
            accessFlags(AccessFlags.PUBLIC)
            returns("Z")
            paramCount(min = 2)
        }
    }.first {
        it.parameters.let { params ->
            parametersMatch(
                params, listOf("L", "J", "Z")
            ) || parametersMatch( // 21.35+
                params,
                listOf("J", "Z")
            )
        }
    }
}

@SuppressLint("NonUniqueDexKitData")
internal val experimentalDoubleFeatureFlagFingerprint = findMethodDirect {
    findMethod {
        matcher {
            declaredClass(experimentFlagUtilFingerprint().name)
            accessFlags(AccessFlags.PUBLIC)
            returns("D")
            paramCount(min = 2)
        }
    }.first {
        it.parameters.let { params ->
            parametersMatch(
                params, listOf("L", "J", "D")
            ) || parametersMatch( // 21.35+
                params,
                listOf("J", "D")
            )
        }
    }
}

@SuppressLint("NonUniqueDexKitData")
internal val experimentalLongFeatureFlagFingerprint = findMethodDirect {
    findMethod {
        matcher {
            declaredClass(experimentFlagUtilFingerprint().name)
            accessFlags(AccessFlags.PUBLIC)
            returns("J")
            paramCount(min = 2)
        }
    }.first {
        it.parameters.let { params ->
            parametersMatch(
                params, listOf("L", "J", "J")
            ) || parametersMatch( // 21.35+
                params,
                listOf("J", "J")
            )
        }
    }
}

@get:TargetApp("youtube")
@SuppressLint("NonUniqueDexKitData")
internal val experimentalStringFeatureFlagFingerprint = findMethodDirect {
    findMethod {
        matcher {
            declaredClass(experimentFlagUtilFingerprint().name)
            accessFlags(AccessFlags.PUBLIC)
            returns("Ljava/lang/String;")
            paramCount(min = 2)
        }
    }.first {
        it.parameters.let { params ->
            parametersMatch(
                params, listOf("L", "J", "Ljava/lang/String;")
            ) || parametersMatch( // 21.35+
                params,
                listOf("J", "Ljava/lang/String;")
            )
        }
    }
}