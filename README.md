# Speed Revanced

[![Build](https://img.shields.io/badge/LSPosed-Module-blue.svg)](https://github.com/speedrevanced)
[![License](https://img.shields.io/badge/License-GPL%20v3-green.svg)](LICENSE)

**Speed Revanced** is an advanced LSPosed / Xposed module engineered for rooted Android devices, powered by the latest Morphe, ReVanced patches, and DexKit runtime bytecode hooking.

Modify YouTube, YouTube Music, Reddit, and more at runtime — without modifying, repacking, or resigning original APK files.

---

## ⚡ Key Highlights & Features

- **Root / LSPosed Runtime Hooking**: Dynamic DexKit pattern matching injected directly into official app runtimes.
- **Custom Playback Speeds**: Extended speed boundaries (up to 8.0x), custom speed presets, and audio pitch control.
- **Ad-Free Playback**: Full video and feed ad blocking across YouTube and YouTube Music.
- **Background & PiP Playback**: Background audio playback, Picture-in-Picture mode enabled.
- **SponsorBlock Integration**: Automatically skip sponsored segments, intros, outros, and subscription reminders.
- **Return YouTube Dislike**: View dislikes and like/dislike ratios directly in the player.
- **MicroG-Free**: Run directly on official Google Play Store apps using your primary Google account without requiring MicroG or GmsCore.

---

## 📱 Supported Applications & Scope

Speed Revanced supports hooking into:
- **YouTube** (`com.google.android.youtube`)
- **YouTube Music** (`com.google.android.apps.youtube.music`)
- **Reddit** (`com.reddit.frontpage`)
- **Google Photos** (`com.google.android.apps.photos`)
- **Photomath** (`com.microblink.photomath`)
- **Instagram** (`com.instagram.android`)
- **Threads** (`com.instagram.barcelona`)
- **Strava** (`com.strava`)
- **AllTrails** (`com.alltrails.alltrails`)

---

## 🛠️ Project Structure

```text
speedrevanced/
 ├── app/
 │    ├── src/main/java/io/github/speedrevanced/
 │    │    ├── MainHook.kt              # LSPosed entry point (XposedModule)
 │    │    ├── PatchExecutor.kt         # Runtime DexKit executor & patch applicator
 │    │    ├── morphe/                  # Patch definitions & fingerprints
 │    │    │    ├── youtube/            # YouTube patches (Speed, Ads, SponsorBlock, etc.)
 │    │    │    ├── music/              # YouTube Music patches
 │    │    │    └── reddit/             # Reddit patches
 │    │    └── activity/                # Speed Revanced module settings UI
 │    └── src/main/resources/META-INF/xposed/
 │         ├── java_init.list           # Points to io.github.speedrevanced.MainHook
 │         ├── module.prop              # LSPosed module declaration
 │         └── scope.list               # Scope package list
 ├── morphe-patches/                    # Morphe patch definitions & extensions
 ├── morphe-patches-library/            # Morphe extension shared libraries
 ├── libs/                              # DexKit AAR & dependencies
 └── stub/                              # Compile-time Android framework stubs
```

---

## 🚀 Building the APK

### Prerequisites
- JDK 17+
- Android SDK (API 34+ / CompileSdk 37)

### Build Command
```bash
./gradlew :app:assembleRelease
```
The compiled APK will be located at:
```text
app/build/outputs/apk/release/app-release.apk
```

---

## 📲 Installation

1. Install the built **Speed Revanced** APK on your device.
2. Open **LSPosed Manager**.
3. Navigate to **Modules** and enable **Speed Revanced**.
4. Ensure target applications (e.g. YouTube) are checked in the module's scope.
5. Force stop the target app and open it to enjoy Speed Revanced.
