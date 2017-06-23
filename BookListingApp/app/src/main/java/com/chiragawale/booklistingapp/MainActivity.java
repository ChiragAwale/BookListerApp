package com.chiragawale.booklistingapp;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements android.app.LoaderManager.LoaderCallbacks<String> {

    private static final String BASE_URL = " https://www.googleapis.com/books/v1/volumes?";
    private static final int MAX_NUMBER_OF_RESULTS = 10;
    private EditText searchValue = null;
    private TextView results = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Linking the Views to local variables
         */
        results = (TextView) findViewById(R.id.resultDisplay);
        searchValue = (EditText) findViewById(R.id.search_textView);
        final Button searchButton = (Button) findViewById(R.id.search_button);

        /*
        Setting action for search button
         */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    android.app.LoaderManager loaderManager = getLoaderManager();
                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.initLoader(1, null, MainActivity.this);
            }
        });
    }

    /*
    called when a loader is created
     */
    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        String requestURL = BASE_URL + "q=" + searchValue.getText().toString() + "&maxResults=" + 5;

        // Create a new loader for the given URL
        return new BookLoader(this, requestURL);
    }

    /*
    Called when data is loaded
     */
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {


        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            results.setText(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        results.setText("");
    }
}
