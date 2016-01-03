package de.nachbarschaftsheld.freeyourstuff;

import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import de.nachbarschaftsheld.freeyourstuff.R;

/**
 * Created by nico on 23/12/15.
 */
public class AddItemActivity extends AppCompatActivity {

    private static final String BASE_URL = MainActivity.QUERY_URL + "add";
    private static AsyncHttpClient client = new AsyncHttpClient();

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(MainActivity.TAG,"AddItemActivity started");

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_add_item);

        Button button = (Button) findViewById(R.id.button_submit_item);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewItem();
            }
        });

        username = this.getIntent().getExtras().getString("username");

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void submitNewItem() {

        EditText editTextSummary = (EditText) findViewById(R.id.text_summary);
        EditText editTextDescription = (EditText) findViewById(R.id.text_description);

        String itemType = "NONE";

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioType);
        // find the radiobutton by returned id
        RadioButton selectedRadioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        // do what you want with radioButtonText (save it to database in your case)
        if(selectedRadioButton != null){
            itemType = selectedRadioButton.getText().toString();
        }

        StringEntity entity = null;
        try {
            //String summary = URLEncoder.encode(editTextSummary.getText().toString(), "UTF-8");
            //String description = URLEncoder.encode(editTextDescription.getText().toString(), "UTF-8");
            String summary = editTextSummary.getText().toString();
            String description = editTextDescription.getText().toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("summary", summary);
            jsonObject.put("description", description);
            jsonObject.put("type", itemType);
            jsonObject.put("user", username);
            jsonObject.put("id", Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID));
            Location location = MainActivity.currentLocation;
            jsonObject.put("longitude", location.getLongitude());
            jsonObject.put("latitude", location.getLatitude());
            entity = new StringEntity(jsonObject.toString());
            client.post(this, BASE_URL, entity, "application/json", new AsyncHttpResponseHandler());

            Log.i(MainActivity.TAG, "Submit a new item " + jsonObject.toString());
            finish();

        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
