package com.example.android.foodwhips.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.foodwhips.R;
import com.example.android.foodwhips.models.SearchRecipe;
import com.example.android.foodwhips.utilities.ConversionUtils;

import java.util.ArrayList;

/**
 * Created by li-en on 7/2/17.
 */

public class RecipeResultsAdapter extends RecyclerView.Adapter<RecipeResultsAdapter.RecipeHolder> {
    private ArrayList<SearchRecipe> recipeList;
    RecipeClickListener listener;

    final static String TAG = "RECIPERESULTSADAPTER";

    //Constructor of RecipeResults Adapter
    public RecipeResultsAdapter(ArrayList<SearchRecipe> recipeList, RecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    public interface RecipeClickListener {
        void onRecipeClick(int clickedRecipeIndex);
    }

    //Creates each of the ViewHolders to display on the screen
    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //gets context of current activity on the screen
        Context context = viewGroup.getContext();

        //obtains views from the xml file
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean attachToParentImmediately = false;

        //used to instantiate layout xml file into actual View objects
        View view = inflater.inflate(R.layout.search_results, viewGroup, attachToParentImmediately);

        RecipeHolder holder = new RecipeHolder(view);

        return holder;
    }

    //Displays the data info of an article at specified position given
    @Override
    public void onBindViewHolder(RecipeHolder recipeHolder, int position) {
        recipeHolder.bind(position);
    }

    //returns size of arrayList holding all the recipe matches
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mFoodImage;
        TextView mRecipeNameText;
        TextView mRatingText;
        TextView mTimeTakenText;
        TextView mCoursesText;
        TextView mCuisinesText;

        public RecipeHolder(View view) {
            super(view);

            //Getting references of the id's
            mFoodImage = (ImageView) view.findViewById(R.id.recipe_image);
            mRecipeNameText = (TextView) view.findViewById(R.id.recipe_name);
            mRatingText = (TextView) view.findViewById(R.id.recipe_rating);
            mTimeTakenText = (TextView) view.findViewById(R.id.recipe_time);
            mCoursesText = (TextView) view.findViewById(R.id.recipe_courses);
            mCuisinesText = (TextView) view.findViewById(R.id.recipe_cuisines);

            view.setOnClickListener(this);
        }

        public void bind(int position) {
            SearchRecipe recipe = recipeList.get(position);

            new ConversionUtils.FetchImageTask(mFoodImage).execute(recipe.getImg());
            mRecipeNameText.setText(recipe.getRecipeName().toUpperCase());

            Log.v(TAG, "WHAT IS THE VALUE OF RECIPE RATING?: " + recipe.getRating() + " AND LENGTH OF RATING: " + recipe.getRating().length());

            if(recipe.getRating().equals("0")){
                Log.v(TAG, "WENT IN HERE BECAUSE RATING IS 0");
                mRatingText.setVisibility(View.VISIBLE);
                mRatingText.setText("Rating: 0 stars");
            }
            else if(recipe.getRating() != null) {
                mRatingText.setVisibility(View.VISIBLE);
                mRatingText.setText("Rating: " + ConversionUtils.starRating(recipe.getRating()));
            }

            if(recipe.getTimeInSecs().length() > 0 && recipe.getTimeInSecs() != null) {
                mTimeTakenText.setVisibility(View.VISIBLE);
                mTimeTakenText.setText("Time: " + ConversionUtils.secondsToHrsMins(recipe.getTimeInSecs()));
            }

            if (recipe.isCoursesEmpty() == false) {
                mCoursesText.setVisibility(View.VISIBLE);
                mCoursesText.setText("Course(s): " + recipe.printCourses());
            }

            if(recipe.isCuisinesEmpty() == false) {
                mCuisinesText.setVisibility(View.VISIBLE);
                mCuisinesText.setText("Cuisine(s): " + recipe.printCuisines());
            }
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            listener.onRecipeClick(pos);
        }
    }
}