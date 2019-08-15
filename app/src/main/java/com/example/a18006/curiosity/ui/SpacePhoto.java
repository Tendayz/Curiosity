package com.example.a18006.curiosity.ui;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

public class SpacePhoto implements Parcelable {

    private String mUrl;

    public SpacePhoto(String url) {
        mUrl = url;
    }

    protected SpacePhoto(Parcel in) {
        mUrl = in.readString();
    }

    public static final Creator<SpacePhoto> CREATOR = new Creator<SpacePhoto>() {
        @Override
        public SpacePhoto createFromParcel(Parcel in) {
            return new SpacePhoto(in);
        }

        @Override
        public SpacePhoto[] newArray(int size) {
            return new SpacePhoto[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public static LinkedList<SpacePhoto> getSpacePhotos(String[] images) {

        LinkedList<SpacePhoto> spacePhoto = new LinkedList<>();

        for (int i = 0; i < 20; i++) {
            spacePhoto.add(new SpacePhoto(images[i]));
        }
        return spacePhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
    }
}
