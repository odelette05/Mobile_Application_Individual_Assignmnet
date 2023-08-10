package com.example.individualassignemnt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("History");

        // Set the content view to your activity layout
        setContentView(R.layout.activity_friend);

        // Initialize the button
        Button btn_expenses = findViewById(R.id.btn_createExpense);
        btn_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FriendActivity.this, AddExpensesActivity.class));
            }
        });

        // Initialize the linear layout
        LinearLayout ll = findViewById(R.id.history);

        // Remove all views from the linear layout to clear previous content
        ll.removeAllViews();

        // Create and initialize a new text view
        TextView listContent = new TextView(this);
        listContent.setTextSize(20.0f);

        // Set text view content
        SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(this);
        sqLiteAdapter.openToRead();

        String Content = sqLiteAdapter.queueAll();
        sqLiteAdapter.close();

        listContent.setText(Content);

        // Add the new text view to the linear layout
        ll.addView(listContent);


    }
}