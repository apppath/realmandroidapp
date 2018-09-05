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
