package io.github.speedrevanced.revanced.meta.ghost

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.parameters
import io.github.speedrevanced.morphe.returns
import io.github.speedrevanced.morphe.strings

val storySeenFingerprint = findMethodDirect {
    val r1 = findMethod {
        matcher {
            returns("void")
            parameters(emptyList())
            strings("media/seen/")
        }
    }
    if (r1.isNotEmpty()) return@findMethodDirect r1.first()

    val r2 = findMethod {
        matcher {
            strings("media/seen/")
        }
    }
    if (r2.isNotEmpty()) return@findMethodDirect r2.first()

    findMethod {
        matcher {
            strings("seen")
        }
    }.first()
}
