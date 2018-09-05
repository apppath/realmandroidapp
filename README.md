# Realm
Realm is a lightweight database that can replace both SQLite and ORM libraries in your Android projects. Compared to SQLite, Realm is faster and has lots of modern features, such as JSON support, a fluent API, data change notifications, and encryption support, all of which make life easier for Android developers.
more info visit https://realm.io/docs/java/latest/

# App Images

![alt text](https://github.com/apppath/realmandroidapp/blob/master/list-of-all-friends.png){:height="300px" width="300px"}
![alt text](https://github.com/apppath/realmandroidapp/blob/master/list-of-all-friends.png){:height="300px" width="300px"}
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
## Realm Configuration RealmConfig.java file

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

## Realm Table Model Friends.java file

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
## Insert record in Realm AddActivity.java file

```java

package com.example.basicprogramming.friendslistrealms.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basicprogramming.friendslistrealms.R;
import com.example.basicprogramming.friendslistrealms.model.Friends;

import java.util.UUID;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class AddActivity extends AppCompatActivity {

    private EditText name, email, profile, salary;
    private Realm myRealm;
    private RealmAsyncTask realmAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        myRealm = Realm.getDefaultInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.full_name_edit_text);
        email = findViewById(R.id.email_id_edit_text);
        profile = findViewById(R.id.profile_edit_text);
        salary = findViewById(R.id.salary_edit_text);
    }

    public void addFriends(View view) {

        final String id = UUID.randomUUID().toString();
        final String full_name = name.getText().toString().trim();
        final String email_id = email.getText().toString().trim();
        final String profiles = profile.getText().toString().trim();
        final String salarys = salary.getText().toString();

        if (full_name.isEmpty()) {
            Toasty.info(AddActivity.this,
                    "Full name is require", Toast.LENGTH_LONG).show();
            return;
        }

        if (email_id.isEmpty()) {
            Toasty.info(AddActivity.this,
                    "Email id is require", Toast.LENGTH_LONG).show();
            return;
        }

        if (profiles.isEmpty()) {
            Toasty.info(AddActivity.this,
                    "Profile is require", Toast.LENGTH_LONG).show();
            return;
        }

        if (salarys.isEmpty()) {
            Toasty.info(AddActivity.this,
                    "Salary id is require", Toast.LENGTH_LONG).show();
            return;
        }

        realmAsyncTask = myRealm.executeTransactionAsync(new Realm.Transaction() {
               @Override
               public void execute(Realm realm) {
                  Friends friends = realm.createObject(Friends.class, id);
                  friends.setName(full_name);
                  friends.setEmail(email_id);
                  friends.setProfile(profiles);
                  friends.setSalary(Double.parseDouble(salarys));
           }
      },
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toasty.success(AddActivity.this,
                                "Friend insert successufully", Toast.LENGTH_LONG).show();

                        name.setText("");
                        email.setText("");
                        profile.setText("");
                        salary.setText("");
          }
     }
                ,
                new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toasty.error(AddActivity.this,
                                "Error inserting record", Toast.LENGTH_LONG).show();
          }
});
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}

```

## AddActivity layout activity_add.xml file

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".app.AddActivity">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:fontFamily="monospace"
        android:text="Add New Friends In Realm DB"
        android:textAlignment="center"
        android:textSize="20dp"
        android:textStyle="bold" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:layout_margin="10dp"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Full Name"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/full_name_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Email Id"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/email_id_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Profile"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/profile_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Salary"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/salary_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="addFriends"
            android:text="Add Friends"
            tools:ignore="OnClick" />

    </LinearLayout>

</RelativeLayout>

```

## Display record in Realm MainActivity.java File

```java

package com.example.basicprogramming.friendslistrealms.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.basicprogramming.friendslistrealms.R;
import com.example.basicprogramming.friendslistrealms.adapter.FriendAdapter;
import com.example.basicprogramming.friendslistrealms.model.Friends;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm myRealm;
    private RealmResults<Friends> realmResults;

    private FriendAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRealm = Realm.getDefaultInstance();
        getDataList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting_menu:
                Toasty.info(MainActivity.this,
                        "Setting Menu",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.add_menu:
                startActivity(new Intent(MainActivity.this, AddActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void getDataList() {

        realmResults = myRealm.where(Friends.class).findAll();

        recyclerView = findViewById(R.id.my_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FriendAdapter(MainActivity.this, myRealm, realmResults);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}


```
## MainActivity layout activity_main.xml file

```xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="10dp"
    tools:context=".app.MainActivity">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/my_recycler_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</LinearLayout>

```

## Recyclerview Adapter FriendAdapter.java file

```java
package com.example.basicprogramming.friendslistrealms.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicprogramming.friendslistrealms.R;
import com.example.basicprogramming.friendslistrealms.app.EditFriendActivity;
import com.example.basicprogramming.friendslistrealms.model.Friends;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {

    private Context context;
    private Realm realm;
    private RealmResults<Friends> realmResults;
    private LayoutInflater inflater;
    private Friends friends;

    public FriendAdapter(Context context, Realm realm, RealmResults<Friends> friends) {

        this.context = context;
        this.realm = realm;
        this.realmResults = friends;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public FriendHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.friends_list_layout, null);
        FriendHolder holder = new FriendHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendHolder viewHolder, int position) {
        friends = realmResults.get(position);
        viewHolder.boundData(friends, position);
        viewHolder.setListener();
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public class FriendHolder extends RecyclerView.ViewHolder {
        private Integer position;
        private TextView full_name, email_id, profile_detail, salary;
        private ImageView editImage, deleteImage;

        public FriendHolder(View itemView) {
            super(itemView);
            full_name = itemView.findViewById(R.id.full_name_text_view);
            email_id = itemView.findViewById(R.id.email_id_text_view);
            profile_detail = itemView.findViewById(R.id.profile_text_view);
            salary = itemView.findViewById(R.id.salary_text_view);

            editImage = itemView.findViewById(R.id.edit_image_view);
            deleteImage = itemView.findViewById(R.id.delete_image_view);
        }

        public void boundData(Friends friends, Integer position) {
            this.position = position;

            full_name.setText(friends.getName());
            email_id.setText(friends.getEmail());
            profile_detail.setText(friends.getProfile());
            salary.setText("Rupees " + friends.getSalary());

        }

        public void setListener() {

            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, EditFriendActivity.class);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realmResults.deleteFromRealm(position);
                            Toasty.error(context,
                                    "Delete Friend From List",
                                    Toast.LENGTH_LONG).show();
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, realmResults.size());
                        }
                    });

                }
            });

            full_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    friends = realmResults.get(position);

                    Dialog dialog = new Dialog(context, R.style.appDialog);
                    dialog.setTitle("Friend Profile Detail");
                    dialog.setContentView(R.layout.dialog_list);
                    dialog.setCanceledOnTouchOutside(true);

                    TextView tv1 = dialog.findViewById(R.id.full_name_detail);
                    TextView tv2 = dialog.findViewById(R.id.email_id_detail);
                    TextView tv3 = dialog.findViewById(R.id.profile_detail);
                    TextView tv4 = dialog.findViewById(R.id.salary_detail);

                    tv1.setText("Full name \n\n" + friends.getName() + "\n");
                    tv2.setText("Email id \n\n" + friends.getEmail() + "\n");
                    tv3.setText("Profile \n\n" + friends.getProfile() + "\n");
                    tv4.setText("Rupees \n\n" + friends.getSalary());

                    dialog.show();

                }
            });

        }
    }
}


