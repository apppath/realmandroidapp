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
## Realm Config File RealmConfig.java

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

## Realm Table Model File Friends.java

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
## Realm Insert Record AddActivity.java File

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
## Realm Display Record MainActivity.java File

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

## Recyclerview Adapter to display FriendAdapter.java

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


