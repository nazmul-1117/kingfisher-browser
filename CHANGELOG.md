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