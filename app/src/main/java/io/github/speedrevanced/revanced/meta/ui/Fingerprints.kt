package io.github.speedrevanced.revanced.meta.ui

import io.github.speedrevanced.morphe.findMethodDirect
import io.github.speedrevanced.morphe.strings

val metaAIOptionFingerprint = findMethodDirect {
    val r1 = findMethod {
        matcher {
            strings("meta_ai_content_deep_dive_prompt_v2")
        }
    }
    if (r1.isNotEmpty()) return@findMethodDirect r1.first()

    val r2 = findMethod {
        matcher {
            strings("GEN_AI_INFO")
        }
    }
    if (r2.isNotEmpty()) return@findMethodDirect r2.first()

    findMethod {
        matcher {
            strings("meta_ai")
        }
    }.first()
}
