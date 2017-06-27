package com.chiragawale.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements android.app.LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final int MAX_NUMBER_OF_RESULTS = 10;
    private EditText searchValue = null;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;
    private BookAdapter mAdapter;
    private android.app.LoaderManager loaderManager;

    private static final String STATE_COUNTER = "counter";

    private ArrayList<Book> mList = null;

    Loader loader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("OnCreate", "``````````````````````````````````````````````````````````");


        if (savedInstanceState != null) {
            mList = (ArrayList) savedInstanceState.getSerializable(STATE_COUNTER);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Linking the Views to local variables
         */
        searchValue = (EditText) findViewById(R.id.search_textView);
        final Button searchButton = (Button) findViewById(R.id.search_button);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);


        ArrayList<Book> bookList = new ArrayList<>();
        if(mList!=null){
            bookList = mList;
        }
        mAdapter = new BookAdapter(this, bookList);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        /*
        Setting action for search button
         */

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                     loaderManager = getLoaderManager();

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    Log.w("Init ", "``````````````````````````````````````````````````````````");

                    Log.w("after init", "``````````````````````````````````````````````````````````");
                    if( loader == null )
                        loader = loaderManager.initLoader(
                                0, null, MainActivity.this
                        );
                    else if( loader.isAbandoned() )
                        return;
                    else
                        loaderManager.restartLoader(
                                0, null, MainActivity.this
                        );
                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }

            }
        });


        /*
        Sets on click listeners to each item in the list view
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //Gets the book that is selected
                Book currentBook = mAdapter.getItem(position);
                //Parsing the link from the Book class for Intent
                Uri uri = Uri.parse(currentBook.getLink());
                //Defining the website intent
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,uri);
                //Launches the intent
                startActivity(websiteIntent);

            }
        });
    }

    /*
    called when a loader is created
     */
    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        Log.w("OnCreateLoader", "``````````````````````````````````````````````````````````");
        mProgressBar.setVisibility(View.VISIBLE);
        //Creates the string for URL according to the values provided by the user
        String requestURL = BASE_URL + "q=" + searchValue.getText().toString() + "&maxResults=" + MAX_NUMBER_OF_RESULTS;

        // Create a new loader for the given URL
        return new BookLoader(this, requestURL);
    }


       /*
        Called when data is loaded
         */

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        mList = data;
        Log.w("Onfinished", "``````````````````````````````````````````````````````````");
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_books);
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {

            mAdapter.addAll(data);
            Log.w("Added Data", "``````````````````````````````````````````````````````````");

        }


    }




    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> data) {
        mAdapter.clear();
        Log.w("ResetLoader", "``````````````````````````````````````````````````````````");

    }

    protected void restartLoading() {
        //Incase the screen is rotated
        mAdapter.clear();
        getLoaderManager().restartLoader(0, null, MainActivity.this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putSerializable(STATE_COUNTER, mList);

    }
}
