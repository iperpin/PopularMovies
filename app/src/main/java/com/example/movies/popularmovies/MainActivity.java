package com.example.movies.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.movies.popularmovies.adapters.MovieAdapter;
import com.example.movies.popularmovies.database.MovieContract;
import com.example.movies.popularmovies.objects.MovieObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private static final String LOG_TAG = "MainActivity";
    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noInternetTextView;
    private SwipeRefreshLayout swipeLayout;
    private String SAVED_LIST = "saved_list";
    private boolean infoSaved = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "SaveInstance: " + adapter.getMovies());
        outState.putParcelableArrayList(SAVED_LIST, adapter.getMovies());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.rvMovies);
        noInternetTextView = findViewById(R.id.tv_no_internet);
        noInternetTextView.setVisibility(View.INVISIBLE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Utils.numberOfColumns(this)));
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);

        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "On Restore");
            infoSaved = true;
            ArrayList<MovieObject> items = savedInstanceState.getParcelableArrayList(SAVED_LIST);
            adapter.update(items);
        }

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //refreshItems();
    }

    public void requestMovies(String url) {
        Log.d(LOG_TAG, "New petition to: " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setNoInternetTextView();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String bodyResponse = response.body().string();
                final List<MovieObject> movieObjects = Utils.parseMoviesJSON(bodyResponse);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noInternetTextView.setVisibility(View.INVISIBLE);
                        adapter.update(movieObjects);
                    }
                });
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex, MovieObject movie) {
        Log.d(LOG_TAG, movie.toString());
        Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);
        intent.putExtra(getString(R.string.intent_movie_object), movie);
        startActivity(intent);
    }

    private void refreshItems() {

        if (!infoSaved) {
            if (Utils.LoadPreferencesInt(MainActivity.this, getString(R.string.spinner_position), 0) == 3) {
                fetchFavoriteMovies();
            } else {
                if (Utils.isNetworkAvailable(this)) {
                    requestMovies(getString(R.string.url_base) + Utils.getUrlSpinnerChoice(MainActivity.this)
                            + getString(R.string.api_key_tag) + BuildConfig.MY_MOVIE_API_KEY);
                } else {
                    setNoInternetTextView();
                }
            }

        } else {
            infoSaved = false;
        }

        swipeLayout.setRefreshing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);

        MenuItem item = menu.findItem(R.id.actionbar_spinner);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_items, R.layout.spinner_single_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner.setAdapter(adapter);
        int lastSpinnerPosition = Utils.LoadPreferencesInt(MainActivity.this, getString(R.string.spinner_position), 0);
        spinner.setSelection(lastSpinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Utils.savePrefsInt(MainActivity.this, getString(R.string.spinner_position), position);
                Log.d(LOG_TAG, "Item Selected");
                refreshItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        return true;
    }

    private void setNoInternetTextView() {
        adapter.clear();
        noInternetTextView.setText(getString(R.string.no_internet));
        noInternetTextView.setVisibility(View.VISIBLE);
    }

    private void fetchFavoriteMovies() {

        String[] projection = {
                MovieContract.MovieEntry.COLUMN_ID,
                MovieContract.MovieEntry.MOVIE_ID,
                MovieContract.MovieEntry.MOVIE_VOTEAVG,
                MovieContract.MovieEntry.MOVIE_TITLE,
                MovieContract.MovieEntry.MOVIE_POSTER,
                MovieContract.MovieEntry.MOVIE_BACKDROP,
                MovieContract.MovieEntry.MOVIE_OVERVIEW,
                MovieContract.MovieEntry.MOVIE_RELEASEDATE,
                MovieContract.MovieEntry.MOVIE_VOTECOUNT,
                MovieContract.MovieEntry.MOVIE_VIDEO,
                MovieContract.MovieEntry.MOVIE_POPULARITY,
                MovieContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE,
                MovieContract.MovieEntry.MOVIE_ORIGINAL_TITLE,
                MovieContract.MovieEntry.MOVIE_ADULT
        };

        Cursor cursor = getApplicationContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                projection, null, null, null
        );

        if (cursor != null) {
            if (cursor.getCount() == 0) {
            }
            Log.d(LOG_TAG, "Size= " + cursor.getCount());

            List<MovieObject> movieObjects = new ArrayList<>();

            while (cursor.moveToNext()) {
                MovieObject movie = new MovieObject();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_ID))));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_VOTEAVG))));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_TITLE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_POSTER)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_BACKDROP)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_RELEASEDATE)));
                movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_VOTECOUNT))));
                movie.setVideo(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_VIDEO))));
                movie.setPopularity(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_POPULARITY))));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_ORIGINAL_LANGUAGE)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_ORIGINAL_TITLE)));
                movie.setAdult(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.MOVIE_ADULT))));
                movieObjects.add(movie);
            }

            noInternetTextView.setVisibility(View.INVISIBLE);
            adapter.update(movieObjects);
            cursor.close();
        }
    }
}
