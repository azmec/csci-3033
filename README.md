# Meal Manual
Meal Manual is an "cookbook" Android application targeted for devices
supporting API 31. The application provides facilities to view a catalog of
recipes serviced by [Spoonacular](https://spoonacular.com), create and manage a
shopping list, index available ingredients, favorite recipes, and create
recipes. This project was developed to fulfill the course requirements of CSCI
3033, "Computer Languages: Java".

## Instructions
### Installation
1. Download, install, and launch the latest version of [Android Studio](https://developer.android.com/studio).
2. If prompted, ensure the Android emulator installed on your system is
   compatible with API 31.
2. Select `File`, `New`, and `Project from Version Control`.
3. Copy and paste the URL for this repository into the `URL` field in the newly
   opened window.
4. Click the `Clone` button in the bottom-right.
5. If asked, "trust" the project by clicking on the `Trust Project` button.

### Initialization
1. Let Gradle index the application's files.
2. You **will** encounter a compile-time error along the lines `apiKey cannot
   be null`. This is expected.
3. Navigate to the file `local.properties` and append the following line to the
   end of the file.

   ```txt
   API_KEY = "$KEY"
   ```

   Where `$KEY` is replaced with the API key you were provided _or_ an API key
   of your own. _Note that you can provide any string of characters instead, but all web queries
   will silently fail._
4. Select `File` and `Sync Project with Gradle Files` to continue the
   initialization process.
5. Select `Build` and `Make Project` to build the project.

### Demoing
1. Select `Run` and `Run 'app'`.
2. Allow the Android Virtual Device (AVD) to begin and the application to be
   installed on the emulator. Depending on your machine, this can take some
   time.
3. Depending on your machine, the AVD may behave inconsistently. For example,
   running the application may start the AVD and display the emulator but not
   visibly launch the application. In these cases, you have a few options:
    1. If the `Run` button is available, click it _again_ to launch the
       application.
    2. If clicking the `Run` button does not work, shut down the AVD and
       attempt to run the application again.
    3. If neither of the above work, shut down the AVD and wipe the user data
       from the emulator. Attempt to run the application again.
4. The application should open on the "Recipe" page, filled with on-disk recipes and random recipes
   queried from the web API. The bottom of the screen should display a toolbar exposing the
   remaining "Grocery", "Pantry", "Liked", and "Add Recipes" pages. The application is now free to
   demo. _Note that on-disk recipes may not be initial displayed the first time the application is
   launched due to inconsistent behavior in the AVD._
