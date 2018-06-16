package com.example.android.bakingapp.utilities;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.ui.StepsActivity;

import java.util.ArrayList;

import timber.log.Timber;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList mItems;
    private boolean isSteps;
    private boolean hasIngredients;

    public final ListAdapter.ItemOnClickHandler mCallback;

    public interface ItemOnClickHandler {
        void onClick(int position);
    }

    /**
     * Constructor
     */
    public ListAdapter(Context context, ArrayList items, ItemOnClickHandler onClickHandler, boolean has_ingredients) {
        this.mContext = context;
        this.mItems = items;
        this.hasIngredients = has_ingredients;
        this.mCallback = onClickHandler;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Activity activity = (Activity) mContext;
        int layoutId;
        if (activity.getClass().getName().equals(StepsActivity.class.getName())) {
            layoutId = R.layout.list;
            isSteps = true;
        } else {
            layoutId = R.layout.grid;
            isSteps = false;
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        if (isSteps) {
            if (hasIngredients) {
                final RecipesData.Ingredients current = (RecipesData.Ingredients) mItems.get(position);
                String ingredient = mContext.getString(R.string.ingredients, position + 1, current.getQuantity(), current.getMeasure(), current.getIngredient());
                holder.tv_name.setText(ingredient);
                holder.iv_thumbnail_url_list.setVisibility(View.GONE);
                holder.setIsRecyclable(false);
            } else {
                final RecipesData.Steps current = (RecipesData.Steps) mItems.get(position);
                String step;
                if (position != 0) {
                    step = mContext.getString(R.string.steps_with_index, position, current.getShortDescription());
                } else {
                    step = mContext.getString(R.string.steps, current.getShortDescription());
                }
                holder.tv_name.setText(step);
                String image_url = current.getImageUrl();
                if (image_url.endsWith(".png") || image_url.endsWith(".jpg")) {
                    Glide.with(mContext).load(image_url).crossFade().dontTransform().into(holder.iv_thumbnail_url_list);
                } else {
                    Timber.d("No image resource found");
                }
                holder.setIsRecyclable(false);
            }
        } else {
            final RecipesData current = (RecipesData) mItems.get(position);
            holder.tv_name.setText(current.getName());
            holder.tv_servings.setText(mContext.getString(R.string.servings, current.getServings()));
            holder.setIsRecyclable(false);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_servings;
        public ImageView iv_thumbnail_url_list;
        public View list_container;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.tv_name = itemView.findViewById(R.id.tw_list_recipe_name);
            this.tv_servings = itemView.findViewById(R.id.servings);
            this.iv_thumbnail_url_list = itemView.findViewById(R.id.iv_thumbnail_url_list);
            this.list_container = itemView.findViewById(R.id.list_container);
            this.list_container.setOnClickListener(mViewListener);

        }

        private View.OnClickListener mViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                mCallback.onClick(position);
            }
        };
    }
}