```
## Recyclerview Adapter layout friends_list_layout.xml file

```xml

<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="8dp"
    app:cardElevation="5dp"
    app:cardUseCompatPadding="true">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp">


        <TextView
            android:id="@+id/full_name_text_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:fontFamily="@font/arsenal_bold"
            android:text="@string/full_name"
            android:textColor="@color/secondary_text"
            android:textSize="20sp" />

        <TextView
            android:id="@+id/email_id_text_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/full_name_text_view"
            android:layout_marginTop="10dp"
            android:fontFamily="@font/arsenal"
            android:text="@string/email_id"
            android:textColor="@color/secondary_text"
            android:textSize="17sp" />

        <TextView
            android:id="@+id/profile_text_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/email_id_text_view"
            android:layout_marginTop="10dp"
            android:fontFamily="@font/arsenal"
            android:text="@string/profile_details"
            android:textColor="@color/secondary_text"
            android:textSize="17sp" />

        <TextView
            android:id="@+id/salary_text_view"
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:layout_below="@id/profile_text_view"
            android:layout_alignParentEnd="true"
            android:layout_marginTop="10dp"
            android:fontFamily="@font/arsenal"
            android:text="@string/salary_show"
            android:textAlignment="viewEnd"
            android:textColor="@color/secondary_text"
            android:textSize="17sp" />

        <ImageView
            android:id="@+id/edit_image_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@id/salary_text_view"
            android:layout_marginTop="10dp"
            android:src="@android:drawable/ic_input_add" />

        <ImageView
            android:id="@+id/delete_image_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_below="@id/salary_text_view"
            android:layout_alignParentRight="true"
            android:layout_marginTop="10dp"
            android:src="@android:drawable/ic_delete" />

    </RelativeLayout>


