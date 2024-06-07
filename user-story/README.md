# Userstory
**UserStory** is developed as part of my final project for the **Bangkit Mobile Development** Bootcamp's topic on "Intermediate Android Development". This Android app is developed in Kotlin and it allows users to interact with the [Story API](https://story-api.dicoding.dev/v1). Authentication is required to use the app's features, including uploading new stories, exploring stories from other users, and viewing stories with location information.

## Features

- **User Authentication**: Login or register to access the app's features.
- **Upload New Story**: Share your own stories by uploading image, description, and even your current location.
- **List All Stories**: Browse through a list of all users' stories.
- **Story Detail**: View detailed information about each story.
- **Story Maps**: View marked maps that show users' stories with location information. 

## Technologies Used

- **Kotlin**: The app is built using the Kotlin programming language.
- **ViewModel**: Implements the ViewModel architecture component to handle data for the app and survive configuration changes.
- **SQLite & Room**: Uses SQLite and Room to manage and store the app's local database.
- **Retrofit**: A powerful HTTP client library that simplifies the process of making API requests.
- **Gson**: A JSON library that helps with parsing and formatting JSON data for API configuration.
- **Google Maps API**: An API provided by Google to integrate and display interactive maps in the app.
- **Paging 3**: A library from Android that simplifies data pagination, optimizing resource usage and performance.
- **Remote Mediator**: Component of the Paging 3 library that manages the interaction between local data storage and remote data sources.


## Getting Started

1. **Clone the repository**:
    ```bash
    git clone https://github.com/devinahana/android-dev-bangkit.git
    ```
2. **Open *user-story* folder** in Android Studio.

3. **Run the app** on an emulator or physical device.

***

© Devina Hana - Bangkit Academy
