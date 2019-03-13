# Android Base Project Kotlin

## Architecture (MVP)

- Do not add bussiness rules in the `View`. It should only call the Android API to perform UI actions.
- Do not save state in the `View`.
- Every click/tab/animation listener should call the `Presenter` to decide the next action.
- Write unit tests for the `Presenter` using `Mockito` and `Roboletric`. We are not writing unit tests for the `View`.
- Use `RxJava` for asynchronous calls.
- Use `Dagger` for dependency injection.
- Try to use `ConstraintLayout` as your root view.
- Use `ThreeThen` API to handle dates.

## Flavors:

- **Dev:** Does not send crashes or logs to `Firebase`.
- **Staging:** Does send crashes and logs to `Firebase staging`.
- **Prod:** Does send crashes and logs to `Firebase prod`.  Does not show logs in logcat.

Notes:
- Configuration paramaters for each build variant are defined in `build.gradle` file.
- `Dev` and `Staging` share the same `google_maps_api.xml` and `google-services.json` files. `Prod` should have a different `Firebase` project.
- `Staging` and `Prod` allow you to send logs to `Firebase Crashlytics` when using `Timber.e(e: Exception)`.

## Lint

We are using [ktlint](https://github.com/shyiko/ktlint) as our lint tool.
Code style configuration file is stored in `codeStyle/kotlinCodeStyle.xml`, you can import it in your IDE.

- It runs before each build (local builds included).
- If it fails, reports are stored in `app/build/ktlint.xml`. You can fix the errors manually or by running `./gradlew ktlintFormat`.

## Deployment

We are using [Bitrise](https://app.bitrise.io/app/8d3a8f31eedbb9da#/builds) as our Continuos Integration tool.
Signing credentials to upload the app to the Play Store are stored [there](https://app.bitrise.io/app/8d3a8f31eedbb9da#/workflow) (Worflows / Code Signing)

Workflows:
- **Primary:** It runs for each push and ensures that `./gradlew testDevDebugUnitTest` does not fail.
- **Deploy:** It runs for each merge to master and updates version code, version name and outputs a release APK file. To download the signed APK and mappings file tap on the corresponding build and go to `Apps & Artifacts`.

**TODO:** We could automatically upload the APK to Play Store (currently we are doing it manually).

## MVP Template

We added a template to make easier the creation of new MVP Activities, follow these steps to add it to your Android Studio installation:
1. Copy the folder `MVPTemplate` included in this project to your Android Studio templates folder (`/Applications/Android Studio/Contents/plugins/android/lib/templates/other`)
2. Restart Android Studio
3. From Android Studio right click on the folder where you want to add a new `MVP Activity`
4. Go to `New/Other/MVP Template` and fill the fields

### Looking for a JAVA Version ?
https://github.com/LateralView/android-base-project

### Extra

[Here](https://github.com/LateralView/android-base-project/wiki) you will find useful links and documentation about Android Development and our coding standars at Lateral View.

For any suggestions or if you want to join our team please contact us via [android@lateralview.net](mailto:android@lateralview.net)

Happy codding!

Developed by the [Lateral View](https://lateralview.co) team.

