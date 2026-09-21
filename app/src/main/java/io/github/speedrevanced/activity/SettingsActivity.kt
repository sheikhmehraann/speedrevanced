package io.github.speedrevanced.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import app.morphe.extension.shared.Utils
import io.github.libxposed.service.XposedService
import io.github.speedrevanced.BuildConfig
import io.github.speedrevanced.R
import io.github.speedrevanced.appPatchConfigurations
import io.github.speedrevanced.common.UpdateChecker
import java.io.File
import kotlin.system.exitProcess

class SettingsActivity : Activity(), SettingApplication.ServiceStateListener {

    private var mService: XposedService? = null
    private var currentTab = TAB_HOME

    private lateinit var tabHomeScroll: ScrollView
    private lateinit var tabAppsLayout: LinearLayout
    private lateinit var tabSettingsScroll: ScrollView

    private lateinit var topBarTitle: TextView
    private lateinit var topBarSubtitle: TextView

    private lateinit var navPillHome: FrameLayout
    private lateinit var navPillApps: FrameLayout
    private lateinit var navPillSettings: FrameLayout

    private lateinit var navIconHome: ImageView
    private lateinit var navIconApps: ImageView
    private lateinit var navIconSettings: ImageView

    private lateinit var navLabelHome: TextView
    private lateinit var navLabelApps: TextView
    private lateinit var navLabelSettings: TextView

    private var searchQuery = ""

    companion object {
        private const val TAB_HOME = 0
        private const val TAB_APPS = 1
        private const val TAB_SETTINGS = 2
    }

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

