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
