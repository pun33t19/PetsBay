package com.app.petsbay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserDisplayAdapter extends RecyclerView.Adapter<UserDisplayAdapter.UserDisplayViewHolder> {
    private final ArrayList<String> imagePaths;


    public UserDisplayAdapter(ArrayList<String> imagePaths){

        this.imagePaths = imagePaths;
    }

    @Override
    public UserDisplayAdapter.UserDisplayViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View v=layoutInflater.inflate(R.layout.main_display_section,parent,false);

        return new UserDisplayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  UserDisplayAdapter.UserDisplayViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public static class UserDisplayViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView petImage;
        TextView userInfo;
        TextView petType;

        public UserDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
                //to initialise and find the different layout elements.
            petImage=itemView.findViewById(R.id.user_pet_image);
            cardView=itemView.findViewById(R.id.cardview_display);
            userInfo=itemView.findViewById(R.id.username_textview);
            petType=itemView.findViewById(R.id.pet_type_textview);
        }
    }
}
