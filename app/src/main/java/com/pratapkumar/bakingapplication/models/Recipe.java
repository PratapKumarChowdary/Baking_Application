package com.pratapkumar.bakingapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Recipe implements Parcelable {

    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> mIngredients = null;

    @SerializedName("steps")
    @Expose
    private List<Step> mSteps = null;

    @SerializedName("servings")
    @Expose
    private int mServings;

    @SerializedName("image")
    @Expose
    private String mImage;

    public Recipe(int recipeId, String name, List<Ingredient> ingredients, List<Step> steps,
                  int servings, String image) {
        mId = recipeId;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
        mImage = image;
    }

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIngredients = new ArrayList<>();
        in.readList(mIngredients,Ingredient.class.getClassLoader());
        mSteps = new ArrayList<>();
        in.readList(mSteps,Step.class.getClassLoader());
        mServings = in.readInt();
        mImage = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeList(mIngredients);
        parcel.writeList(mSteps);
        parcel.writeInt(mServings);
        parcel.writeString(mImage);
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public List<Ingredient> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<Step> getmSteps() {
        return mSteps;
    }

    public void setmSteps(List<Step> mSteps) {
        this.mSteps = mSteps;
    }

    public int getmServings() {
        return mServings;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    @Override
    public String toString() {
        return "Recipe{" + "id=" + mId + ", name='" + mName + '\'' + ", ingredients=" + mIngredients + ", steps=" + mSteps + ", servings=" + mServings + ", image='" + mImage + '\'' + '}';
    }
}
