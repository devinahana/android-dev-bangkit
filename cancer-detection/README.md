# Cancer Detection Application

This is a **Cancer Detection Application** developed as part of my final project for the **Bangkit Mobile Development** Bootcamp's topic on "Applied Machine Learning for Android". The app leverages machine learning to classify images as either cancerous or non-cancerous with an associated confidence score. 

## Features

- **Cancer Detection**: Allows users to select an image from the gallery and classify it as either cancerous or non-cancerous. The app provides a confidence score for the classification, offering a measure of certainty in the result.
- **Cancer Related Articles**: Provides a list of articles related to cancer to help users better understand the condition and stay informed.

## Technologies Used

- **Kotlin**: The app is built using the Kotlin programming language.
- **TensorFlow Lite**: The app uses TensorFlow Lite to implement the model for classifying images as cancerous or non-cancerous.
- **Retrofit**: A powerful HTTP client library that simplifies the process of making API requests.
- **Gson**: A JSON library that helps with parsing and formatting JSON data for API configuration.
- **ViewModel**: Implements the ViewModel architecture component to handle data for the app and survive configuration changes.

## Getting Started

1. **Clone the repository**:
    ```bash
    git clone https://github.com/devinahana/simple-user-app.git
    ```
2. **Open cancer-detection folder** in Android Studio.

3. **Configure the app**:
    - You'll need to obtain a News API token and configure it in the build.gradle (Module :app) for the app to function properly.

4. **Run the app** on an emulator or physical device.

***

© Devina Hana - Bangkit Academy led by Google, Tokopedia, Gojek, & Traveloka