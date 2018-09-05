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
