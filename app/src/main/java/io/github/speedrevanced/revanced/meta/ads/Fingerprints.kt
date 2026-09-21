package io.github.speedrevanced.revanced.meta.ads

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.strings

val adInjectorFingerprint = findMethodDirect {
    findMethod {
        matcher {
            strings("Is ad pod")
        }
    }.single()
}