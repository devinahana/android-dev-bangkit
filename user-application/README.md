# Simple User Application

This is a **Simple User Application** developed as part of my final project for the **Bangkit Mobile Development** Bootcamp's topic on "Learning Android Application Fundamentals". This Android app is developed in Kotlin and it allows you to interact with GitHub's user API. You can explore and search users, view user details including followers and following lists, and mark users as favorites.

## Features

- **List All Users**: Browse through a list of all users.
- **Search Users**: Search for specific users by username.
- **User Detail**: View detailed information about each user, including their followers and following lists.
- **Favorite Users**: Add users to your favorites list for easy access.

## Technologies Used

- **Kotlin**: The app is built using the Kotlin programming language.
- **ViewModel**: Implements the ViewModel architecture component to handle data for the app and survive configuration changes.
- **SQLite & Room**: Uses SQLite and Room to manage and store the app's local database.
- **Retrofit**: A powerful HTTP client library that simplifies the process of making API requests.
- **Gson**: A JSON library that helps with parsing and formatting JSON data for API configuration.
- **DataStore**: Used to store preference settings such as dark mode or light mode.

## Getting Started

1. **Clone the repository**:
    ```bash
    git clone https://github.com/devinahana/simple-user-app.git
    ```
2. **Open user-application folder** in Android Studio.

3. **Configure the app**:
    - You'll need to obtain a GitHub API token and configure it in the build.gradle (Module :app) for the app to function properly

4. **Run the app** on an emulator or physical device.

***

© Devina Hana - Bangkit Academy led by Google, Tokopedia, Gojek, & Traveloka