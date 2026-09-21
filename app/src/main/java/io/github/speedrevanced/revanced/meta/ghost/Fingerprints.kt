package io.github.speedrevanced.revanced.meta.ghost

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.paramCount
import io.github.speedrevanced.morphe.returnType
import io.github.speedrevanced.morphe.strings

val storySeenFingerprint = findMethodDirect {
    val r1 = findMethod {
        matcher {
            returnType("void")
            paramCount(0)
            strings("media/seen/")
        }
    }
    if (r1.isNotEmpty()) return@findMethodDirect r1.first()

    findMethod {
        matcher {
            strings("media/seen/")
        }
    }.first()
}
