## [3.0.0] - 17-05-2026

### 🚀 Major Release – Full App Rebuild

### ✨ Added

* Complete rewrite of Kingfisher Browser using **Kotlin + Jetpack Compose**
* Integrated **GeckoView engine** replacing traditional WebView for modern browsing performance
* Fully redesigned UI with modern Compose-based architecture
* New customizable top navigation bar (URL/search controls)
* Redesigned bottom navigation system with improved usability
* New loading progress indicator with smoother visual feedback
* Updated application icon and refreshed branding system
* Improved menu system with cleaner and more accessible layout
* Enhanced URL handling and smart input parsing system

---

### 🔄 Changed

* Migrated entire codebase from **Java → Kotlin**
* Replaced XML-based UI system with **Jetpack Compose UI**
* Replaced Android WebView engine with **Gecko engine (GeckoView)**
* Refactored app architecture for better modularity and scalability
* Improved navigation flow and state handling across the app
* Redesigned UI components for consistency and modern UX patterns

---

### ⚡ Improved

* Faster and smoother page rendering with Gecko engine
* Better UI responsiveness using Compose reactive state system
* Improved app startup performance and initialization flow
* Enhanced navigation stability (back/forward/refresh behavior)
* Optimized layout performance across different screen sizes
* More stable URL loading and page transition handling

---

### 🧩 Internal Improvements

* Clean architecture restructuring for long-term maintainability
* Separated UI, engine, and logic layers more clearly
* Improved state management for browser sessions
* Better lifecycle handling for browser engine integration
* Prepared foundation for future features (tabs, sync, extensions)

---

### 🛡️ Security Enhancements

* Strengthened Gecko security configuration baseline
* Improved browsing isolation and session safety
* Enhanced protection against unsafe URL handling
* Maintained privacy-first browsing foundation

---

### ⚠️ Breaking Changes

* Entire architecture changed (Java WebView → Kotlin + GeckoView)
* UI system fully migrated to Jetpack Compose (not XML-based anymore)
* Internal browser engine replaced (WebView compatibility removed)

---

## [1.0.0] - 20-04-2026

### ✨ Added

* First stable release of Kingfisher Browser
* Application launcher icon with adaptive icon support
* URL loader system for improved page loading handling
* Basic browser navigation system (back, forward, refresh)
* Core WebView-based browsing engine implementation
* Initial privacy-first browsing foundation (incognito-oriented design)

### 🔧 Improved

* Fixed app icon rendering and scaling across different Android devices
* Improved URL loading behavior for smoother navigation experience
* Enhanced WebView initialization stability on app launch
* Optimized UI layout consistency for address bar and controls
* Better handling of page reload and navigation state updates

### 🐛 Fixed

* Resolved incorrect or missing app icon display issues
* Fixed URL loading failures in certain web pages
* Fixed minor UI inconsistencies in browser toolbar
* Fixed initial launch loading behavior issues

### 🛡️ Security Enhancements

* Strengthened WebView security configuration (safe browsing baseline)
* Disabled unsafe file/content access in WebView
* Improved isolation of browsing session behavior for better privacy foundation

---


## [0.4.0] - 16-04-2026

### ✨ Added
- Incognito mode with privacy-first browsing session handling
- Bookmark system foundation with local storage support
- Local browsing history storage system
- Clear history functionality for user data control

### 🔧 Improved
- Refined search/address bar alignment for better UI consistency
- Reworked back/forward navigation logic for more stable browsing flow
- Enhanced navigation button behavior and state handling

### 🛡️ Security Enhancements
- Strengthened privacy protections in browsing sessions
- Improved WebView security configuration and restrictions
- Better isolation of incognito and normal browsing states

---

## [0.3.0] - 16-04-2026

### ✨ Added
- MaterialCardView-based address bar with IME action handling (keyboard search/go support)
- Real-time page loading progress tracking with dynamic refresh/stop toggle
- Decoupled UI state using `BrowserEngineCallback` for cleaner architecture separation
- Smart URL formatting: simplified display when idle, full URL on focus
- Navigation button state feedback (opacity-based active/inactive states)
- Hardware back button support for WebView navigation
- Preparation for privacy mode and tab system (foundation work)

---

## [0.2.0] - 16-04-2026

### ✨ Added
- Modular architecture structure (`ui/`, `web/`, `utils/`)
- HTTPS-only enforcement with cleartext traffic blocking
- Safe URL scheme validation (prevents invalid or unsafe navigation)
- Third-party cookie restrictions for improved privacy control
- Memory-safe WebView lifecycle handling (reduce leaks/crashes)
- Foundation setup for address bar and navigation system

---

## [0.1.0] - 08-04-2026

### 🎬 Project Setup

### ✨ Added

* Initial Android project setup
* Basic WebView implementation
* Load default homepage

---