package edu.illinois.cs.cs125.cs125mp6;

import android.graphics.Color;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    private static boolean choiceMade;

    private static Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        final Button choice1 = findViewById(R.id.choice1);
        final Button choice2 = findViewById(R.id.choice2);
        final Button choice3 = findViewById(R.id.choice3);
        buttons = new Button[]{choice1, choice2, choice3};
        resetButtonColors();
        final TextView result = findViewById(R.id.result);
        try {
            getSongs();
        } catch (Exception e) {
        }
        final Button newLyrics = findViewById(R.id.newLyrics);
        newLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "New Lyrics button clicked");
                newLyrics.setText("NEW LYRICS");
                String[] selectedArtists = new String[2];
                String s1 = songs.get(new Random().nextInt(songs.size()));
                choice1.setText(s1.split(" - ")[0]);
                choice1.setTag(s1.split(" - ")[1]);
                selectedArtists[0] = choice1.getText().toString();
                String s2;
                do {
                    s2 = songs.get(new Random().nextInt(songs.size()));
                    choice2.setText(s2.split(" - ")[0]);
                    choice2.setTag(s2.split(" - ")[1]);
                } while (choice2.getText().toString().equals(selectedArtists[0]));
                selectedArtists[1] = choice2.getText().toString();
                String s3;
                do {
                    s3 = songs.get(new Random().nextInt(songs.size()));
                    choice3.setText(s3.split(" - ")[0]);
                    choice3.setTag(s3.split(" - ")[1]);
                }
                while (choice3.getText().toString().equals(selectedArtists[0]) || choice3.getText().toString().equals
                        (selectedArtists[1]));
                final Button correct = buttons[new Random().nextInt(buttons.length)];
                choice1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Log.d(TAG, "Choice 1 button clicked");
                        if (!choiceMade) {
                            changeButtonColors(correct);
                            choiceMade = true;
                            if (choice1 == correct) {
                                result.setText("Correct!");
                            } else {
                                result.setText("Incorrect!");
                            }
                        }
                    }
                });
                choice2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Log.d(TAG, "Choice 2 button clicked");
                        if (!choiceMade) {
                            changeButtonColors(correct);
                            choiceMade = true;
                            if (choice2 == correct) {
                                result.setText("Correct!");
                            } else {
                                result.setText("Incorrect!");
                            }
                        }
                    }
                });
                choice3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Log.d(TAG, "Choice 3 button clicked");
                        if (!choiceMade) {
                            changeButtonColors(correct);
                            choiceMade = true;
                            if (choice3 == correct) {
                                result.setText("Correct!");
                            } else {
                                result.setText("Incorrect!");
                            }
                        }
                    }
                });
                result.setText("");
                resetButtonColors();
                choiceMade = false;
                getNewLyrics(correct);
            }
        });

    }

    void resetButtonColors() {
        for (Button b : buttons) {
            b.setBackgroundColor(Color.LTGRAY);
        }
    }

    void changeButtonColors(Button correct) {
        for (Button b : buttons) {
            if (b == correct) {
                b.setBackgroundColor(Color.GREEN);
            } else {
                b.setBackgroundColor(Color.RED);
            }
        }
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
                            String formattedLyrics;
                            try {
                                formattedLyrics = formatLyrics(response.get("lyrics").toString());
                            } catch (JSONException e) {
                                Log.d(TAG, "JSONException occurred");
                                return;
                            }
                            lyrics.setText(formattedLyrics);
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

    String formatLyrics(String toFormat) {
        String formattedString = toFormat.replace("\\n", "\n");
        formattedString = formattedString.replace("\\r", "\r");
        formattedString = formattedString.replace("\\\"", "\"");
        return formattedString;
    }

    void getSongs() throws Exception {
        songs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("Songs.txt")));
        String song;
        while ((song = br.readLine()) != null) {
            songs.add(song);
        }
    }
}
