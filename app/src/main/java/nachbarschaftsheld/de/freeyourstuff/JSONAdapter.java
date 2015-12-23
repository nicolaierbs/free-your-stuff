package nachbarschaftsheld.de.freeyourstuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

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
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.summaryTextView = (TextView) convertView.findViewById(R.id.text_summary);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.text_description);

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
        if (jsonObject.has("cover_i")) {

            // If so, grab the Cover ID out from the object
            String imageID = jsonObject.optString("cover_i");

            // Construct the image URL (specific to API)
            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";

            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
        } else {

            // If there is no cover ID in the object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }

        // Grab the title and author from the JSON
        String itemSummary = "";
        String itemDescription = "";

        if (jsonObject.has("summary")) {
            itemSummary = jsonObject.optString("summary");
        }

        if (jsonObject.has("description")) {
            itemDescription = jsonObject.optString("description");
        }

// Send these Strings to the TextViews for display
        holder.summaryTextView.setText(itemSummary);
        holder.descriptionTextView.setText(itemDescription);

        return convertView;
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
        public TextView summaryTextView;
        public TextView descriptionTextView;
    }
}