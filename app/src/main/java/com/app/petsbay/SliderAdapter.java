package com.app.petsbay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {
    Context context;
    ArrayList<ModelClass> imageLinks;

    public SliderAdapter(Context context, ArrayList<ModelClass> imageLinks) {
        this.context = context;
        this.imageLinks = imageLinks;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_view_layout,parent,false);

        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {

        ModelClass modelItems=imageLinks.get(position);

        Glide.with(viewHolder.itemView).load(modelItems.getUrl()).into(viewHolder.img);
    }

    @Override
    public int getCount() {
        return imageLinks.size();
    }

     class SliderAdapterViewHolder extends SliderAdapter.ViewHolder{
        ImageView img;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.userpetimages);
        }
    }
}
