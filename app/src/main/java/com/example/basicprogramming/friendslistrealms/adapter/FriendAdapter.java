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
