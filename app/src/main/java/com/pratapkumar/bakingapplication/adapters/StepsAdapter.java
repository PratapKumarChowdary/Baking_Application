
package com.pratapkumar.bakingapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratapkumar.bakingapplication.R;
import com.pratapkumar.bakingapplication.models.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private List<Step> mSteps;

    private final StepsAdapterOnClickHandler mOnClickHandler;

    public interface StepsAdapterOnClickHandler {
        void onItemClick(int stepIndex);
    }

    public StepsAdapter(List<Step> steps, StepsAdapterOnClickHandler onClickHandler) {
        mSteps = steps;
        mOnClickHandler = onClickHandler;
    }


    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_item,parent,false);
        return new StepsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.bind(step, position);
    }


    @Override
    public int getItemCount() {
        if (null == mSteps) return 0;
        return mSteps.size();
    }


    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_id)
        TextView tvStepId;
        @BindView(R.id.tv_step_short_description)
        TextView tvStepShortDescription;
        @BindView(R.id.iv_play_arrow)
        ImageView recipeImageView;
        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Step step, int position) {

            int stepId = getCorrectStepId(step, position);
            tvStepId.setText(String.valueOf(stepId));
            tvStepShortDescription.setText(step.getmShortDescription());

            String thumbnailUrl = step.getmThumbnailUrl();
            if (thumbnailUrl.isEmpty() || thumbnailUrl.contains(
                    itemView.getContext().getString(R.string.mp4))) {
               ivThumbnail.setVisibility(View.GONE);
            } else {

               ivThumbnail.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(thumbnailUrl)
                        .error(R.drawable.cake)
                        .placeholder(R.drawable.cake)
                        .into(ivThumbnail);
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickHandler.onItemClick(adapterPosition);
        }


        private int getCorrectStepId(Step step, int position) {
            int stepId = step.getmId();
            if (stepId != position) {
                stepId = position;
            }
            return stepId;
        }

    }
}
