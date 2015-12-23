package nachbarschaftsheld.de.freeyourstuff;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "username";
    SharedPreferences mSharedPreferences;

    private static final String QUERY_URL = "http://130.83.104.54:8080/freeyourstuff/rest/freeyourstuff/data/get";

    private static String username;

    private static SwipeRefreshLayout swipeView;

    ListView mainListView;

    ProgressDialog mDialog;

    JSONAdapter mJSONAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeView.setOnRefreshListener(this);

        // Greet the user, or ask for their name if new
        displayWelcome();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add another item", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Searching for items");
        mDialog.setCancelable(false);

        // Create a JSONAdapter for the ListView
        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());

        // Set the ListView to use the ArrayAdapter
        mainListView = (ListView) findViewById(R.id.main_listview);
        mainListView.setOnItemClickListener(this);
        mainListView.setAdapter(mJSONAdapter);

        queryItems("Moby Dick");
    }

     public void displayWelcome() {

        // Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Read the user's name,
        // or an empty string if nothing found
        username = mSharedPreferences.getString(PREF_NAME, "");

        if (username.length() > 0) {

            // If the name is valid, display a Toast welcoming them
            Toast.makeText(this, "Welcome back, " + username + "!", Toast.LENGTH_LONG).show();

        } else {

            // otherwise, show a dialog to ask for their name
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("You're great!");
            alert.setMessage("Which user name would you like to use?");

            // Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);

            // Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    String username = input.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, username);
                    e.commit();

                    // Welcome the new user
                    Toast.makeText(getApplicationContext(), "Welcome, " + username + "!", Toast.LENGTH_LONG).show();
                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {}
            });

            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void queryItems(String searchString) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        // Show ProgressDialog to inform user that a task in the background is occurring
        mDialog.show();

        // Have the client get a JSONArray of data
        // and define how to respond
        Log.i("freeyourstuff",QUERY_URL);
        //client.get(QUERY_URL + urlString,
        client.get(QUERY_URL,
                        new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        // Dismiss the ProgressDialog
                        mDialog.dismiss();
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Items updated!",Toast.LENGTH_LONG).show();
                        Log.d("freeyourstuff", jsonArray.toString());

                        mJSONAdapter.updateData(jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Dismiss the ProgressDialog
                        mDialog.dismiss();
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("omg android", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 12. Now that the user's chosen a book, grab the cover data
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String coverID = jsonObject.optString("cover_i","");
        String summary = jsonObject.optString("summary","");
        String description = jsonObject.optString("description","");

// create an Intent to take you over to a new DetailActivity
        Intent detailIntent = new Intent(this, DetailActivity.class);

// pack away the data about the cover
// into your Intent before you head out
        detailIntent.putExtra("coverID", coverID);
        detailIntent.putExtra("summary", summary);
        detailIntent.putExtra("description", description);

// start the next Activity using your prepared Intent
        startActivity(detailIntent);
    }

    @Override
    public void onRefresh() {
        Log.i("freeyourstuff", "Swyping refresh");
        queryItems("Jane Austen");
        swipeView.setRefreshing(false);
    }
}
