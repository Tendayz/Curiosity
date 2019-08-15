package com.example.a18006.curiosity.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a18006.curiosity.R;
import com.example.a18006.curiosity.data.db.DBHelper;

import java.util.LinkedList;

class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

    View photoView;
    @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        photoView = inflater.inflate(R.layout.item_photo, parent, false);
        ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

        SpacePhoto spacePhoto = mSpacePhotos.get(position);
        ImageView imageView = holder.mPhotoImageView;

        Glide.with(mContext)
                .load(spacePhoto.getUrl())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return (mSpacePhotos.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView mPhotoImageView;

        View itemView;

        public MyViewHolder(View itemView) {

            super(itemView);

            this.itemView = itemView;

            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                    SpacePhoto spacePhoto = mSpacePhotos.get(position);
                    Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                    intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    mContext.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Удаление")
                    .setMessage("Удалить изображение?")
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Ок",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int position = getAdapterPosition();
                                    deleteImageFromDB(mSpacePhotos.get(position).getUrl());
                                    mSpacePhotos.remove(position);
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        public void deleteImageFromDB(String url){
            DBHelper dbHelper = new DBHelper(mContext);
            dbHelper.delete(url);
            notifyDataSetChanged();
        }
    }

    private LinkedList<SpacePhoto> mSpacePhotos;
    private Context mContext;

    public ImageGalleryAdapter(Context context, LinkedList<SpacePhoto> spacePhotos) {
        mContext = context;
        mSpacePhotos = spacePhotos;
    }
}
