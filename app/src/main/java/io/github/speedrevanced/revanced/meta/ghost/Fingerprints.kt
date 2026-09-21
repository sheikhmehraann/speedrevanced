package io.github.speedrevanced.revanced.meta.ghost

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.strings

val storySeenFingerprint = findMethodDirect {
    findMethod {
        matcher {
            strings("media/seen/")
        }
    }.first()
}
