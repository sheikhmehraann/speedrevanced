package io.github.speedrevanced.morphe.youtube.layout.player.buttons

import android.view.View
import app.morphe.extension.youtube.videoplayer.PlayerOverlayButton
import io.github.speedrevanced.morphe.youtube.misc.playercontrols.fullscreen_button_id
import io.github.speedrevanced.patch
import io.github.speedrevanced.scopedHook
import org.luckypray.dexkit.wrap.DexMethod

private val initializeButtonList = mutableListOf<(view: View) -> Unit>()

fun addPlayerBottomButton(initializeButton: (view: View) -> Unit) {
    initializeButtonList.add(initializeButton)
}

val playerOverlayButtonsHook = patch {

    ExploderUIFullscreenButtonFingerprint.hookMethod(scopedHook(DexMethod("Landroid/view/View;->findViewById(I)Landroid/view/View;").toMember()) {
        val fullscreenButtonId = fullscreen_button_id
        after {
            if (innerDepth != 0) return@after
            if (it.args[0] == fullscreenButtonId) {
                val view = it.result as? View ?: return@after
                PlayerOverlayButton.initializeButton(view)
                initializeButtonList.forEach { func -> func(view) }
            }
        }
    })

    // TODO Addon
    // addPlayerBottomButton(AddOnApi::initializeButton)
}
