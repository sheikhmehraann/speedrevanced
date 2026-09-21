package io.github.speedrevanced.revanced.meta.feed

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.strings

val feedItemParserFingerprint = findMethodDirect {
    val r1 = findMethod {
        matcher {
            strings("clips_netego", "suggested_users", "Unknown FeedItem Type")
        }
    }
    if (r1.isNotEmpty()) return@findMethodDirect r1.first()

    val r2 = findMethod {
        matcher {
            strings("clips_netego", "media_or_ad")
        }
    }
    if (r2.isNotEmpty()) return@findMethodDirect r2.first()

    val r3 = findMethod {
        matcher {
            strings("clips_netego", "stories_netego")
        }
    }
    r3.firstOrNull() ?: r2.firstOrNull() ?: r1.firstOrNull() ?: error("feedItemParserFingerprint not found")
}
