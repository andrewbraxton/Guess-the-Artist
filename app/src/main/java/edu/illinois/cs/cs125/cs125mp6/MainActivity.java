package edu.illinois.cs.cs125.cs125mp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /**
     * Default logging tag for messages from the main activity.
     */
    private static final String TAG = "MP6:Main";

    /**
     * Request queue for our network requests.
     */
    private static RequestQueue requestQueue;

    private static ArrayList<String> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        songs = new ArrayList<String>();
        songs.add("Young Thug-Halftime");
        songs.add("Denzel Curry-Ultimate");
        songs.add("Travis Scott-Sicko Mode");
        songs.add("Chief Keef-Faneto");
        songs.add("Rush-Tom Sawyer");

        String answer = "";

        // Attach the handler to our UI button


        final Button choice1 = findViewById(R.id.choice1);
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Choice 1 button clicked");
            }
        });
        final Button choice2 = findViewById(R.id.choice2);
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Choice 2 button clicked");
            }
        });
        final Button choice3 = findViewById(R.id.choice3);
        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Choice 3 button clicked");
            }
        });

        final Button newLyrics = findViewById(R.id.newLyrics);
        newLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "New Lyrics button clicked");

                String s1 = songs.get(new Random().nextInt(songs.size()));
                choice1.setText(s1.split("-")[0]);
                choice1.setTag(s1.split("-")[1]);
                String s2 = songs.get(new Random().nextInt(songs.size()));
                choice2.setText(s2.split("-")[0]);
                choice2.setTag(s2.split("-")[1]);
                String s3 = songs.get(new Random().nextInt(songs.size()));
                choice3.setText(s3.split("-")[0]);
                choice3.setTag(s3.split("-")[1]);

                Button[] buttons = {choice1, choice2, choice3};

                Button correct = buttons[new Random().nextInt(buttons.length)];

                getNewLyrics(correct);
            }
        });

    }

    void getNewLyrics(Button correct) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.lyrics.ovh/v1/" + correct.getText() + "/" + correct.getTag(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            final TextView lyrics = findViewById(R.id.lyrics);
                            lyrics.setText(response.toString());
                            lyrics.setMovementMethod(new ScrollingMovementMethod());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.w(TAG, error.toString());
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
