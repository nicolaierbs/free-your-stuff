package nachbarschaftsheld.de.freeyourstuff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);
        TextView textSummaryView = (TextView) findViewById(R.id.text_detail_summary);
        TextView textDescriptionView = (TextView) findViewById(R.id.text_detail_description);

        // Unpack the parameters from its trip inside your Intent
        String coverID = this.getIntent().getExtras().getString("coverID");
        String summary = this.getIntent().getExtras().getString("summary");
        String description = this.getIntent().getExtras().getString("description");

        // See if there is a valid coverID
        if (coverID.length() > 0) {
            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";
            // Use Picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.drawable.img_books_loading).into(imageView);
        }



    }

}
