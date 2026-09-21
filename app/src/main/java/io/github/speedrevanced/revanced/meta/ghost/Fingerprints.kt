package io.github.speedrevanced.revanced.meta.ghost

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.returns
import io.github.speedrevanced.morphe.strings

val storySeenFingerprint = findMethodDirect {
    val r0 = findMethod {
        matcher {
            strings("media/seen/")
            returns("void")
        }
    }
    if (r0.isNotEmpty()) return@findMethodDirect r0.first()

    val r1 = findMethod {
        matcher {
            strings("media/seen")
            returns("void")
        }
    }
    if (r1.isNotEmpty()) return@findMethodDirect r1.first()

    findMethod {
        matcher {
            strings("media/seen/?reel=%s&live_vod=0")
        }
    }.firstOrNull() ?: error("storySeenFingerprint not found")
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

