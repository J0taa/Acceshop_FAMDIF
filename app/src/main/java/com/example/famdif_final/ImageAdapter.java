package com.example.famdif_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    public Context mContext;
    private List<Upload> mUploads;

    public ImageAdapter(Context context, List<Upload> upload){
        mContext=context;
        mUploads=upload;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload  uploadCurrent = mUploads.get(position);
        Picasso.with(mContext)
                .load(uploadCurrent.getmImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size() ;
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public ImageViewHolder(View item){
            super(item);
            imageView =item.findViewById(R.id.image_view_upload);
        }
    }
}
