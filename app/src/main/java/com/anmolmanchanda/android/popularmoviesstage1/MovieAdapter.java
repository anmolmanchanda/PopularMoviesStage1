package com.anmolmanchanda.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieViewHolder> {
    private static final String MOVIE_KEY = "movie_key";
    private Context context;
    private List <Movie> movieList;

    public MovieAdapter(Context mContext, ArrayList <Movie> mMovieArrayList) {
        context = mContext;
        movieList = mMovieArrayList;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate( layoutIdForListItem, viewGroup, shouldAttachToParentImmediately );
        return new MovieViewHolder( view );
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MovieViewHolder movieViewHolder, final int position) {
        movieViewHolder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = movieList.get( position );
                Intent intent = new Intent( context, DetailsActivity.class );
                intent.putExtra( MOVIE_KEY, movie );
                context.startActivity( intent );
            }
        } );

        Picasso.with( context )
                .load( movieList.get( position ).getPosterPath() )
                .placeholder( R.drawable.placeholder_image )
                .error( R.drawable.error_image )
                .into( movieViewHolder.imageView );
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.iv_movie)
        public ImageView imageView;
        public View mView;

        //constructor
        public MovieViewHolder(View itemView) {
            super( itemView );
            mView = itemView;
            imageView = (ImageView) itemView.findViewById( R.id.iv_movie );
        }
    }
}