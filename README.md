# DoneTik

A modern Todo App built with **Kotlin** and **Jetpack Compose**, featuring:

---

## Features

- [x] Authentication (Google/Email via Firebase)
- [x] CRUD Operations with FireStore
- [ ] Leaderboard
- [ ] Teams
- [ ] Profile
- [ ] Search

---

## Tech Stack & Dependencies

- [**Kotlin**](https://kotlinlang.org/docs/home.html)
- [**Jetpack Compose**](https://developer.android.com/jetpack/compose)
- [**Firebase**](https://firebase.google.com/docs)
- [Authentication](https://firebase.google.com/docs/auth)
- [Firestore Database](https://firebase.google.com/docs/firestore)
- [**Credential Manager**](https://developer.android.com/training/sign-in/credential-manager)
- [**Jetpack DataStore**](https://developer.android.com/topic/libraries/architecture/datastore)
- [**Koin (Dependency Injection)**](https://insert-koin.io/)
- [**Lottie for Compose**](https://airbnb.io/lottie/#/android)
- [**Kotlinx Serialization**](https://github.com/Kotlin/kotlinx.serialization)
- [**Coil (Image Loading)**](https://coil-kt.github.io/coil/)

---

## Architecture: MVI

The app follows the **Model-View-Intent (MVI)** pattern:

- **Model**: Represents UI state  
- **View**: Renders state to UI  
- **Intent**: Captures user actions and triggers state updates
---

## Installation Instructions

1. **Clone the repo**

   ```bash
   git clone https://github.com/slowburn-404/DoneTik.git
   cd DoneTik
   ./gradlew assembleDebug
   ```

2. **Set up Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project
   - Enable **Authentication** (Email/Password & Google)
   - Add **Cloud Firestore**
   - Download `google-services.json` and place it in the `app/` directory

3. **Open in Android Studio** and run the app.

4. Set environment variables in `local.properties` for `WEB_CLIENT_ID` created from [Google Cloud Console](https://console.cloud.google.com/apis/credentials).

---

## Contribution Guidelines

We welcome contributions!

1. Fork the repository  
2. Create a branch: `git checkout -b feature/your-feature-name`  
3. Commit your changes: `git commit -m "Add feature"`  
4. Push: `git push origin feature/your-feature-name`  
5. Open a Pull Request  

### Please:
- Follow Kotlin & Compose best practices  
- Use the MVI pattern  
- Include docs/screenshots if UI-related  

---

## License

This project is licensed under the [MIT License](LICENSE.txt).
