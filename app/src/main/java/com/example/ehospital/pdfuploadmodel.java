package com.example.ehospital;


import android.os.Parcel;
import android.os.Parcelable;

public class pdfuploadmodel implements Parcelable
{
    public String name;
    public String url;

    public pdfuploadmodel(String name, String url) {
        this.name = name;
        this.url = url;
    }
    public pdfuploadmodel() {
    }

    protected pdfuploadmodel(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    public static final Creator<pdfuploadmodel> CREATOR = new Creator<pdfuploadmodel>() {
        @Override
        public pdfuploadmodel createFromParcel(Parcel in) {
            return new pdfuploadmodel(in);
        }

        @Override
        public pdfuploadmodel[] newArray(int size) {
            return new pdfuploadmodel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }
}
