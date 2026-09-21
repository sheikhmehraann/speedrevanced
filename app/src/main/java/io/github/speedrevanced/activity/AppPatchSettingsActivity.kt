package io.github.speedrevanced.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import app.morphe.extension.shared.Utils
import io.github.libxposed.service.XposedService
import io.github.speedrevanced.AppPatchInfo
import io.github.speedrevanced.R
import io.github.speedrevanced.appPatchConfigurations

class AppPatchSettingsActivity : Activity(), SettingApplication.ServiceStateListener {

    companion object {
        const val ARGUMENT_APP_NAME = "app_name_key"
    }

    private var mService: XposedService? = null
    private var currentAppInfo: AppPatchInfo? = null
    private val patchSwitchMap = mutableMapOf<String, Switch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_patch_settings)
        Utils.setContext(this)

        val appName = intent.getStringExtra(ARGUMENT_APP_NAME)
        currentAppInfo = appPatchConfigurations.find { it.appName == appName }

        findViewById<ImageView>(R.id.btn_back)?.setOnClickListener {
            onBackPressed()
        }

        setupHeader()
        setupActionButtons()
    }

    private fun setupHeader() {
        val appInfo = currentAppInfo ?: return
        findViewById<TextView>(R.id.patch_header_title)?.text = appInfo.appName
        findViewById<TextView>(R.id.patch_header_pkg)?.text = appInfo.packageName
        updatePatchCountBadge()
    }

    private fun setupActionButtons() {
        val appInfo = currentAppInfo ?: return

        findViewById<View>(R.id.btn_default_patches)?.setOnClickListener {
            val service = mService ?: return@setOnClickListener
            val remotePrefs = service.getRemotePreferences(appInfo.packageName)
            val editor = remotePrefs.edit()
            for (patch in appInfo.patches) {
                if (patch.name.isEmpty() || patch.name.startsWith("<")) continue
                editor.putBoolean(patch.name, patch.use)
                patchSwitchMap[patch.name]?.isChecked = patch.use
            }
            editor.apply()
            vibrate()
            updatePatchCountBadge()
            Utils.showToastLong("Default settings applied")
        }

        findViewById<View>(R.id.btn_all_patches)?.setOnClickListener {
            val service = mService ?: return@setOnClickListener
            val remotePrefs = service.getRemotePreferences(appInfo.packageName)
            val editor = remotePrefs.edit()
            for (patch in appInfo.patches) {
                if (patch.name.isEmpty() || patch.name.startsWith("<")) continue
                editor.putBoolean(patch.name, true)
                patchSwitchMap[patch.name]?.isChecked = true
            }
            editor.apply()
            vibrate()
            updatePatchCountBadge()
            Utils.showToastLong("All patches enabled")
        }

        findViewById<View>(R.id.btn_none_patches)?.setOnClickListener {
            val service = mService ?: return@setOnClickListener
            val remotePrefs = service.getRemotePreferences(appInfo.packageName)
            val editor = remotePrefs.edit()
            for (patch in appInfo.patches) {
                if (patch.name.isEmpty() || patch.name.startsWith("<")) continue
                editor.putBoolean(patch.name, false)
                patchSwitchMap[patch.name]?.isChecked = false
            }
            editor.apply()
            vibrate()
            updatePatchCountBadge()
            Utils.showToastLong("All patches disabled")
        }

        findViewById<View>(R.id.btn_app_info)?.setOnClickListener {
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${appInfo.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            } catch (_: Throwable) {
                Utils.showToastLong("Could not open app details")
            }
        }
    }

    private fun populatePatches() {
        val appInfo = currentAppInfo ?: return
        val service = mService ?: return
        val container = findViewById<LinearLayout>(R.id.container_patches) ?: return
        container.removeAllViews()
        patchSwitchMap.clear()

        val remotePrefs = service.getRemotePreferences(appInfo.packageName)
        val inflater = LayoutInflater.from(this)

        val validPatches = appInfo.patches
            .filter { it.name.isNotEmpty() && !it.name.startsWith("<") }
            .sortedBy { it.name }

        for ((index, patch) in validPatches.withIndex()) {
            val itemView = inflater.inflate(R.layout.ksu_patch_item, container, false)
            val titleView = itemView.findViewById<TextView>(R.id.patch_item_title)
            val descView = itemView.findViewById<TextView>(R.id.patch_item_desc)
            val switchView = itemView.findViewById<Switch>(R.id.patch_item_switch)

            titleView.text = patch.name
            descView.text = if (patch.description.isNotEmpty()) patch.description else "Modifies runtime behavior for this app"
            val isEnabled = remotePrefs.getBoolean(patch.name, patch.use)
            switchView.isChecked = isEnabled
            patchSwitchMap[patch.name] = switchView

            itemView.setOnClickListener {
                val newState = !switchView.isChecked
                switchView.isChecked = newState
                remotePrefs.edit().putBoolean(patch.name, newState).apply()
                vibrate()
                updatePatchCountBadge()
            }

            container.addView(itemView)

            if (index < validPatches.size - 1) {
                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    ).apply {
                        setMargins(16, 0, 16, 0)
                    }
                    setBackgroundColor(getColor(R.color.ksu_card_stroke))
                }
                container.addView(divider)
            }
        }
        updatePatchCountBadge()
    }

    private fun updatePatchCountBadge() {
        val appInfo = currentAppInfo ?: return
        val service = mService
        val badge = findViewById<TextView>(R.id.patch_header_count) ?: return

        val validPatches = appInfo.patches.filter { it.name.isNotEmpty() && !it.name.startsWith("<") }
        if (service != null) {
            val remotePrefs = service.getRemotePreferences(appInfo.packageName)
            val activeCount = validPatches.count { remotePrefs.getBoolean(it.name, it.use) }
            badge.text = "$activeCount / ${validPatches.size} Active"
        } else {
            val defaultCount = validPatches.count { it.use }
            badge.text = "$defaultCount / ${validPatches.size} Patches"
        }
    }

    private fun vibrate() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
        } catch (_: Throwable) {}
    }

    override fun onStart() {
        super.onStart()
        SettingApplication.addServiceStateListener(this, true)
    }

    override fun onStop() {
        SettingApplication.removeServiceStateListener(this)
        super.onStop()
    }

    override fun onServiceStateChanged(service: XposedService?) {
        mService = service
        runOnUiThread {
            if (service != null) {
                populatePatches()
            } else {
                updatePatchCountBadge()
            }
        }
    }
}
