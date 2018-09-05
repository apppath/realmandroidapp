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
## Realm Config File

```java

package com.example.basicprogramming.friendslistrealms.configs;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("frienddb.realm").build();
        Realm.setDefaultConfiguration(configuration);
    }
}

```

## Realm Table Model File

```java

package com.example.basicprogramming.friendslistrealms.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Friends extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String email;
    private String profile;
    private Double salary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}


```
