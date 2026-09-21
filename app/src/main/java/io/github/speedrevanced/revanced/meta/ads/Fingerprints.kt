package io.github.speedrevanced.revanced.meta.ads

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.returns
import io.github.speedrevanced.morphe.strings

val adInjectorFingerprint = findMethodDirect {
    val r1 = findMethod {
        matcher {
            returns("boolean")
            strings("Is ad pod")
        }
    }
    if (r1.isNotEmpty()) return@findMethodDirect r1.first()

    val r2 = findMethod {
        matcher {
            returns("boolean")
            strings("SponsoredContentController.insertItem")
        }
    }
    if (r2.isNotEmpty()) return@findMethodDirect r2.first()

    val r3 = findMethod {
        matcher {
            returns("boolean")
            strings("sponsored_content")
        }
    }
    r3.firstOrNull() ?: r2.firstOrNull() ?: r1.firstOrNull() ?: error("adInjectorFingerprint not found")
}