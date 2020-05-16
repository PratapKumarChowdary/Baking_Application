package com.pratapkumar.bakingapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.adapters.StepsAdapter;
import com.pratapkumar.bakingapplication.models.Recipe;
import com.pratapkumar.bakingapplication.models.Step;
import com.pratapkumar.bakingapplication.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler {

    private Recipe mRecipe;

    private StepsAdapter mStepsAdapter;

    OnStepClickListener mCallback;

    List<Step> steps = new ArrayList<>();

    @BindView(R.id.rv_steps)
    RecyclerView rvSteps;

    public interface OnStepClickListener {
        void onStepSelected(int stepIndex);
    }

    public StepsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this,view);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constant.RECIPE)) {
                Bundle b = intent.getBundleExtra(Constant.RECIPE);
                mRecipe = b.getParcelable(Constant.RECIPE);
                steps = mRecipe.getmSteps();
                mStepsAdapter = new StepsAdapter(steps, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                rvSteps.setLayoutManager(layoutManager);
                rvSteps.setHasFixedSize(true);
                rvSteps.setAdapter(mStepsAdapter);
            }
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onItemClick(int stepIndex) {
        mCallback.onStepSelected(stepIndex);
    }
}
