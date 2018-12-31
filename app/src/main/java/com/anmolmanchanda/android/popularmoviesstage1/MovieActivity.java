package com.anmolmanchanda.android.popularmoviesstage1;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieActivity extends AppCompatActivity {

    private static final int COLUMN = 2;
    private static final String SHARED_KEY_SORT = "sort";
    private static final String RATED = "top_rated";
    private static final String POPULARITY = "popular";
    SharedPreferences mSettings;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_movie );

        recyclerView = findViewById( R.id.rv_movies );
        recyclerView.setHasFixedSize( true );
        gridLayoutManager = new GridLayoutManager( this, COLUMN );
        recyclerView.setLayoutManager( gridLayoutManager );

        mSettings = PreferenceManager.getDefaultSharedPreferences( this );
        mEditor = mSettings.edit();
        mEditor.apply();

        movieAdapter = new MovieAdapter( this, new ArrayList <Movie>() );
        recyclerView.setAdapter( movieAdapter );
    }

    @Override
    public void onStart() {
        super.onStart();
        updateScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popularity:
                mEditor.putString( SHARED_KEY_SORT, POPULARITY );
                mEditor.apply();
                updateScreen();
                item.setChecked( true );
                return true;
            case R.id.rated:
                mEditor.putString( SHARED_KEY_SORT, RATED );
                mEditor.apply();
                updateScreen();
                item.setChecked( true );
                return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void updateScreen() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        String sortBy = mSettings.getString( SHARED_KEY_SORT, POPULARITY );
        try {
            recyclerView.setAdapter( new MovieAdapter( this,
                    fetchMoviesTask.execute( sortBy ).get() ) );
            Log.d( "updateScreen()", "fetchMovieTask performed" );
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu( menu );
        String sortBy = mSettings.getString( SHARED_KEY_SORT, POPULARITY );
        if (sortBy.equals( POPULARITY )) {
            menu.findItem( R.id.popularity ).setChecked( true );
        } else {
            menu.findItem( R.id.rated ).setChecked( true );
        }
        return true;
    }

    public class FetchMoviesTask extends AsyncTask <String, Void, ArrayList <Movie>> {
        private final String LOG_TAG = MovieActivity.class.getSimpleName();

        private ArrayList <Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String MDB_RESULT = "results";
            final String MDB_TITLE = "title";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_VOTE_AVERAGE = "vote_average";
            final String MDB_OVERVIEW = "overview";
            final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185/";

            JSONObject movieJson = new JSONObject( movieJsonStr );
            JSONArray movieArray = movieJson.getJSONArray( MDB_RESULT );

            ArrayList <Movie> movieArrayList = new ArrayList <>();

            for (int i = 0; i <= 9; i++) {

                JSONObject movieObject = movieArray.getJSONObject( i );

                String title = movieObject.getString( MDB_TITLE );
                String release_date = movieObject.getString( MDB_RELEASE_DATE );
                String poster_path = movieObject.getString( MDB_POSTER_PATH );
                String vote_average = movieObject.getString( MDB_VOTE_AVERAGE );
                String overview = movieObject.getString( MDB_OVERVIEW );

                Movie movie = new Movie( title,
                        release_date,
                        IMAGE_BASE_URL + poster_path,
                        vote_average,
                        overview
                );

                movieArrayList.add( movie );
            }

            return movieArrayList;
        }

        @Override
        protected ArrayList <Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {
                final String MOVIEDB_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String QUERY_APPKEY = "api_key";
                Uri builtUri = Uri.parse( MOVIEDB_BASE_URL ).buildUpon()
                        .appendPath( params[0] )
                        .appendQueryParameter( QUERY_APPKEY, getString( R.string.key ) )
                        .build();

                URL url = new URL( builtUri.toString() );

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod( "GET" );
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader( new InputStreamReader( inputStream ) );

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append( line + "\n" );
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e( LOG_TAG, "Error ", e );
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e( LOG_TAG, "Error closing stream", e );
                    }
                }
            }

            try {
                return getMovieDataFromJson( movieJsonStr );
            } catch (JSONException e) {
                Log.e( LOG_TAG, e.getMessage(), e );
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList <Movie> result) {
        }
    }
}
