package io.github.speedrevanced.revanced.meta.ghost

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import io.github.speedrevanced.patch
import java.lang.reflect.Method
import java.util.Map

val GhostStorySeen = patch(
    name = "Ghost mode stories",
    description = "Prevents sending seen receipts when viewing stories."
) {
    var hooked = false
    runCatching {
        // Modern IG (442+/447): Find callers of builder method
        val methodData = ::storySeenFingerprint.get()
        val itemClassName = methodData.className
        var storeClassName: String? = null
        for (caller in methodData.callers) {
            val cn = caller.className
            if (!cn.isNullOrEmpty() && cn != itemClassName) {
                storeClassName = cn
                break
            }
        }

        if (storeClassName != null) {
            val storeClass = Class.forName(storeClassName, false, classLoader)
            val gate = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (param.thisObject != null && storeClass.isInstance(param.thisObject)) {
                        param.result = null
                    }
                }
            }

            // Batch inserts on base class
            var c: Class<*>? = storeClass.superclass
            while (c != null && c != Any::class.java) {
                var found = false
                for (m in c.declaredMethods) {
                    if (m.returnType != java.lang.Void.TYPE) continue
                    val p = m.parameterTypes
                    val perItem = p.size == 2 && p[0] == String::class.java && p[1] == Any::class.java
                    val bulk = p.size == 1 && Map::class.java.isAssignableFrom(p[0])
                    if (!perItem && !bulk) continue
                    runCatching {
                        m.isAccessible = true
                        XposedBridge.hookMethod(m, gate)
                        found = true
                        hooked = true
                    }
                }
                if (found) break
                c = c.superclass
            }

            // Immediate sender on store itself
            for (m in storeClass.declaredMethods) {
                if (m.returnType != java.lang.Void.TYPE) continue
                val p = m.parameterTypes
                if (p.size != 1) continue
                val pt = p[0]
                if (pt.isPrimitive || pt == String::class.java || Map::class.java.isAssignableFrom(pt) || java.util.Collection::class.java.isAssignableFrom(pt)) continue
                runCatching {
                    m.isAccessible = true
                    XposedBridge.hookMethod(m, gate)
                    hooked = true
                }
            }
        }
    }

    if (!hooked) {
        // Fallback for legacy IG
        runCatching {
            ::storySeenFingerprint.hookMethod(object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = null
                }
            })
        }
    }
}

