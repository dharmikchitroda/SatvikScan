
# SatvikScan

**Scan packaged food. Understand ingredients. Follow your dietary preferences.**

SatvikScan is an Android app that helps people following **Jain, Swaminarayan, and other personal dietary practices** check packaged-food ingredients more conveniently.

> **Status:** Google Play Closed Testing — not publicly released yet.

## Why I Built It

The idea came to me while sitting in my college canteen. As a Swaminarayan, I often had to read packaged-food labels to check for ingredients such as onion and garlic. It was repetitive and time-consuming.

I wanted to make this everyday task easier for people with ingredient-based dietary restrictions. That real-life problem became **SatvikScan**.

I spent around **4–5 months** planning, building, and refining the app.

## Features

- **Dietary Profiles:** Choose Jain, Swaminarayan, or create a custom avoid list.
- **Barcode Scanning:** Scan packaged products using the camera.
- **Text Recognition:** Read ingredient text from product labels.
- **Ingredient Assessment:** See **SAFE**, **AVOID**, or **UNCERTAIN** based on available information and selected preferences.
- **Ingredient Guides:** Refer to dietary ingredient information in the app.
- **Scan History:** Review previously checked products.
- **Languages:** English, Hindi, and Gujarati resources.

## Screenshots

<p align="center">
  <img src="Untitled%20Design%20(16).png" width="125" alt="SatvikScan screenshot 1" />
  <img src="Untitled%20Design%20(17).png" width="125" alt="SatvikScan screenshot 2" />
  <img src="Untitled%20Design%20(19).png" width="125" alt="SatvikScan screenshot 3" />
  <img src="Untitled%20Design%20(18).png" width="125" alt="SatvikScan screenshot 4" />
  <img src="Untitled%20Design%20(20).png" width="125" alt="SatvikScan screenshot 5" />
  <img src="Untitled%20Design%20(22).png" width="125" alt="SatvikScan screenshot 6" />
  <img src="Untitled%20Design%20(21).png" width="125" alt="SatvikScan screenshot 7" />
</p>

## Tech Stack

| Area | Technologies |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM |
| Camera | CameraX |
| Barcode & Text Recognition | Google ML Kit |
| Networking | Retrofit, Gson |
| Local Database | Room |
| Cloud | Firebase Firestore, Firebase Storage |
| Navigation | Navigation Compose |
| Build | Gradle Kotlin DSL, KSP |

## How It Works

1. Select Jain, Swaminarayan, or Custom preferences.
2. Scan a product barcode or use ingredient text recognition.
3. The app checks available ingredient information against your preferences.
4. Review the result and ingredients that may need attention.

## Getting Started

### Requirements

- Android Studio
- Android SDK with API 36 compile support
- Android 8.0 (API 26) or later
- Firebase configuration

### Clone the Repository

```bash
git clone https://github.com/dharmikchitroda/SatvikScan.git
cd SatvikScan
```

Open the project in Android Studio and allow Gradle to sync.

### Build

**Windows**
```bat
gradlew.bat assembleDebug
```

**macOS/Linux**
```bash
./gradlew assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/
```

## Important Note

SatvikScan is an **ingredient-checking aid**, not a food-certification system or a guarantee that a product meets every user's dietary requirements. Product information may be incomplete or outdated, and OCR can make mistakes. Always check the physical packaging and contact the manufacturer if an ingredient is unclear.

## Development Journey

SatvikScan is an individual Android project developed over approximately **4–5 months**. It gave me practical experience with Android UI development, camera integration, ML Kit, API integration, local storage, and Firebase.

The app is currently in **Google Play Closed Testing**, where I am collecting feedback and refining the product before a possible public release.

---

**Built with Kotlin and Jetpack Compose to make ingredient checking more convenient.**
