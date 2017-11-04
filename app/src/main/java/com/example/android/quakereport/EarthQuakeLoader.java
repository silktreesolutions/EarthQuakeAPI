package com.example.android.quakereport;


import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;

/**
 * Created by admin on 14-Oct-2017.
 */

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

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
