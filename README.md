<div align="center">

<br/>

```
███████╗████████╗███████╗ █████╗ ██╗  ████████╗██╗  ██╗    ██╗   ██╗ █████╗ ██╗   ██╗██╗  ████████╗
██╔════╝╚══██╔══╝██╔════╝██╔══██╗██║  ╚══██╔══╝██║  ██║    ██║   ██║██╔══██╗██║   ██║██║  ╚══██╔══╝
███████╗   ██║   █████╗  ███████║██║     ██║   ███████║    ██║   ██║███████║██║   ██║██║     ██║   
╚════██║   ██║   ██╔══╝  ██╔══██║██║     ██║   ██╔══██║    ╚██╗ ██╔╝██╔══██║██║   ██║██║     ██║   
███████║   ██║   ███████╗██║  ██║███████╗██║   ██║  ██║     ╚████╔╝ ██║  ██║╚██████╔╝███████╗██║   
╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝╚═╝   ╚═╝  ╚═╝      ╚═══╝  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝   
```

### 🔐 *Your secrets, disguised as math.*

<br/>

[![Kotlin](https://img.shields.io/badge/Android-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![React](https://img.shields.io/badge/Web-React_+_TypeScript-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev)
[![AES-256](https://img.shields.io/badge/Encryption-AES--256--GCM-00C853?style=for-the-badge&logo=gnuprivacyguard&logoColor=white)](#)
[![Hilt](https://img.shields.io/badge/DI-Hilt-FF6F00?style=for-the-badge&logo=android&logoColor=white)](#)
[![License](https://img.shields.io/badge/License-Proprietary-FF1744?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](LICENSE)

<br/>

> **Stealth Vault** is disguised as a fully functional scientific calculator.  
> Enter the secret PIN and press `=` — and a hardware-encrypted private vault opens.  
> *Nobody will ever know what's hiding behind the numbers.*

<br/>

</div>

---

## 📌 Table of Contents

- [✨ Overview](#-overview)
- [🌟 Features](#-features)
- [🛠️ Tech Stack](#️-tech-stack)
- [📁 Project Structure](#-project-structure)
- [🚀 Getting Started](#-getting-started)
- [🛡️ Security Architecture](#️-security-architecture)
- [⚖️ License](#️-license)

---

## ✨ Overview

**Stealth Vault** is a multi-platform privacy ecosystem. On the surface, it's an elegant calculator app. Underneath, it's a hardened encrypted sandbox for your most sensitive files, photos, videos, and app data.

This monorepo contains **two applications**:

| Platform | Directory | Description |
|:---:|:---:|:---|
| 📱 **Android** | `/app` | Native Kotlin app with hardware-backed AES-256 encryption, intruder detection, app locking, and a decoy PIN system |
| 💻 **Web** | `/web` | React + TypeScript companion — same decoy calculator UI, auto-lock, glassmorphism design, and Framer Motion animations |

---

## 🌟 Features

<br/>

### 🧮 &nbsp; Decoy Calculator — The Perfect Cover

> Looks and behaves exactly like a real scientific calculator.

| What it does | How |
|:---|:---|
| Full math expressions | `exp4j` (Android) / `Function()` eval (Web) |
| Scientific functions | `sin`, `cos`, `log`, `√`, `x²`, `π`, `e` |
| Memory operations | `MC`, `MR`, `M+`, `M−` |
| Calculation history | Tap the clock icon to reveal past results |
| Swipe to delete | Swipe left/right on the display to backspace |
| Haptic feedback | Tactile response on every button press |
| **Secret unlock trigger** | Enter PIN → press `=` → vault opens silently |

---

### 🔐 &nbsp; Hardware-Grade Encryption

> Your files are invisible to the OS and encrypted at rest.

```
User PIN  ──►  Android KeyStore (HSM)  ──►  AES-256-GCM Key Derivation
                                                      │
                                                      ▼
                               Encrypted Blob  ◄──  File / Photo / Video
                               (stored in app private dir — invisible to file explorers)
```

- 🔑 Keys are sealed in **Android KeyStore** — never exposed to memory unless actively decrypting
- 🗄️ Vault metadata stored in **Room DB + SQLCipher** (database-level encryption)
- 🚫 Vault files are **stripped from Android Gallery** and all OS scanners immediately on import

---

### 📱 &nbsp; App Locker with Fake Crash Screen

> Lock any installed app. Confuse anyone who tries to open it.

- Runs as a **background accessibility service**, monitoring foreground app changes
- When a locked app is opened by an intruder, the screen is replaced with a **convincing "Application has stopped" crash dialog**
- You bypass the overlay with a **secret gesture** known only to you

---

### 🕵️ &nbsp; Anti-Intruder Protocols

| Feature | Description |
|:---|:---|
| 📸 **Intruder Selfie** | CameraX silently snaps a front photo on every wrong PIN |
| 🪤 **Decoy PIN** | A second PIN that opens a clean, empty decoy vault |
| 💥 **Emergency Wipe** | After 5 failed attempts, all vault data is permanently erased |
| ⏱️ **Auto-Lock (Web)** | Web app locks after 3 minutes of inactivity |

---

## 🛠️ Tech Stack

<br/>

<table>
<tr>
<td valign="top" width="50%">

### 📱 Android App

| Layer | Technology |
|:---|:---|
| Language | Kotlin |
| Architecture | MVVM + Clean Architecture |
| UI | Material Design 3, Jetpack Nav |
| Encryption | AES-256-GCM, Android KeyStore |
| Database | Room DB + SQLCipher |
| Dependency Injection | Hilt |
| Camera | CameraX |
| CI/CD | GitHub Actions |

</td>
<td valign="top" width="50%">

### 💻 Web Companion

| Layer | Technology |
|:---|:---|
| Language | TypeScript |
| Framework | React 18 |
| Build Tool | Vite |
| Animations | Framer Motion |
| Icons | Lucide React |
| Styling | Vanilla CSS (HSL variables) |
| Design | Glassmorphism, Dark Mode |
| Lock | Custom `useAutoLock` hook |

</td>
</tr>
</table>

---

## 📁 Project Structure

```
Security-Vault/
│
├── 📱 app/                              ← Android application
│   └── src/main/
│       ├── java/com/stealthvault/app/
│       │   ├── data/
│       │   │   ├── local/              ← Room DB entities, DAOs, preferences
│       │   │   ├── repository/         ← VaultRepository (file ops + encryption)
│       │   │   └── security/           ← AES-256-GCM crypto engine
│       │   ├── di/                     ← Hilt dependency injection modules
│       │   ├── service/                ← App lock service, emergency wipe
│       │   ├── ui/
│       │   │   ├── fake/               ← Decoy Calculator Activity
│       │   │   ├── lock/               ← PIN lock screen
│       │   │   ├── vault/              ← Main vault fragments + ViewModel
│       │   │   └── settings/           ← Decoy PIN, wipe config, preferences
│       │   └── utils/                  ← Sensor security, helpers
│       └── res/                        ← Layouts, drawables, themes
│
├── 💻 web/                              ← React + TS web companion
│   └── src/
│       ├── components/
│       │   ├── DecoyCalculator.tsx     ← Full calculator UI + stealth trigger
│       │   └── Sidebar.tsx             ← Vault navigation sidebar
│       ├── hooks/
│       │   └── useAutoLock.ts          ← Inactivity auto-lock hook
│       ├── App.tsx                     ← Root app (lock/unlock state)
│       └── index.css                   ← HSL design tokens + glassmorphism
│
└── ⚙️  build.gradle.kts                 ← Root Gradle configuration
```

---

## 🚀 Getting Started

### 📱 Android

**Requirements:** Android Studio Jellyfish+, JDK 17, Android SDK 34+

```bash
# 1. Clone the repo
git clone https://github.com/Subhan-Haider/Security-Vault.git

# 2. Open in Android Studio → File > Open → select the repo root
# 3. Wait for Gradle sync to complete

# 4. Build the debug APK
./gradlew assembleDebug

# Output → app/build/outputs/apk/debug/app-debug.apk
```

---

### 💻 Web Companion

**Requirements:** Node.js 18+, npm

```bash
# 1. Enter the web directory
cd web

# 2. Install dependencies
npm install

# 3. Launch the dev server
npm run dev

# 4. Open in browser
#    http://localhost:5173
#
#    Enter PIN: 1337  →  press =  →  vault unlocks
```

---

## 🛡️ Security Architecture

Stealth Vault uses **defense-in-depth** — multiple independent security layers:

```
┌─────────────────────────────────────────────────┐
│                 LAYER 1 — OBSCURITY              │
│   Calculator disguise, no visible vault UI       │
├─────────────────────────────────────────────────┤
│                 LAYER 2 — ACCESS CONTROL         │
│   PIN verification, Decoy PIN, Auto-lock         │
├─────────────────────────────────────────────────┤
│                 LAYER 3 — ENCRYPTION             │
│   AES-256-GCM keys inside Android KeyStore HSM   │
│   SQLCipher encrypted database                   │
├─────────────────────────────────────────────────┤
│                 LAYER 4 — ANTI-FORENSICS         │
│   Files hidden from OS, metadata stripped        │
│   Intruder selfie, Emergency wipe on 5 failures  │
└─────────────────────────────────────────────────┘
```

---

## ⚖️ License

<div align="center">

**© 2026 Subhan Haider — All Rights Reserved**

This project is released under a **Custom Proprietary License**.

| | Restriction |
|:---:|:---|
| 🚫 | No publishing to any app store (Google Play, Apple App Store, etc.) |
| 🚫 | No redistribution or sharing with third parties |
| 🚫 | No modification or creation of derivative works |
| 🚫 | No commercial use of any kind |
| ✅ | Personal viewing and private study is permitted |

See the full [LICENSE](LICENSE) for details.

<br/>

---

*Built with 🔐 by [Subhan Haider](https://github.com/Subhan-Haider)*

</div>
