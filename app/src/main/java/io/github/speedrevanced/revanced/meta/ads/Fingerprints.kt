package io.github.speedrevanced.revanced.meta.ads

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.returnType
import io.github.speedrevanced.morphe.strings

val adInjectorFingerprint = findMethodDirect {
    val r1 = findMethod {
        matcher {
            returnType("boolean")
            strings("Is ad pod")
        }
    }
    if (r1.isNotEmpty()) return@findMethodDirect r1.first()

    val r2 = findMethod {
        matcher {
            returnType("boolean")
            strings("SponsoredContentController.insertItem")
        }
    }
    if (r2.isNotEmpty()) return@findMethodDirect r2.first()

    findMethod {
        matcher {
            returnType("boolean")
            strings("sponsored_content")
        }
    }.first()
}