package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 30-Aug-2017.
 */

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

    String locationOffSet;
    String primaryLocation;

    public EarthQuakeAdapter(Context context, ArrayList<EarthQuake> earthquakes){
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //getting the current view
        View listItemView = convertView;

        //checking if the listItemView is initialized
        if(listItemView == null){
            //inflating the list view
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //getting the current item from the list
        EarthQuake currentEarthQuake = getItem(position);

        //getting magnitude
        double mag = currentEarthQuake.getEarthquakeMagnitude();
        //formatting the magnitude
        DecimalFormat magnitudeFormatter = new DecimalFormat("0.0");
        String formattedMagnitude = magnitudeFormatter.format(mag);

        //Extracting the magnitude
        TextView magnitude = (TextView)listItemView.findViewById(R.id.magnitutde);
        //Setting the value of the magnitude
        magnitude.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle =  (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(mag);
        magnitudeCircle.setColor(magnitudeColor);



        //Checking if the earthquake location contains an location offset
        if(currentEarthQuake.getEarthquakeLocation().contains("of")){
            String[] locationArray = currentEarthQuake.getEarthquakeLocation().split("of");
            locationOffSet = locationArray[0] + " of ";
            primaryLocation = locationArray[1];
        }else{
            //set location off set to value of the string
            locationOffSet = getContext().getString(R.string.near_the);
            primaryLocation = currentEarthQuake.getEarthquakeLocation();
        }

        //Extracting the location off set
        TextView locationOffSetView = (TextView) listItemView.findViewById(R.id.location_offset);
        //setting the locatino off set
        locationOffSetView.setText(locationOffSet);

        //Extracting the primary location
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        //setting the primary location
        primaryLocationView.setText(primaryLocation);

        //Creating Date Object
        Date dateObject = new Date(currentEarthQuake.getEarthquakeDateAndTime());

        //Extracting the date
        TextView date = (TextView) listItemView.findViewById(R.id.date);

        //Formatting date
        String formattedDate = new SimpleDateFormat("LLL dd, yyyy").format(dateObject);

        //Setting the value of the date
        date.setText(formattedDate);

        //Extracting the time
        TextView time = (TextView) listItemView.findViewById(R.id.time);

        //Formatting the time
        String formattedTime = new SimpleDateFormat("h:mm a").format(dateObject);

        //Setting the value of the time
        time.setText(formattedTime);


        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {

        int magnitudeColor = 0;

        int magnitudeColorCode = (int) Math.floor(magnitude);

        switch (magnitudeColorCode) {
            case 0:
            case 1:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 2:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 3:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 4:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 5:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 6:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            case 7:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 8:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 9:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            default:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                break;
        }

        return magnitudeColor;
    }
}
