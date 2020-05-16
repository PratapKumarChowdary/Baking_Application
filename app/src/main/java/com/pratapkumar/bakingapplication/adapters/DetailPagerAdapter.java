package com.pratapkumar.bakingapplication.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.fragments.IngredientsFragment;
import com.pratapkumar.bakingapplication.fragments.StepsFragment;
import com.pratapkumar.bakingapplication.utilities.Constant;

import static com.pratapkumar.bakingapplication.utilities.Constant.INGREDIENTS;
import static com.pratapkumar.bakingapplication.utilities.Constant.STEPS;


public class DetailPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private int mNumIngredients;
    private int mNumSteps;


    public DetailPagerAdapter(Context context, FragmentManager fm, int numIngredients, int numSteps) {
        super(fm);
        mContext = context;
        mNumIngredients = numIngredients;
        mNumSteps = numSteps;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case INGREDIENTS:
                return new IngredientsFragment();
            case STEPS:
                return new StepsFragment();
        }
        return null;
    }


    @Override
    public int getCount() {
        return Constant.PAGE_COUNT;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case INGREDIENTS:
                String title = Constant.TAP_TITLE[position % Constant.PAGE_COUNT].toUpperCase();
                title += mContext.getString(R.string.space) +
                        mContext.getString(R.string.open_parenthesis) + mNumIngredients +
                        mContext.getString(R.string.close_parenthesis);
                return title;
            case STEPS:
                title = Constant.TAP_TITLE[position % Constant.PAGE_COUNT].toUpperCase();
                title += mContext.getString(R.string.space) +
                        mContext.getString(R.string.open_parenthesis) + mNumSteps +
                        mContext.getString(R.string.close_parenthesis);
                return title;
        }
        return null;

    }
}
