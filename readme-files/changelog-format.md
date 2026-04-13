# 📜 Changelog

All notable changes to **Kingfisher Browser** will be documented in this file.

This project follows **Semantic Versioning (SemVer)**:

* **MAJOR**: incompatible API changes
* **MINOR**: new features (backward-compatible)
* **PATCH**: bug fixes and improvements

Format is based on *Keep a Changelog*.

---

## [Unreleased]

### 🚀 Planned

* Tab management system
* Bookmark manager (local storage)
* Dark mode UI
* Basic ad-blocking engine
* Custom homepage
* Improved URL filtering

---

## [1.0.0] - 2026-04-13

### 🎉 Initial Release

### ✨ Added

* Basic WebView-based browser
* Address bar with URL loading
* Back and forward navigation
* Page refresh support
* Minimal clean UI

### 🔐 Security

* Disabled file access
* Disabled content access
* Disabled form data saving
* HTTPS-first browsing behavior
* Blocked non-http/https URL schemes

### 🕶️ Privacy

* Incognito-style browsing (no history storage)
* Cookies cleared on session
* Cache disabled
* DOM storage disabled
* No analytics or tracking

### 🧱 Architecture

* Modular package structure:

  * `ui`
  * `web`
  * `security`
  * `utils`

---

## [0.2.0] - 2026-04-10

### ✨ Added

* WebViewClient for controlled navigation
* Basic URL validation
* Improved page loading handling

### 🔐 Security

* Blocked unsafe URL schemes
* Restricted external intent handling

### 🧪 Improved

* Stability improvements
* Cleaner WebView configuration

---

## [0.1.0] - 2026-04-08

### 🎬 Project Setup

### ✨ Added

* Initial Android project setup
* Basic WebView implementation
* Load default homepage

---

## 📌 Versioning Strategy

* Start at **0.x.x** → development phase
* Release **1.0.0** → first stable version
* Increment:

  * **PATCH (1.0.1)** → bug fixes
  * **MINOR (1.1.0)** → new features
  * **MAJOR (2.0.0)** → big changes / breaking updates

---