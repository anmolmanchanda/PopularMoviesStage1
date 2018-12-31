package com.anmolmanchanda.android.popularmoviesstage1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private static final String MOVIE_KEY = "movie_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details );

        Movie parcelableExtra = this.getIntent().getParcelableExtra( MOVIE_KEY );

        TextView title = findViewById( R.id.title );
        title.setText( parcelableExtra.getTitle() );

        TextView overview = findViewById( R.id.overview );
        overview.setText( parcelableExtra.getOverView() );

        TextView voteAverage = findViewById( R.id.vote_average );
        voteAverage.setText( parcelableExtra.getVoteAverage() );

        TextView releaseDate = findViewById( R.id.release_date );
        releaseDate.setText( parcelableExtra.getReleaseDate() );

        ImageView imageViewMovie = findViewById( R.id.iv_movie );
        Picasso.with( this )
                .load( parcelableExtra.getPosterPath() )
                .into( imageViewMovie );
    }
}
