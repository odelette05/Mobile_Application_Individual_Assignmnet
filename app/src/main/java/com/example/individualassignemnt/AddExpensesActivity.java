package com.example.individualassignemnt;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddExpensesActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private EditText Title;
    private Button distribution;
    private MultiAutoCompleteTextView autoCompleteTextViewContacts;
    private ArrayAdapter<String> contactAdapter;
    private ArrayList<String> selectedContacts = new ArrayList<>();
    private ArrayList<String> distribution_list = new ArrayList<>();
    private String distribution_selected;
    private com.example.individualassignemnt.SQLiteAdapter sqLiteAdapter; // SQLiteAdapter instance


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Expense");
        setContentView(R.layout.activity_add_expenses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextAmount = findViewById(R.id.amount);
        editTextAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5)});
        distribution = findViewById(R.id.btn_distribution);
        Title = findViewById(R.id.expense_title);

        // Initialize views
        autoCompleteTextViewContacts = findViewById(R.id.friend_enter);
        // ... (initialize other views)

        // Set up contact suggestion adapter
        contactAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line);
        sqLiteAdapter = new SQLiteAdapter(this);
        autoCompleteTextViewContacts.setAdapter(contactAdapter);
        selectedContacts.clear();
        distribution_list.clear();
        selectedContacts.add("You");
        autoCompleteTextViewContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactPopupMenu(view);
            }
        });

        distribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFixedOptionsDialog();
            }
        });

    }

    private void showFixedOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Distribution");
        if (distribution_list.isEmpty()) {
            distribution_list.add("Even");
            distribution_list.add("By Percentage");
            distribution_list.add("By Ratio");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, distribution_list);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the selected option
                distribution_selected = distribution_list.get(which);
                distribution.setText(distribution_selected);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    private void showContactPopupMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        ArrayList<String> contactNames = fetchContactNames();

        for (String contactName : contactNames) {
            popupMenu.getMenu().add(contactName);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Append selected contact to the existing text
                String selectedContact = item.getTitle().toString();
                if (!selectedContacts.contains(selectedContact)) {
                    selectedContacts.add(selectedContact);
                    updateContactText();
                }
                return true;
            }
        });

        popupMenu.show();
    }

    private ArrayList<String> fetchContactNames() {
        ArrayList<String> contactNames = new ArrayList<>();

        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int displayNameIndex = ((Cursor) cursor).getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String contactName = cursor.getString(displayNameIndex);
                contactNames.add(contactName);
            }

            cursor.close();
        }

        return contactNames;
    }

    private void updateContactText() {
        StringBuilder contactsText = new StringBuilder();
        for (String contact : selectedContacts) {
            if (contactsText.length() > 0) {
                contactsText.append(", ");
            }
            contactsText.append(contact);
        }
        autoCompleteTextViewContacts.setText(contactsText.toString());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                if (checkInput()) {
                    if ("Even".equals(distribution_selected)) {
                        equal_calculation();
                    } else if ("By Percentage".equals(distribution_selected)) {
                        showPercentageDistributionDialog(); // Call the method to create AlertDialog
                    } else if ("By Ratio".equals(distribution_selected)) {
                        ratio_calculation();
                    }
                }
                return true;
            case R.id.home:
                startActivity(new Intent(AddExpensesActivity.this,FriendActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void showPercentageDistributionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpensesActivity.this);

        LinearLayout layout = new LinearLayout(AddExpensesActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        builder.setView(layout);

        LayoutInflater inflater = LayoutInflater.from(AddExpensesActivity.this);

        for (String name : selectedContacts) {

            LinearLayout layoutH = new LinearLayout(AddExpensesActivity.this);
            layoutH.setOrientation(LinearLayout.HORIZONTAL);

            View rowView = inflater.inflate(R.layout.fill_in_percentage, null);
            TextView nameTextView = rowView.findViewById(R.id.Friends);
            nameTextView.setText(name);


            layoutH.addView(rowView);
            layout.addView(layoutH);

        }

        builder.setTitle("Percentage Distribution")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressLint("WrongViewCast")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle calculation of percentages here
                        // For example, you can get the percentage entered in the EditText:
                        double[] percentages = new double[selectedContacts.size()];
                        double total = 0;
                        double price1 = 0;

                        for (int i = 0; i < selectedContacts.size(); i++) {
                            View listItemView = layout.getChildAt(i);
                            EditText percentageEditText = listItemView.findViewById(R.id.Percentage_enter); // Corrected line
                            String percentageText = percentageEditText.getText().toString();
                            double percentageValue = Double.parseDouble(percentageText);
                            total += percentageValue;
                            percentages[i] = percentageValue;
                        }

                        EditText price = findViewById(R.id.amount);
                        String prices = price.getText().toString();
                        price1 = Double.parseDouble(prices);


                        if (total != 100) {
                            Toast.makeText(AddExpensesActivity.this, "Make sure that the total of the percentage is 100!", Toast.LENGTH_LONG).show();
                        } else {
                            double[] amount_distributed = new double[selectedContacts.size()];
                            for (int j = 0; j < selectedContacts.size(); j++) {
                                amount_distributed[j] = (percentages[j] / 100) * price1;
                            }
                            String Desc = Title.getText().toString();
                            // Store in Database
                            for (int j = 0; j < selectedContacts.size(); j++) {
                                sqLiteAdapter.openToWrite();
                                sqLiteAdapter.insert(selectedContacts.get(j), Desc, String.format("%.2f",amount_distributed[j]));
                                sqLiteAdapter.close();
                                Intent intent = new Intent(AddExpensesActivity.this, FriendActivity.class);
                                startActivity(intent);
                            }

                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog customDialog = builder.create();
        customDialog.show();
    }

        private void equal_calculation() {
        Title = findViewById(R.id.expense_title);
        String amountText = editTextAmount.getText().toString();
        if (!amountText.isEmpty()) {
            double amount = Double.parseDouble(amountText);
            double amount_distributed = amount / selectedContacts.size();
            String Desc = Title.getText().toString();
            sqLiteAdapter.openToWrite();
            for (int i = 0; i < selectedContacts.size(); i++) {
                sqLiteAdapter.insert(selectedContacts.get(i), Desc, String.format("%.2f",amount_distributed));
            }
            sqLiteAdapter.close();
            Intent intent = new Intent(AddExpensesActivity.this, FriendActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(AddExpensesActivity.this, "Please enter an amount!", Toast.LENGTH_LONG).show();
        }
    }


    private void ratio_calculation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpensesActivity.this);

        LinearLayout layout = new LinearLayout(AddExpensesActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        builder.setView(layout);

        LayoutInflater inflater = LayoutInflater.from(AddExpensesActivity.this);

        for (String name : selectedContacts) {

            LinearLayout layoutH = new LinearLayout(AddExpensesActivity.this);
            layoutH.setOrientation(LinearLayout.HORIZONTAL);

            View rowView = inflater.inflate(R.layout.fill_in_ratio, null);
            TextView nameTextView = rowView.findViewById(R.id.Friends);
            nameTextView.setText(name);


            layoutH.addView(rowView);
            layout.addView(layoutH);

        }

        builder.setTitle("Ratio Distribution")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressLint("WrongViewCast")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle calculation of percentages here
                        // For example, you can get the percentage entered in the EditText:
                        distribution_selected = "By Ratio";
                        double[] ratio = new double[selectedContacts.size()];
                        double total = 0;
                        double price1 = 0;

                        for (int i = 0; i < selectedContacts.size(); i++) {
                            View listItemView = layout.getChildAt(i);
                            EditText ratioEditText = listItemView.findViewById(R.id.ratio_enter); // Corrected line
                            String ratioText = ratioEditText.getText().toString();
                            double ratioValue = Double.parseDouble(ratioText);
                            total += ratioValue;
                            ratio[i] = ratioValue;
                        }

                        EditText price = findViewById(R.id.amount);
                        String prices = price.getText().toString();
                        price1 = Double.parseDouble(prices);



                        double[] amount_distributed = new double[selectedContacts.size()];
                        for (int j = 0; j < selectedContacts.size(); j++) {
                            amount_distributed[j] = (ratio[j] / total) * price1;
                        }
                        String Desc = Title.getText().toString();
                        // Store in Database
                        for (int j = 0; j < selectedContacts.size(); j++) {
                            sqLiteAdapter.openToWrite();
                            sqLiteAdapter.insert(selectedContacts.get(j),Desc, String.format("%.2f",amount_distributed[j]));
                            sqLiteAdapter.close();
                            Intent intent = new Intent(AddExpensesActivity.this, FriendActivity.class);
                            startActivity(intent);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog customDialog = builder.create();
        customDialog.show();
    }


    private boolean checkInput() {
        double amount = Double.parseDouble(String.valueOf(editTextAmount.getText()));
        if (amount == 0) {
            Toast.makeText(AddExpensesActivity.this, "Please key in the correct value!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (selectedContacts.size() < 2) {
            Toast.makeText(AddExpensesActivity.this, "Please insert at least 1 people!", Toast.LENGTH_LONG).show();
            return false;
        }

        String title = Title.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(AddExpensesActivity.this, "Please insert a title!", Toast.LENGTH_LONG).show();
            return false;
        }

        ArrayList<String> selections = new ArrayList<>();
        selections.add("Even");
        selections.add("By Percentage");
        selections.add("By Ratio");
        if (!selections.contains(distribution_selected)) {
            Toast.makeText(AddExpensesActivity.this, "Please choose a distribution!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    class DecimalDigitsInputFilter implements InputFilter {
        private final int decimalDigits;

        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.replace(dstart, dend, source.subSequence(start, end).toString());
            if (!builder.toString().matches("(([1-9]){1}([0-9]{0," + (decimalDigits) + "})?)?(\\.[0-9]{0," + (decimalDigits - 3) + "})?")) {
                if (source.length() == 0) return dest.subSequence(dstart, dend);
                return "";
            }

            return null;
        }

    }

}