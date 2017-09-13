package com.example.leapfrog.movielisting.helper;


import com.example.leapfrog.movielisting.activities.MovieDetailActivity;

public class RatingCalculator {


    public static Float calculateRatingNum(String rateStr) {
        Float rate = Float.parseFloat(rateStr);
        //rating num in 5
        Float finalRate = (rate / 10) * MovieDetailActivity.NUM_OF_STARS;
        return finalRate;
    }

}
