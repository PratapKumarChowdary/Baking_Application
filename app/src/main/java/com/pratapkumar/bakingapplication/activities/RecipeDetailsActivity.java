package com.pratapkumar.bakingapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.pratapkumar.bakingapplication.adapters.DetailPagerAdapter;
import com.pratapkumar.bakingapplication.fragments.StepDetailFragment;
import com.pratapkumar.bakingapplication.fragments.StepsFragment;
import com.pratapkumar.bakingapplication.utilities.Constant;
import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.models.Recipe;
import com.pratapkumar.bakingapplication.models.Step;
import com.squareup.picasso.Picasso;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailsActivity extends AppCompatActivity implements StepsFragment.OnStepClickListener {

    private boolean isTablet;
    private Recipe recipe;
    private List<Step> stepList;

    @Nullable
    @BindView(R.id.viewpager)
    ViewPager recipesViewPager;

    @Nullable
    @BindView(R.id.iv_detail)
    ImageView recipeImage;

    @Nullable
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Nullable
    @BindView(R.id.tv_servings)
    TextView numberServing;

    @Nullable
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.step_detail_container)
    FrameLayout step_detail_container;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(RecipeDetailsActivity.this);


        Intent intent = getIntent();
        if(intent.hasExtra(Constant.RECIPE)) {
            Bundle b = intent.getBundleExtra(Constant.RECIPE);
            recipe = b.getParcelable(Constant.RECIPE);
            setTitle(recipe.getmName());
        }

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if(isTablet){

            if (savedInstanceState == null) {

                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                Step step = recipe.getmSteps().get(0);
                stepDetailFragment.setStep(step);
                stepDetailFragment.setStepIndex(0);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
            }

        }
        else{
                displayImage();

                int numServings = recipe.getmServings();
                numberServing.setText(String.valueOf(numServings));

                setCollapsingToolbarTextColor();
                showUpButton(isTablet);

                stepList = recipe.getmSteps();

                tabLayout.setupWithViewPager(recipesViewPager);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                int numIngredients = recipe.getmIngredients().size();
                int numSteps = recipe.getmSteps().size() - 1;

                DetailPagerAdapter detailPagerAdapter = new DetailPagerAdapter(this,
                        getSupportFragmentManager(), numIngredients, numSteps);
                recipesViewPager.setAdapter(detailPagerAdapter);
        }


    }

    private void displayImage() {
        String imageUrl = recipe.getmImage();
        if (imageUrl.isEmpty()) {
            switch (recipe.getmId()) {
                case 1:
                    recipeImage.setImageResource(R.drawable.nutella_pie);
                    break;
                case 2:
                    recipeImage.setImageResource(R.drawable.brownies);
                    break;
                case 3:
                    recipeImage.setImageResource(R.drawable.yellow_cake);
                    break;
                case 4:
                    recipeImage.setImageResource(R.drawable.cheesecake);
                    break;
                default:
                    recipeImage.setImageResource(R.drawable.cake);
                    break;
            }
        }
        else {
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.cake)
                    .placeholder(R.drawable.cake)
                    .into(recipeImage);
        }
    }

    @Override
    public void onStepSelected(int stepIndex) {
        if(isTablet){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            Step step = recipe.getmSteps().get(stepIndex);
            stepDetailFragment.setStep(step);
            stepDetailFragment.setStepIndex(stepIndex);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();

        }
        else
        {
            Bundle b = new Bundle();
            b.putInt(Constant.EXTRA_STEP_INDEX, stepIndex);
            b.putParcelable(Constant.RECIPE, recipe);

            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putExtra(Constant.EXTRA_STEP_INDEX, b);
            intent.putExtra(Constant.RECIPE, b);
            startActivity(intent);
        }

    }

    private void setCollapsingToolbarTextColor() {
        collapsingToolbarLayout.setExpandedTitleColor(
                getResources().getColor(R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(
                getResources().getColor(R.color.white));
    }

    private void showUpButton(boolean isTablet ) {

        if (!isTablet) {
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
