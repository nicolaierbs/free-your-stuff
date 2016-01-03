package de.nachbarschaftsheld.freeyourstuff;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.nachbarschaftsheld.freeyourstuff.R;

/**
 * Created by nico on 22/12/15.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/"; // 13
    String mImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_detail);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Access the imageview from XML
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.detail_view);
        TextView textType = (TextView) findViewById(R.id.text_detail_type);
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);
        TextView textSummaryView = (TextView) findViewById(R.id.text_detail_summary);
        TextView textDescriptionView = (TextView) findViewById(R.id.text_detail_description);
        TextView textUserView = (TextView) findViewById(R.id.text_detail_user);

        // Unpack the parameters from its trip inside your Intent
        String coverID = this.getIntent().getExtras().getString("coverID");
        String user = this.getIntent().getExtras().getString("user");
        String type = this.getIntent().getExtras().getString("type");
        String summary = this.getIntent().getExtras().getString("summary");
        String description = this.getIntent().getExtras().getString("description");

        Log.i(MainActivity.TAG, "Showing details: " + user + ", " + type + ", " + summary + ", " + description);

        // See if there is a valid coverID
        if (coverID.length() > 0) {
            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";
            // Use Picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.drawable.img_books_loading).into(imageView);
        }

        if (summary.length() > 0) {
            textSummaryView.setText(summary);
        }

        if (description.length() > 0) {
            textDescriptionView.setText(description);
        }

        if (type.length() > 0 ) {
            textType.setText(type);
        }

        if(type.length() > 0){
            if(type.equalsIgnoreCase("GIVE")){
                layout.setBackgroundColor(Color.GRAY);
            }
            else if(type.equalsIgnoreCase("NEED")){
                layout.setBackgroundColor(Color.LTGRAY);
            }
        }

        if (user.length() > 0 ) {
            textUserView.setText("User: " + user);
        }



    }

}
