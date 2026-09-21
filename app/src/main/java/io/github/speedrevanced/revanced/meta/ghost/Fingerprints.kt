package io.github.speedrevanced.revanced.meta.ghost

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.strings

val storySeenFingerprint = findMethodDirect {
    val r1 = findMethod {
        matcher {
            strings("media/seen/?reel=%s&live_vod=0")
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
            strings("media/seen")
        }
    }.firstOrNull() ?: r2.firstOrNull() ?: r1.firstOrNull() ?: error("storySeenFingerprint not found")
}
