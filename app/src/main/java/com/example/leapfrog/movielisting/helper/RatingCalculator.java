package com.example.leapfrog.movielisting.helper;


import com.example.leapfrog.movielisting.MovieDetailActivity;

public class RatingCalculator {


    public static Float calculateRatingNum(String rate_str) {
        Float rate_flt = Float.parseFloat(rate_str);
        //rating num in 5
        Float final_rate = (rate_flt / 10) * MovieDetailActivity.NUM_OF_STARS;
        return final_rate;
    }

}
