package com.example.flickster;
import java.util.ArrayList;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.List;


import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.rkpandey.flixster.R;
import com.example.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;
import okhttp3.internal.http2.Header;
import org.parceler.Parcels;

public class DetailActivity extends YouTubeBaseActivity {

    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;

    private static final String YOUTUBE_API_KEY = "AIzaSyC7osj4IfjWcugTnu-VARyqepM-1Pe91qo";
    public static final String VIDEOS_URL="https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar  = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);


        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getVoteAverage());

        AsyncHttpClient client = new AsyncHttpClient();

        if (ratingBar.getNumStars() >= 5 ) {
            client.get(String.format(VIDEOS_URL,movie.getMovieId()),new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {

                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Headers headers, JSONObject response) {
                    try {
                        JSONArray results =  response.getJSONArray("results");
                        if (results.length() == 0) {
                            return;
                        }
                        JSONObject movieTrailer = results.getJSONObject(0);
                        String youtubeKey = movieTrailer.getString("key");
                        initializeYoutube(youtubeKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void Failure(int statusCode, Header headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode,
                            Header

                            headers,
                            responseString,
                            throwable);
                }
            });
        }

    }

    private void initializeYoutube(final String youtubeKey) {

        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "on init success");
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DeatailActivity", "on init failure");
            }
        });
    }
}
