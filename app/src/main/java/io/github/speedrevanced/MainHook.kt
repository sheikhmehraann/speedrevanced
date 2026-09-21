package io.github.speedrevanced

import android.app.Application
import app.morphe.extension.shared.ResourceType
import app.morphe.extension.shared.ResourceUtils
import app.morphe.extension.shared.Utils
import de.robv.android.xposed.XposedHelpers
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam
import io.github.speedrevanced.morphe.ResourceFinder
import io.github.speedrevanced.morphe.resourceMappings

class MainHook : XposedModule() {

    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        modulePath = moduleApplicationInfo.sourceDir
    }

    override fun onPackageReady(param: PackageReadyParam) {
        val patches = patchesByPackage[param.packageName] ?: return

        inContext(param) { app ->
            if (isReVancedPatched(param)) {
                return@inContext
            }

            resourceMappings = object : ResourceFinder {
                override operator fun get(type: String, name: String): Int {
                    val id = ResourceUtils.getIdentifier(ResourceType.fromValue(type), name)
                    if (id == 0) throw Exception("Could not find resource type: $type name: $name")
                    return id
                }
            }

            PatchExecutor(app, param, this).applyPatches(patches)
        }
    }

    private fun isReVancedPatched(param: PackageReadyParam): Boolean {
        return runCatching {
            param.classLoader.loadClass("app.morphe.extension.shared.Utils")
        }.isSuccess || runCatching {
            param.classLoader.loadClass("app.morphe.extension.shared.utils.Utils")
        }.isSuccess || runCatching {
            param.classLoader.loadClass("app.revanced.integrations.shared.Utils")
        }.isSuccess || runCatching {
            param.classLoader.loadClass("app.revanced.integrations.shared.utils.Utils")
        }.isSuccess
    }
}

context(xposed: XposedInterface)
fun inContext(lpparam: PackageReadyParam, f: (Application) -> Unit) {
    val className = lpparam.applicationInfo.className
    val appClazz = runCatching {
        if (!className.isNullOrEmpty()) {
            XposedHelpers.findClass(className, lpparam.classLoader)
        } else {
            Application::class.java
        }
    }.getOrElse {
        Application::class.java
    }

    val onCreateMethod = runCatching {
        appClazz.getMethod("onCreate")
    }.getOrElse {
        Application::class.java.getMethod("onCreate")
    }

    onCreateMethod.hookMethod {
        before {
            val app = it.thisObject as? Application ?: return@before
            Utils.setContext(app)
            f(app)
        }
    }
}
