package com.app.petsbay;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

class UserDisplayAdapter extends RecyclerView.Adapter<UserDisplayAdapter.UserDisplayViewHolder> {
    private final List<User> userNames;

    Context context;
    //FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore fStore= FirebaseFirestore.getInstance();
    String link;

    public UserDisplayAdapter(List<User> userNames, Context context){

        this.userNames = userNames;
        this.context=context;
    }



    @NotNull
    @Override
    public UserDisplayViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View v=layoutInflater.inflate(R.layout.main_display_section,parent,false);

        return new UserDisplayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  UserDisplayAdapter.UserDisplayViewHolder holder, int position) {


           User user=userNames.get(position);



            if (user.getUniqueId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                holder.itemView.setLayoutParams(holder.params);

            else {
                holder.userInfo.setText(user.UserName);
                holder.location.setText(user.location);


                Glide.with(context).load(user.Url).into(holder.petImage);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String id = user.uniqueId;

                        MainFragmentDirections.SendDetails action = MainFragmentDirections.sendDetails();
                        action.setId(id);
                        Navigation.findNavController(view).navigate(action);


                    }
                });

            }


    }


    @Override
    public int getItemCount() {

        return userNames.size();
    }

    public static class UserDisplayViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView petImage;
        TextView userInfo;
        TextView location;

        ViewGroup.LayoutParams params;


        public UserDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
                //to initialise and find the different layout elements.
                params=new FrameLayout.LayoutParams(0,0);


            petImage=itemView.findViewById(R.id.user_pet_image);
            cardView=itemView.findViewById(R.id.cardview_display);
            userInfo=itemView.findViewById(R.id.username_textview);
            location=itemView.findViewById(R.id.location_textview);


        }
    }
}