</android.support.v7.widget.CardView>

```
## Update record in Realm EditFriendActivity.java file

```java
package com.example.basicprogramming.friendslistrealms.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basicprogramming.friendslistrealms.R;
import com.example.basicprogramming.friendslistrealms.model.Friends;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class EditFriendActivity extends AppCompatActivity {

    private EditText full_name, email_id, profile_detail, salary_detail;
    private Realm myRealm;
    Bundle bundle;
    int position;
    private Friends friends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        bundle = getIntent().getExtras();
        if (bundle != null)
            position = bundle.getInt("position");

        full_name = findViewById(R.id.edit_full_name_edit_text);
        email_id = findViewById(R.id.edit_email_id_edit_text);
        profile_detail = findViewById(R.id.edit_profile_edit_text);
        salary_detail = findViewById(R.id.edit_salary_edit_text);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myRealm = Realm.getDefaultInstance();
        RealmResults<Friends> realmResults = myRealm.where(Friends.class).findAll();
        friends = realmResults.get(position);
        setupFriends(friends);
    }

    public void setupFriends(Friends friends) {

        full_name.setText(friends.getName());
        email_id.setText(friends.getEmail());
        profile_detail.setText(friends.getProfile());
        salary_detail.setText("" + friends.getSalary());
    }

    public void editFriend(View view) {

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                friends.setName(full_name.getText().toString().trim());
                friends.setEmail(email_id.getText().toString().trim());
                friends.setProfile(profile_detail.getText().toString().trim());
                friends.setSalary(Double.parseDouble(salary_detail.getText().toString()));

                Toasty.success(EditFriendActivity.this,
                        "Update Friend Info Successfully",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditFriendActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}

```
## EditFriendActivity layout activity_edit_friend.xml file

```xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".app.EditFriendActivity">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:fontFamily="monospace"
        android:text="Update Friend In Realm DB"
        android:textAlignment="center"
        android:textSize="20dp"
        android:textStyle="bold" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:layout_margin="10dp"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Full Name"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/edit_full_name_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Email Id"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/edit_email_id_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Profile"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/edit_profile_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="Salary"
            android:textSize="16sp"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/edit_salary_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:onClick="editFriend"
            android:text="Edit Friends"
            tools:ignore="OnClick" />

    </LinearLayout>

</RelativeLayout>

```

### style.xml file 

```xml

<resources>

    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

    <style name="appDialog" parent="android:Theme.Holo.Light.Dialog.MinWidth">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="android:textColor">#ec395d</item>

    </style>

</resources>


```

### string.xml file

```xml
<resources>
    <string name="app_name">Friends List</string>
    <string name="app_add">Add Friend</string>
    <string name="app_edit">Edit Friend</string>
    <string name="full_name">full name</string>
    <string name="email_id">email id</string>
    <string name="profile_details">profile details</string>
    <string name="salary_show">salary show</string>
</resources>

```
### color.xml file

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <color name="colorPrimary">#009688</color>
    <color name="colorPrimaryDark">#00796B</color>
    <color name="colorAccent">#4CAF50</color>
    <color name="primary_light">#B2DFDB</color>
    <color name="primary_text">#455A64</color>
    <color name="secondary_text">#37474F</color>
    <color name="icons">#FFFFFF</color>
    <color name="divider">#BDBDBD</color>

</resources>

```

### main_menu.xml file

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/setting_menu"
        android:orderInCategory="100"
        android:title="Setting"
        app:showAsAction="never" />
    <item
        android:id="@+id/add_menu"
        android:orderInCategory="101"
        android:title="Insert"
        app:showAsAction="ifRoom" />
</menu>

```

# Thank you 
