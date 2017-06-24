package com.chiragawale.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by chira on 6/22/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

/** Tag for log messages */
private static final String LOG_TAG = BookLoader.class.getName();

/** Query URL */
private String mUrl;

/**
 * Constructs a new {@link BookLoader}.
 *
 * @param context of the activity
 * @param url to load data from
 */
public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
        }

@Override
protected void onStartLoading() {
        forceLoad();
        }

/**
 * This is on a background thread.
 */
@Override
public List<Book> loadInBackground() {
        if (mUrl == null) {
        return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Book> earthquakes = QueryUtil.getBookData(mUrl);
        return earthquakes;
        }
        }
