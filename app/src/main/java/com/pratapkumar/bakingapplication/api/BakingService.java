package com.pratapkumar.bakingapplication.api;

import com.pratapkumar.bakingapplication.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
