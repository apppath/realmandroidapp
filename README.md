# Realm
Realm is a lightweight database that can replace both SQLite and ORM libraries in your Android projects. Compared to SQLite, Realm is faster and has lots of modern features, such as JSON support, a fluent API, data change notifications, and encryption support, all of which make life easier for Android developers.

## Step 1

Add the class path dependency to the project level build.gradle file.

```gradle
classpath "io.realm:realm-gradle-plugin:5.4.1"
```
Apply the realm-android plugin to the top of the application level build.gradle file.

```gradle
apply plugin: 'realm-android'
```
Before your application can synchronize with the Realm Object Server, it has to be enabled in your build file. Add this to the application’s build.gradle

```gradle
realm {
  syncEnabled = true;
}
```
