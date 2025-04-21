# SafeBite 🐍🚑
> **Empowering Rapid Response, AI Species Identification, and Life-Saving First-Aid for Snakebite Emergencies.**

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![TensorFlow Lite](https://img.shields.io/badge/ML-TensorFlow%20Lite-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)](https://www.tensorflow.org/lite)
[![Gemini](https://img.shields.io/badge/AI-Gemini%201.5%20Flash-4285F4?style=for-the-badge&logo=google&logoColor=white)](https://ai.google.dev/)
[![Google Maps](https://img.shields.io/badge/Maps-Google%20Maps%20SDK-4285F4?style=for-the-badge&logo=google-maps&logoColor=white)](https://developers.google.com/maps)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)

---

## 📌 Overview

**SafeBite** is a comprehensive Android application engineered to address the critical golden hour of snakebite treatment. In snakebite incidents, panic and lack of information—such as snake venomousness, nearest antivenom facilities, and correct first-aid protocol—often lead to fatalities.

SafeBite bridges this gap by offering:
- **Instant One-Touch SOS dispatch** with exact GPS coordinates and reverse-geocoded addresses via SMS and WhatsApp.
- **On-Device AI Species Classification** using quantized MobileNet TensorFlow Lite models.
- **Nearest Hospital Discovery & Live Navigation** leveraging TomTom POI and Google Maps SDK.
- **Community Snake Rescue Network** to connect users directly with wildlife rescuers and catchers.
- **AI-Powered Emergency Chatbot** driven by Google's Gemini 1.5 Flash model for instantaneous emergency guidance.

---

## ✨ Key Features

### 🚨 1. One-Touch Emergency SOS
- Instant automated dispatch of distress messages via **SMS** (`SmsManager`) and **WhatsApp Intent**.
- Embeds exact GPS latitude, longitude, and clickable Google Maps navigation links.
- Uses Android `Geocoder` for reverse geocoding to resolve the human-readable physical address.
- Sends distress alerts directly to pre-configured primary and secondary emergency contacts.

### 🔬 2. AI Snake Species Identification
- **On-Device Inference:** Uses a quantized TensorFlow Lite model (`mobilenet_v1_1.0_224_quant.tflite`) for fast, offline-capable image inference.
- **Camera & Gallery Input:** Seamlessly handles Camera captures and Gallery selections with adaptive Android P+ `ImageDecoder` and ARGB_8888 bitmap normalisation.
- **Taxonomy Knowledge Base:** Automatically queries Wikipedia and iNaturalist APIs via `OkHttp` and `Jsoup` HTML parsing, filtering out disambiguation pages to retrieve real-time species facts, danger level, and venom information.

### 🏥 3. Nearest Hospital & Antivenom Locator
- Searches for emergency medical facilities within a 60km radius using the **TomTom POI Search API**.
- Calculates real-time distance using `Location.distanceBetween` and dynamically sorts hospitals by proximity.
- Shimmer skeleton loading screens for smooth UI feedback during cold GPS starts and network calls.

### 🗺️ 4. Real-Time Hospital Navigation & Route Tracing
- Integrates Google Maps SDK (`SupportMapFragment`) with dual-marker placement (User & Hospital).
- Draws dynamic geodesic **Polylines** following the route as the user travels.
- Built-in one-tap intent to launch turn-by-turn navigation in Google Maps.

### 🐍 5. Snake Rescue Network
- Finds verified local snake catchers and wildlife rescue responders within an 80km radius.
- Direct dial action buttons to immediately dispatch expert handlers to the scene.

### 🩺 6. Evidence-Based First-Aid Guidance
- Comprehensive dos and don'ts curated to prevent common harmful practices (e.g., tourniquets, incisions).
- Visual symptoms checklist with photographic diagrams to help diagnose neurotoxic vs. hemotoxic bites.
- Embedded video tutorial adapter for instructional first-aid demonstration.

### 🤖 7. Gemini AI First-Aid Consultation ChatBot
- Real-time emergency assistant powered by the **Google Gemini 1.5 Flash API**.
- Conversational triage advice tailored to symptoms and bite contexts.
- Clean chat bubble interface with message history preservation.

---

## 🛠️ Architecture & Tech Stack

SafeBite follows modern Android development practices adhering to the **MVVM (Model-View-ViewModel)** architectural pattern:

- **Language:** Kotlin (100%)
- **Target SDK:** Android 34 (Android 14) | **Min SDK:** Android 27 (Android 8.1)
- **Architecture:** MVVM, ViewBinding, Kotlin Coroutines, LiveData
- **Machine Learning & Vision:**
  - `org.tensorflow:tensorflow-lite:2.8.0`
  - `org.tensorflow:tensorflow-lite-support:0.3.1`
  - `com.google.mlkit:image-labeling:17.0.7`
- **Location & Mapping:**
  - Google Play Services Location (`FusedLocationProviderClient`, `LocationRequest.Builder`)
  - Google Maps Android SDK (`SupportMapFragment`, `PolylineOptions`)
  - TomTom POI Search REST API
- **Networking & Scraping:**
  - Retrofit 2 & Gson Converter
  - OkHttp 3 & Logging Interceptor
  - Jsoup 1.16.1 (HTML parser for taxonomy extraction)
- **Cloud & Persistence:**
  - Firebase Realtime Database
  - SharedPreferences for cached session & emergency contacts
- **UI / UX Design:**
  - Responsive layout scaling: `com.intuit.sdp` & `com.intuit.ssp`
  - Animations: Airbnb Lottie & custom shimmer drawables
  - Typography: Custom `Agbalumo` heading font

---

## 📂 Project Structure

```
SafeBite2/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   ├── mobilenet_v1_1.0_224_quant.tflite   # Quantized ML model
│   │   │   │   └── labels_mobilenet_quant_v1_224.txt    # Species label set
│   │   │   ├── java/com/rahul/safebite/
│   │   │   │   ├── chatBot/                             # Gemini AI Chatbot service & UI
│   │   │   │   ├── ExtraClasses/                        # Adapters, models & API clients
│   │   │   │   ├── SkeletonView/                        # Shimmer loading adapters
│   │   │   │   ├── SnakeInfo/                           # iNaturalist encyclopedia & ViewModel
│   │   │   │   ├── SnakeRescue/                         # TomTom snake catcher discovery
│   │   │   │   ├── SpeciesIdentification/               # TFLite classifier & Wikipedia scraper
│   │   │   │   ├── FirstAidActivity.kt                  # First aid guide & video adapter
│   │   │   │   ├── HomeActivity.kt / HomeFragment.kt    # Main dashboard & SOS dispatch
│   │   │   │   ├── LocationViewModel.kt                 # FusedLocation updates lifecycle
│   │   │   │   ├── NearestHospitalActivity.kt           # Hospital search & distance sorting
│   │   │   │   ├── ViewHospitalActivity.kt              # Google Maps live route & polyline
│   │   │   │   └── SplashActivity.kt / LoginActivity.kt # Authentication flow
│   │   │   └── res/
│   │   │       ├── drawable/                            # Custom XML vector shapes & icons
│   │   │       ├── layout/                              # 26 clean XML layouts
│   │   │       ├── menu/                                # Bottom navigation bar
│   │   │       └── values/                              # Colors, typography & themes
│   │   └── test/ / androidTest/                         # Unit and UI instrumentation tests
│   ├── build.gradle.kts                                 # App-level Gradle build configuration
│   └── proguard-rules.pro                               # R8/ProGuard rules for TFLite & Retrofit
├── build.gradle.kts                                     # Root Gradle script
└── settings.gradle.kts                                  # Module repositories & plugins
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or newer.
- **JDK 17** configured in Android Studio.
- An Android device or Emulator running **API 27+** with Google Play Services enabled.

### Setup Instructions

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/rahulambhore394/Safe-Bite.git
   cd Safe-Bite
   ```

2. **Add Configuration Keys:**
   - **Google Maps & Services:**
     Ensure `app/google-services.json` is present and configure your Google Maps API key inside `app/src/main/AndroidManifest.xml`:
     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="YOUR_GOOGLE_MAPS_API_KEY" />
     ```
   - **TomTom API:**
     Add your TomTom API key in `NearestHospitalActivity.kt` and `TomTomApi.kt`.
   - **Gemini API:**
     Configure your Gemini API key in `ChatBotActivity.kt`.

3. **Build the Project:**
   Open the project in Android Studio and let Gradle sync. Build the debug APK with:
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run on Device:**
   Connect your physical device or start an emulator and run:
   ```bash
   ./gradlew installDebug
   ```

---

## 🔒 Permissions Used

| Permission | Purpose |
| :--- | :--- |
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Required to find nearest hospitals, dispatch SOS coordinates, and render live route tracking. |
| `SEND_SMS` | Dispatches instantaneous emergency SOS SMS to primary and secondary emergency contacts. |
| `CAMERA` | Captures images of snakes for real-time AI species identification. |
| `READ_MEDIA_IMAGES` / `READ_EXTERNAL_STORAGE` | Allows selecting snake photographs from the gallery for classification. |
| `INTERNET` | Fetches hospital locations, Wikipedia taxonomy, and Gemini AI first-aid responses. |

---

## 👨‍💻 Author

**Rahul Ambhore**  
- **GitHub:** [@rahulambhore394](https://github.com/rahulambhore394)  
- **Email:** [rahulambhore394@gmail.com](mailto:rahulambhore394@gmail.com)  

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.
