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

val dmSeenFingerprint = findMethodDirect {
    findMethod {
        matcher {
            strings("mark_thread_seen-")
            returns("void")
        }
    }.firstOrNull {
        it.paramTypes.size >= 3
    } ?: error("dmSeenFingerprint not found")
}

val dmTypingFingerprint = findMethodDirect {
    findMethod {
        matcher {
            strings("is_typing_indicator_enabled")
            returns("void")
        }
    }.firstOrNull {
        it.paramTypes.size in 1..2
    } ?: error("dmTypingFingerprint not found")
}

