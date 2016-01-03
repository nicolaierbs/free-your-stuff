package de.nachbarschaftsheld.freeyourstuff;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import de.nachbarschaftsheld.freeyourstuff.R;

/**
 * Created by nico on 21/12/15.
 */
public class JSONAdapter extends BaseAdapter{

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // your particular dataset uses String IDs
        // but you have to put something in this method
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.row_item, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.rowItemLayout = (RelativeLayout) convertView.findViewById(R.id.row_item_layout);
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.row_img_thumbnail);
            holder.typeTextView = (TextView) convertView.findViewById(R.id.row_text_type);
            holder.summaryTextView = (TextView) convertView.findViewById(R.id.row_text_summary);
            holder.distanceTextView = (TextView) convertView.findViewById(R.id.row_text_distance);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.row_text_time);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        JSONObject jsonObject = (JSONObject) getItem(position);

// See if there is a cover ID in the Object
//        if (jsonObject.has("cover_i")) {
//
//            // If so, grab the Cover ID out from the object
//            String imageID = jsonObject.optString("cover_i");
//
//            // Construct the image URL (specific to API)
//            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";
//
//            // Use Picasso to load the image
//            // Temporarily have a placeholder in case it's slow to load
//            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
//        } else {

            // If there is no cover ID in the object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.mipmap.ic_launcher);
      //  }

        // Grab the title and author from the JSON
        String itemSummary = "";
        String distance = "";
        String time = "";
        String type = "";

        if (jsonObject.has("summary")) {
            itemSummary = jsonObject.optString("summary");
        }

        if (jsonObject.has("longitude") && jsonObject.has("latitude")) {
            distance = computeDistance(jsonObject.optDouble("longitude"),jsonObject.optDouble("latitude"));
        }

        if (jsonObject.has("date")) {
            time = computeTime(jsonObject.optLong("date"));
        }

        if (jsonObject.has("type")) {
            type = jsonObject.optString("type");
        }

// Send these Strings to the TextViews for display
        holder.summaryTextView.setText(itemSummary);
        holder.distanceTextView.setText(distance);
        holder.timeTextView.setText(time);
        holder.typeTextView.setText(type);
        holder.typeTextView.setRotation(-90);
        if(type.equalsIgnoreCase("GIVE")){
            holder.rowItemLayout.setBackgroundColor(Color.GRAY);
        }
        else if(type.equalsIgnoreCase("NEED")){
            holder.rowItemLayout.setBackgroundColor(Color.LTGRAY);
        }

        return convertView;
    }

    private String computeDistance(double longitude, double latitude) {
        Location itemLocation = new Location("ItemLocation");
        itemLocation.setLongitude(longitude);
        itemLocation.setLatitude(latitude);

        long distance = Math.round(MainActivity.currentLocation.distanceTo(itemLocation));
        String dist = distance + "m";

        if (distance > 1000) {
            distance = distance / 1000;
            dist = distance + "km";
        }
        return dist;
    }

    private String computeTime(long date) {
        long time = (new Date().getTime() - date)/60000;
        String timeString;
        if(time < 60){
            timeString = time + "min";
        }
        else if(time < 1440){
            timeString = time/60 + "h";
        }
        else{
            timeString = time/1440 + "d";
        }
        return timeString;
    }


    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    // this is used so you only ever have to do
// inflation and finding by ID once ever per View
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView typeTextView;
        public TextView summaryTextView;
        public TextView distanceTextView;
        public TextView timeTextView;
        public RelativeLayout rowItemLayout;
    }
}