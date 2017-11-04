/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    /** Adapter for the list of earthquakes */
    private EarthQuakeAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** ProgressBar is displayed when earthquake list is being loaded*/
    private ProgressBar mProgressBar;

    /** Boolen value will hold if device has internet connection*/
    private boolean mConnected;
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        ConnectivityManager connectivityManager =  (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        mConnected =  activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        /*
        // Create a fake list of earthquake locations.
        ArrayList<EarthQuake> earthquakes = new ArrayList<>();
        earthquakes.add(new EarthQuake("7.5", "Manila", new Date().toString()));
        earthquakes.add(new EarthQuake("5.3", "London", new Date().toString()));
        earthquakes.add(new EarthQuake("5.7", "Tokyo", new Date().toString()));
        earthquakes.add(new EarthQuake("6.1", "Mexico City", new Date().toString()));
        earthquakes.add(new EarthQuake("6.4", "Moscow", new Date().toString()));
        earthquakes.add(new EarthQuake("5.2", "Rio de Janeiro", new Date().toString()));
        earthquakes.add(new EarthQuake("6.2", "Paris", new Date().toString()));
        */

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, earthquakes);
                */

        mAdapter = new EarthQuakeAdapter(this, new ArrayList<EarthQuake>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        //EarthQuakeAsyncTask task = new EarthQuakeAsyncTask();
        //task.execute(USGS_REQUEST_URL);

        // Get a reference to the LoaderManager, in order to interact with loaders.


        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        // Checking for internet connectivity
        if(mConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else{
            //Remove progress and display message in case of no connectivity
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Get Current Earthquake which was clicked
                EarthQuake currentEarthQuake = mAdapter.getItem(i);
                //Extract the URL
                Uri earthquakeUri = Uri.parse(currentEarthQuake.getEarthQuakeURL());

                //Pass the url to the intent
                Intent earthquakeURIIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(earthquakeURIIntent);
            }
        });
    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),
                                                          getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPreferences.getString(
                                            getString(R.string.settings_order_by_key),
                                            getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthQuakeLoader(this, uriBuilder.toString());

        // TODO: Create a new loader for the given URL
        //mProgressBar.isShown();
        /*
        mProgressBar.setVisibility(View.VISIBLE);
        return new EarthQuakeLoader(this, USGS_REQUEST_URL);
        */
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {
        //mProgressBar.clearAnimation();
        mProgressBar.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        if(mConnected) {
            mEmptyStateTextView.setText(R.string.no_earthquakes);
        }

        mAdapter.clear();

        if(earthQuakes != null && !earthQuakes.isEmpty()){
            mAdapter.addAll(earthQuakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
        // TODO: Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    private class EarthQuakeLoader extends android.content.AsyncTaskLoader<List<EarthQuake>>{

        String mUrl;

        public EarthQuakeLoader(Context context, String url){
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<EarthQuake> loadInBackground() {
            // TODO: Implement this method
            if(mUrl == null) {
                return null;
            }

            List<EarthQuake> result = QueryUtils.fetchEarthquakeData(mUrl);
            return result;
        }
    }
    */

    /*
    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, List<EarthQuake>>{

        @Override
        protected List<EarthQuake> doInBackground(String... urls) {

            if(urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<EarthQuake> result = QueryUtils.fetchEarthquakeData(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(List<EarthQuake> earthQuakeList) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.

            if(earthQuakeList != null && !earthQuakeList.isEmpty()){
                mAdapter.addAll(earthQuakeList);
            }
        }
    }
    */
}