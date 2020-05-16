package com.pratapkumar.bakingapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    private double mQuantity;

    @SerializedName("measure")
    private String mMeasure;

    @SerializedName("ingredient")
    private String mIngredient;

    public Ingredient(double mQuantity, String mMeasure, String mIngredient) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mIngredient = mIngredient;
    }

    protected Ingredient(Parcel in) {
        mQuantity = in.readDouble();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public double getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(double mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public void setmMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public void setmIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mIngredient);
    }
}
