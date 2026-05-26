# 🛡️ Stealth Vault

<p align="center">
  <img src="https://raw.githubusercontent.com/Subhan-Haider/Stealth-Vault/main/app/src/main/res/drawable/ic_vault_logo.png" alt="Stealth Vault Logo" width="128" height="128">
</p>

<p align="center">
  <strong>An impenetrable, decoy-disguised privacy sandbox for Android & Web.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-Kotlin-3498db?style=for-the-badge&logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/Web-React%20%7C%20TS-61dafb?style=for-the-badge&logo=react" alt="React & TS">
  <img src="https://img.shields.io/badge/Encryption-AES--256--GCM-2ecc71?style=for-the-badge" alt="AES-256-GCM">
  <img src="https://img.shields.io/badge/License-MIT-f1c40f?style=for-the-badge" alt="MIT License">
</p>

---

## 📖 Table of Contents
1. [Overview](#-overview)
2. [Key Features](#-key-features)
3. [Architecture & Technology Stack](#-architecture--technology-stack)
4. [Project Directory Structure](#-project-directory-structure)
5. [Getting Started](#-getting-started)
   - [Android App Setup](#android-app-setup)
   - [Web Companion Setup](#web-companion-setup)
6. [Security Architecture](#-security-architecture)
7. [Contributing](#-contributing)
8. [License](#-license)

---

## 🔍 Overview

**Stealth Vault** is a high-security, multi-platform privacy ecosystem designed to hide and encrypt sensitive files, photos, videos, and applications. 

To the casual observer, Stealth Vault is a fully functional, premium calculator. However, entering a secret mathematical sequence or PIN code triggers the transition into an encrypted sandbox vault.

The repository is structured as a monorepo containing:
- **📱 Android App (`/app`)**: The native high-security Android application built with Kotlin, Clean Architecture, and Jetpack Security APIs.
- **💻 Web App (`/web`)**: A high-fidelity React + TypeScript companion app replicating the decoy calculator interface, equipped with animations, auto-locking, and storage mockup options.

---

## 🌟 Key Features

### 🧮 1. The Ultimate Decoy Calculator
- **Functional Math Engine**: Performs scientific calculation sequences (`sin`, `cos`, `log`, `π`, `e`, etc.) using `exp4j` (Android) and dynamic evaluation (Web).
- **Stealth Trigger**: Opens only when your secret passcode (e.g., `1337`) is entered and the `=` operator is pressed.
- **Premium Aesthetics**: Adheres to modern Material Design 3 guidelines (Android) and HSL-tailored glassmorphism styles (Web).

### 🔐 2. Cryptographic Security Sandbox
- **Hardware-Backed Encryption**: AES-256 GCM authenticated encryption utilizing keys sealed within the **Android KeyStore (HSM)**.
- **Gallery & System OS Isolation**: Files transferred to the vault are immediately stripped of metadata and hidden from system scanners, accessible only within the decrypted sandbox.
- **Encrypted Local Database**: Structured data management via Room DB fortified by **SQLCipher** database-level encryption.

### 📱 3. App Locker with "Fake Crash" Overlay
- **Background Interceptor**: Locks sensitive system or third-party apps (e.g., messaging, email, gallery).
- **Social Engineering Cover**: Instead of showing a normal password prompt, it displays a highly convincing **"Application Error: Force Close"** overlay. The intruder assumes the application crashed, while you can bypass it with a hidden gesture.

### 🕵️ 4. Active Anti-Intruder Protocols
- **Intruder Selfie**: Leverages Google's `CameraX` API to silently take a front-facing photograph whenever an incorrect PIN is entered.
- **Decoy PIN System**: Configure a secondary decoy PIN. When entered, it unlocks a dummy vault database, showing harmless files and decoy folder structures.
- **Emergency Wipe**: Wipes all encrypted credentials and file metadata if 5 consecutive failed access attempts occur.

---

## 🛠️ Architecture & Technology Stack

### 📱 Android Application
* **Development Language:** Kotlin
* **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture principles
* **UI/Theming:** Material 3 Components, Jetpack Navigation, custom themes
* **Database:** Room DB + SQLCipher
* **Security:** Android Jetpack Security, Android KeyStore (Hardware Security Module)
* **Dependency Injection:** Hilt
* **Camera APIs:** CameraX

### 💻 Web Companion
* **Development Language:** TypeScript
* **Framework:** React 18
* **Build System:** Vite
* **Animations:** Framer Motion (for smooth micro-interactions, layout morphing)
* **Icons:** Lucide React
* **Styling:** CSS Variables (custom HSL color palette, Glassmorphism, Dark mode default)

---

## 📁 Project Directory Structure

```text
Vault-Calculator/
├── app/                       # Android native application codebase
│   ├── src/
│   │   └── main/
│   │       ├── java/com/stealthvault/app/
│   │       │   ├── data/      # Repositories, Local DB (Room), Cryptography handlers
│   │       │   ├── di/        # Dependency injection configuration (Hilt)
│   │       │   ├── ui/        # UI components (Fake Calc, Lock Screen, Settings, Vault)
│   │       │   └── utils/     # Diagnostic utilities, String operations
│   │       └── res/           # UI Layout files, drawable resources, themes
│   └── build.gradle.kts       # Gradle module build configuration
├── web/                       # React + TS web companion application
│   ├── src/
│   │   ├── components/        # Decoy Calculator and Vault Sidebar components
│   │   ├── hooks/             # Custom React hooks (auto-lock triggers)
│   │   ├── App.tsx            # Application entry routing and viewport container
│   │   └── index.css          # Core CSS variables, typography, and utility classes
│   └── package.json           # Frontend dependencies and run scripts
└── settings.gradle.kts        # Root gradle build configuration
```

---

## 🚀 Getting Started

### Android App Setup

#### Prerequisites
- Android Studio Jellyfish (or higher)
- JDK 17
- Android SDK 34 (Upside Down Cake) or higher

#### Compilation Steps
1. Clone this repository locally:
   ```bash
   git clone https://github.com/Subhan-Haider/Stealth-Vault.git
   ```
2. Open Android Studio and select **File > Open**, navigating to the cloned root repository folder.
3. Allow Gradle to sync and download required dependencies.
4. Click **Run** or build the APK via:
   ```bash
   ./gradlew assembleDebug
   ```
5. Find your generated APK at: `app/build/outputs/apk/debug/app-debug.apk`.

### Web Companion Setup

#### Prerequisites
- Node.js (version 18.x or higher)
- npm or yarn

#### Execution Steps
1. Navigate to the web module directory:
   ```bash
   cd web
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Start the local Vite development server:
   ```bash
   npm run dev
   ```
4. Access the web decoy interface in your browser at `http://localhost:5173`.
5. Enter the passcode `1337` and press `=` to unlock the secure dashboard companion mock.

---

## 🛡️ Security Architecture

Stealth Vault relies on defense-in-depth principles:

1. **Isolation**: Encrypted files are stored within the application's private directory (`/data/user/0/com.stealthvault.app/files/`), making them inaccessible to standard file explorer utilities or malware lacking root access.
2. **Key Rotation**: Cryptographic keys are stored within the Android Keystore, isolated from the memory space of the main application. Decryption occurs on the fly inside memory only when active PIN credentials match.
3. **Decoy Isolation**: Room DB utilizes a separate database name and encryption key configuration for the decoy profile, ensuring the primary vault schema remains zero-knowledge even if the decoy is active.

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:
- Fork the repository and create your feature branch.
- Document any database schema updates.
- Keep the decoy interface styling clean and consistent.
- Submit a Pull Request describing your changes in detail.

---

## ⚖️ License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.
