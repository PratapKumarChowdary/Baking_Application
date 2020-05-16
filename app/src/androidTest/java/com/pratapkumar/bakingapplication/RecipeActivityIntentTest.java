package com.pratapkumar.bakingapplication;


import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import com.pratapkumar.bakingapplication.activities.RecipeActivity;
import com.pratapkumar.bakingapplication.activities.RecipeDetailsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pratapkumar.bakingapplication.utilities.Constant.RECIPE_NAME_AT_ZERO;

@RunWith(AndroidJUnit4.class)

public class RecipeActivityIntentTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void clickRecipe_LaunchDetailActivityIntent() {
        Intents.init();
        onView(withId(R.id.recipesList))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(RECIPE_NAME_AT_ZERO)), click()));
        intended(hasComponent(RecipeDetailsActivity.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
