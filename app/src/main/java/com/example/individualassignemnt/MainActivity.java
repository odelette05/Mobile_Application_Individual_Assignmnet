package com.example.individualassignemnt;

import static android.content.Context.MODE_PRIVATE;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREFS_KEY_FIRST_TIME_USER = "isFirstTimeUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstTimeUser = preferences.getBoolean(PREFS_KEY_FIRST_TIME_USER, true);

        if (isFirstTimeUser) {
            // If it's the first time, show the "Get Started" page and mark the user as seen.
            showGetStartedPage();

           SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREFS_KEY_FIRST_TIME_USER, false);
            editor.apply();
        } else {
            startActivity(new Intent(this, FriendActivity.class));
        }
    }

    private void showGetStartedPage() {
        Intent intent = new Intent(this, GetStartedActivity.class);
        startActivity(intent);
        finish(); // Finish the MainActivity so the user can't go back to it using the back button.
    }
}