        initViews()
        setupWindowInsets()
        setupBottomNavigation()
        setupHeader()
        setupSystemInfo()
        setupAppsSearch()
        setupHideIconSwitch()
        setupUpdateChecker()
        setupRepoButtons()
        populateAppList()
        switchTab(TAB_HOME)
    }

    private fun initViews() {
        tabHomeScroll = findViewById(R.id.tab_home_scroll)
        tabAppsLayout = findViewById(R.id.tab_apps_layout)
        tabSettingsScroll = findViewById(R.id.tab_settings_scroll)

        topBarTitle = findViewById(R.id.top_bar_title)
        topBarSubtitle = findViewById(R.id.top_bar_subtitle)

        navPillHome = findViewById(R.id.nav_pill_home)
        navPillApps = findViewById(R.id.nav_pill_apps)
        navPillSettings = findViewById(R.id.nav_pill_settings)

        navIconHome = findViewById(R.id.nav_icon_home)
        navIconApps = findViewById(R.id.nav_icon_apps)
        navIconSettings = findViewById(R.id.nav_icon_settings)

        navLabelHome = findViewById(R.id.nav_label_home)
        navLabelApps = findViewById(R.id.nav_label_apps)
        navLabelSettings = findViewById(R.id.nav_label_settings)
    }

    private fun setupWindowInsets() {
        val root = findViewById<View>(R.id.root_layout) ?: return
        val topBar = findViewById<View>(R.id.top_bar_container)
        val bottomNav = findViewById<View>(R.id.bottom_nav_bar)

        root.setOnApplyWindowInsetsListener { _, insets ->
            val topInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.statusBars()).top
            } else {
                @Suppress("DEPRECATION")
                insets.systemWindowInsetTop
            }

            val bottomInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.navigationBars()).bottom
            } else {
                @Suppress("DEPRECATION")
                insets.systemWindowInsetBottom
            }

            topBar?.setPadding(
                (20 * resources.displayMetrics.density).toInt(),
                topInset + (12 * resources.displayMetrics.density).toInt(),
                (20 * resources.displayMetrics.density).toInt(),
                (12 * resources.displayMetrics.density).toInt()
            )

            bottomNav?.setPadding(0, 0, 0, bottomInset)
            insets
        }
    }

    private fun setupBottomNavigation() {
        findViewById<View>(R.id.nav_btn_home)?.setOnClickListener { switchTab(TAB_HOME) }
        findViewById<View>(R.id.nav_btn_apps)?.setOnClickListener { switchTab(TAB_APPS) }
        findViewById<View>(R.id.nav_btn_settings)?.setOnClickListener { switchTab(TAB_SETTINGS) }

        findViewById<View>(R.id.btn_metric_apps)?.setOnClickListener { switchTab(TAB_APPS) }
    }

    private fun switchTab(tab: Int) {
        currentTab = tab

        val accentColor = getColor(R.color.ksu_accent)
        val mutedColor = getColor(R.color.ksu_text_muted)

        // Reset all pills and colors
        navPillHome.background = null
        navPillApps.background = null
        navPillSettings.background = null

        navLabelHome.setTextColor(mutedColor)
        navLabelApps.setTextColor(mutedColor)
        navLabelSettings.setTextColor(mutedColor)

        navIconHome.setColorFilter(mutedColor)
        navIconApps.setColorFilter(mutedColor)
        navIconSettings.setColorFilter(mutedColor)

        tabHomeScroll.visibility = View.GONE
        tabAppsLayout.visibility = View.GONE
        tabSettingsScroll.visibility = View.GONE

        when (tab) {
            TAB_HOME -> {
                tabHomeScroll.visibility = View.VISIBLE
                topBarTitle.text = "Speed Revanced"
                topBarSubtitle.text = "LSPosed Module Manager"

                navPillHome.setBackgroundResource(R.drawable.bg_nav_pill)
                navLabelHome.setTextColor(accentColor)
                navIconHome.setColorFilter(accentColor)
            }
            TAB_APPS -> {
                tabAppsLayout.visibility = View.VISIBLE
                topBarTitle.text = "Applications"
                topBarSubtitle.text = "Configured target apps"

                navPillApps.setBackgroundResource(R.drawable.bg_nav_pill)
                navLabelApps.setTextColor(accentColor)
                navIconApps.setColorFilter(accentColor)
            }
            TAB_SETTINGS -> {
                tabSettingsScroll.visibility = View.VISIBLE
                topBarTitle.text = "Settings"
                topBarSubtitle.text = "Module preferences & controls"

                navPillSettings.setBackgroundResource(R.drawable.bg_nav_pill)
                navLabelSettings.setTextColor(accentColor)
                navIconSettings.setColorFilter(accentColor)
            }
        }
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

    private fun setupSystemInfo() {
        findViewById<TextView>(R.id.text_device_info)?.text = "${Build.MANUFACTURER} ${Build.MODEL}"
        findViewById<TextView>(R.id.text_android_info)?.text = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

        val selinuxMode = runCatching {
            val file = File("/sys/fs/selinux/enforce")
            if (file.exists() && file.readText().trim() == "1") "Enforcing" else "Permissive"
        }.getOrDefault("Enforcing")

        findViewById<TextView>(R.id.text_selinux_info)?.text = selinuxMode
    }

    private fun setupAppsSearch() {
        val searchInput = findViewById<EditText>(R.id.input_search_apps) ?: return
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString()?.trim()?.lowercase() ?: ""
                populateAppList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
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

            // Send package changed broadcast
            try {
                val intent = Intent(Intent.ACTION_PACKAGE_CHANGED).apply {
                    data = Uri.parse("package:$packageName")
                    putExtra(Intent.EXTRA_CHANGED_COMPONENT_NAME_LIST, arrayOf(aliasName.className))
                    putExtra(Intent.EXTRA_DONT_KILL_APP, true)
                }
                sendBroadcast(intent)
            } catch (_: Throwable) {}

            // If root is present, refresh launcher cache
            if (isChecked) {
                try {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "am broadcast -a android.intent.action.PACKAGE_CHANGED -d package:$packageName; am force-stop com.transsion.XOSLauncher 2>/dev/null; pkill -f launcher 2>/dev/null"))
                } catch (_: Throwable) {}
                Utils.showToastLong("Icon hidden from launcher.")
            } else {
                try {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "am broadcast -a android.intent.action.PACKAGE_CHANGED -d package:$packageName; am force-stop com.transsion.XOSLauncher 2>/dev/null; pkill -f launcher 2>/dev/null"))
                } catch (_: Throwable) {}
                Utils.showToastLong("Icon unhidden.")
            }
        }
    }

    private fun setupUpdateChecker() {
        val btnUpdates = findViewById<View>(R.id.btn_check_updates)
        val textVersion = findViewById<TextView>(R.id.text_version_info)
        val textAboutVersion = findViewById<TextView>(R.id.text_about_version)
        val textAboutCommit = findViewById<TextView>(R.id.text_about_commit)

        val versionString = "v${BuildConfig.VERSION_NAME} (${BuildConfig.COMMIT_HASH})"
        textVersion?.text = versionString
        textAboutVersion?.text = "Version ${BuildConfig.VERSION_NAME}"
        textAboutCommit?.text = "Commit: ${BuildConfig.COMMIT_HASH}"

        btnUpdates?.setOnClickListener {
            UpdateChecker().apply {
                setActivity(this@SettingsActivity)
                checkUpdate(silent = false)
            }
        }
    }

    private fun setupRepoButtons() {
        val openRepo = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sheikhmehraann/speedrevanced"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_open_repo)?.setOnClickListener { openRepo() }
        findViewById<View>(R.id.btn_home_repo)?.setOnClickListener { openRepo() }
    }

    @SuppressLint("SetTextI18n")
    private fun populateAppList() {
        val container = findViewById<LinearLayout>(R.id.container_apps) ?: return
        container.removeAllViews()
        val inflater = LayoutInflater.from(this)

        var totalPatches = 0
        var totalActive = 0

        val filteredApps = if (searchQuery.isEmpty()) {
            appPatchConfigurations
        } else {
            appPatchConfigurations.filter {
                it.appName.lowercase().contains(searchQuery) || it.packageName.lowercase().contains(searchQuery)
            }
        }

        for ((index, appInfo) in filteredApps.withIndex()) {
            val itemView = inflater.inflate(R.layout.ksu_app_item, container, false)
            val iconView = itemView.findViewById<ImageView>(R.id.app_item_icon)
            val avatarView = itemView.findViewById<TextView>(R.id.app_item_avatar)
            val titleView = itemView.findViewById<TextView>(R.id.app_item_title)
            val pkgView = itemView.findViewById<TextView>(R.id.app_item_pkg)
            val countView = itemView.findViewById<TextView>(R.id.app_item_count)

            // Try loading real app icon if installed on device
            try {
                val appIcon = packageManager.getApplicationIcon(appInfo.packageName)
                iconView.setImageDrawable(appIcon)
                iconView.visibility = View.VISIBLE
                avatarView.visibility = View.GONE
            } catch (_: Throwable) {
                avatarView.text = appInfo.appName.firstOrNull()?.uppercase() ?: "A"
                avatarView.visibility = View.VISIBLE
                iconView.visibility = View.GONE
            }

            titleView.text = appInfo.appName
            pkgView.text = appInfo.packageName

            val validPatches = appInfo.patches.filter { it.name.isNotEmpty() && !it.name.startsWith("<") }
            totalPatches += validPatches.size

            val service = mService
            if (service != null) {
                val remotePrefs = service.getRemotePreferences(appInfo.packageName)
                val activeCount = validPatches.count { remotePrefs.getBoolean(it.name, it.use) }
                totalActive += activeCount
                countView.text = "$activeCount / ${validPatches.size}"
            } else {
                val defaultCount = validPatches.count { it.use }
                totalActive += defaultCount
                countView.text = "$defaultCount / ${validPatches.size}"
            }

            itemView.setOnClickListener {
                val intent = Intent(this, AppPatchSettingsActivity::class.java).apply {
                    putExtra(AppPatchSettingsActivity.ARGUMENT_APP_NAME, appInfo.appName)
                }
                startActivity(intent)
            }

            container.addView(itemView)

            if (index < filteredApps.size - 1) {
                val indentPx = (70 * resources.displayMetrics.density).toInt()
                val endMarginPx = (16 * resources.displayMetrics.density).toInt()
                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    ).apply {
                        setMargins(indentPx, 0, endMarginPx, 0)
                    }
                    setBackgroundColor(getColor(R.color.ksu_card_stroke))
                }
                container.addView(divider)
            }
        }

        // Update metrics
        findViewById<TextView>(R.id.metric_apps_count)?.text = "${appPatchConfigurations.size} Apps"
        findViewById<TextView>(R.id.metric_patches_count)?.text = "$totalPatches Total"
    }

    override fun onResume() {
        super.onResume()
        populateAppList()
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
            val heroCard = findViewById<View>(R.id.card_hero_status)
            val heroIcon = findViewById<TextView>(R.id.hero_icon_badge)
            val heroTitle = findViewById<TextView>(R.id.hero_status_title)
            val heroSubtitle = findViewById<TextView>(R.id.hero_status_subtitle)
            val frameworkInfo = findViewById<TextView>(R.id.text_framework_info)

            if (service != null) {
                badge?.text = "Active"
                badge?.setTextColor(getColor(R.color.ksu_accent))
                heroCard?.setBackgroundResource(R.drawable.ksu_status_card_bg)
                heroIcon?.text = "✓"
                heroIcon?.setTextColor(getColor(R.color.ksu_accent_light))
                heroTitle?.text = "Working"
                heroTitle?.setTextColor(getColor(R.color.ksu_text_primary))
                heroSubtitle?.text = "Speed Revanced is active and hooking"
                heroSubtitle?.setTextColor(getColor(R.color.ksu_accent_light))
                frameworkInfo?.text = "LSPosed (Zygisk) System Hook"
            } else {
                badge?.text = "Inactive"
                badge?.setTextColor(getColor(R.color.ksu_text_muted))
                heroCard?.setBackgroundResource(R.drawable.ksu_status_inactive_bg)
                heroIcon?.text = "!"
                heroIcon?.setTextColor(getColor(R.color.ksu_error))
                heroTitle?.text = "Not Active"
                heroTitle?.setTextColor(getColor(R.color.ksu_text_primary))
                heroSubtitle?.text = "Enable module in LSPosed Manager"
                heroSubtitle?.setTextColor(getColor(R.color.ksu_error))
                frameworkInfo?.text = "LSPosed Service Disconnected"
            }
            populateAppList()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (currentTab != TAB_HOME) {
            switchTab(TAB_HOME)
            return
        }
        finishAndRemoveTask()
        exitProcess(0)
    }
}
