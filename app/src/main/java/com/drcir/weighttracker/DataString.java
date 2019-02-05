package com.drcir.weighttracker;

import android.os.Parcel;
import android.os.Parcelable;

public class DataString implements Parcelable {

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    private String dataset;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dataset);
    }

    public DataString(Parcel p) {
        dataset = p.readString();
    }

    public DataString() {}

    public static final Parcelable.Creator <DataString> CREATOR = new Parcelable.Creator<DataString>() {

        @Override
        public DataString createFromParcel(Parcel parcel) {
            return new DataString(parcel);
        }

        @Override
        public DataString[] newArray(int size) {
            return new DataString[size];
        }
    };
}
