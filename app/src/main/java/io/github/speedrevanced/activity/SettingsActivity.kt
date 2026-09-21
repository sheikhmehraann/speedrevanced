package io.github.speedrevanced.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import app.morphe.extension.shared.Utils
import io.github.libxposed.service.XposedService
import io.github.speedrevanced.BuildConfig
import io.github.speedrevanced.R
import io.github.speedrevanced.appPatchConfigurations
import io.github.speedrevanced.common.UpdateChecker
import kotlin.system.exitProcess

class SettingsActivity : Activity(), SettingApplication.ServiceStateListener {

    private var mService: XposedService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                onBackPressed()
            }
        }
        setContentView(R.layout.activity_settings)
        Utils.setContext(this)

        setupHeader()
        setupHideIconSwitch()
        setupUpdateChecker()
        setupRepoButton()
        populateAppList()
    }

    private fun setupHeader() {
        val badge = findViewById<TextView>(R.id.speed_status_badge)
        badge?.setOnClickListener {
            try {
                val intent = packageManager.getLaunchIntentForPackage("org.lsposed.manager")
                if (intent != null) startActivity(intent)
                else Utils.showToastLong("LSPosed Manager not found")
            } catch (_: Throwable) {
                Utils.showToastLong("Unable to open LSPosed Manager")
            }
        }
    }

    private fun setupHideIconSwitch() {
        val switchHide = findViewById<Switch>(R.id.switch_hide_icon) ?: return
        val aliasName = ComponentName(this, "$packageName.activity.SettingsActivityAlias")

        val currentState = packageManager.getComponentEnabledSetting(aliasName)
        switchHide.isChecked = (currentState == PackageManager.COMPONENT_ENABLED_STATE_DISABLED)

        switchHide.setOnCheckedChangeListener { _, isChecked ->
            val newState = if (isChecked) {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            }
            packageManager.setComponentEnabledSetting(
                aliasName,
                newState,
                PackageManager.DONT_KILL_APP
            )
            if (isChecked) {
                Utils.showToastLong("Icon hidden. Remember to disable 'Force apps to show launcher icons' in LSPosed if icon remains.")
            } else {
                Utils.showToastLong("Icon unhidden.")
            }
        }
    }

    private fun setupUpdateChecker() {
        val btnUpdates = findViewById<View>(R.id.btn_check_updates)
        val textVersion = findViewById<TextView>(R.id.text_version_info)
        textVersion?.text = "Version: ${BuildConfig.VERSION_NAME} (${BuildConfig.COMMIT_HASH})"

        btnUpdates?.setOnClickListener {
            UpdateChecker().apply {
                setActivity(this@SettingsActivity)
                checkUpdate(silent = false)
            }
        }
    }

    private fun setupRepoButton() {
        findViewById<View>(R.id.btn_open_repo)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sheikhmehraann/speedrevanced"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun populateAppList() {
        val container = findViewById<LinearLayout>(R.id.container_apps) ?: return
        container.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for ((index, appInfo) in appPatchConfigurations.withIndex()) {
            val itemView = inflater.inflate(R.layout.ksu_app_item, container, false)
            val titleView = itemView.findViewById<TextView>(R.id.app_item_title)
            val pkgView = itemView.findViewById<TextView>(R.id.app_item_pkg)
            val countView = itemView.findViewById<TextView>(R.id.app_item_count)

            titleView.text = appInfo.appName
            pkgView.text = appInfo.packageName
            val activePatches = appInfo.patches.count { it.name.isNotEmpty() && !it.name.startsWith("<") }
            countView.text = "$activePatches Patches"

            itemView.setOnClickListener {
                val intent = Intent(this, AppPatchSettingsActivity::class.java).apply {
                    putExtra(AppPatchSettingsActivity.ARGUMENT_APP_NAME, appInfo.appName)
                }
                startActivity(intent)
            }

            container.addView(itemView)

            if (index < appPatchConfigurations.size - 1) {
                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    ).apply {
                        setMargins(20, 0, 20, 0)
                    }
                    setBackgroundColor(getColor(R.color.ksu_card_stroke))
                }
                container.addView(divider)
            }
        }
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
            val badge = findViewById<TextView>(R.id.speed_status_badge)
            val textWorkingMode = findViewById<TextView>(R.id.text_working_mode)
            if (service != null) {
                badge?.text = "Active"
                badge?.setTextColor(getColor(R.color.ksu_accent))
                textWorkingMode?.text = "LSPosed System Hook Active"
            } else {
                badge?.text = "Inactive"
                badge?.setTextColor(getColor(R.color.ksu_text_muted))
                textWorkingMode?.text = "Module Inactive (Enable in LSPosed Manager)"
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAndRemoveTask()
        exitProcess(0)
    }
}
