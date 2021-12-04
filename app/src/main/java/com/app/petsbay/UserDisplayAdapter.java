package com.app.petsbay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class UserDisplayAdapter extends RecyclerView.Adapter<UserDisplayAdapter.UserDisplayViewHolder> {
    List<User> userNames=new ArrayList<>();
    onItemClickListener listener;
    Context context;
    soldStatus status;
    View mView;
//    FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore fStore= FirebaseFirestore.getInstance();
    String id;


    public UserDisplayAdapter(){}

    public interface onItemClickListener{
        void onDeleteClick(int position);
    }
    public interface soldStatus{
       void isSold(boolean sold);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
    public void setSoldStatus(soldStatus status){
        this.status=status;
    }

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




                holder.userInfo.setText(user.UserName);
                holder.location.setText(user.location);


                Glide.with(context).load(user.Url).into(holder.petImage);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle bundle=new Bundle();
                        bundle.putString("Name",user.getUserName());
                        bundle.putString("location",user.getLocation());
                        bundle.putString("pettype",user.getPet_type());
                        bundle.putString("Url",user.getUniqueId());
                        bundle.putString("petDescription",user.getPet_description());
                        bundle.putString("position", String.valueOf(holder.getAdapterPosition()));

                         Navigation.findNavController(view).navigate(R.id.sendDetails,bundle);

                         if (listener!=null){
                             int pos=holder.getAdapterPosition();
                             if (pos!=RecyclerView.NO_POSITION){
                                 listener.onDeleteClick(pos);
                             }
                         }

                         if (status!=null){
                             status.isSold(false);
                         }



                    }
                });










    }

    public void deleteUser(){

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




        public UserDisplayViewHolder(@NonNull View itemView) {
            super(itemView);
                //to initialise and find the different layout elements.


            petImage=itemView.findViewById(R.id.user_pet_image);
            cardView=itemView.findViewById(R.id.cardview_display);
            userInfo=itemView.findViewById(R.id.username_textview);
            location=itemView.findViewById(R.id.location_textview);




        }
    }
}
