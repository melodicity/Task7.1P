package com.example.task71p.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.task71p.R;
import com.example.task71p.data.DatabaseHelper;
import com.example.task71p.model.Item;

import java.util.Arrays;

public class CreateActivity extends AppCompatActivity {

    // Declare views
    RadioGroup rgPostType;
    RadioButton rbLost, rbFound, rbSelected;
    EditText etName, etPhone, etDescription, etDate, etLocation;
    Button btnSave;

    // Database helper used for inserting new items
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise views
        rgPostType = findViewById(R.id.rgPostType);
        rbLost = findViewById(R.id.rbLost);
        rbFound = findViewById(R.id.rbFound);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        btnSave = findViewById(R.id.btnSave);

        // On "save" click, add the item to the DB and go back
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Declare input variables
                String name, phone, description, date, location;

                // Validate post type radio buttons
                if (rgPostType.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(CreateActivity.this, "Please select a post type", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Get selected radio button from the radio group
                    int selectedRadioButtonId = rgPostType.getCheckedRadioButtonId();
                    // Find the radio button by returned id
                    rbSelected = (RadioButton) findViewById(selectedRadioButtonId);
                }

                // Validate edit text fields
                if (etName.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty()
                        || etDescription.getText().toString().isEmpty() || etDate.getText().toString().isEmpty()
                        || etLocation.getText().toString().isEmpty()) {
                    Toast.makeText(CreateActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get inputs from (now validated) fields
                name = rbSelected.getText().toString() + " " + etName.getText().toString();
                phone = etPhone.getText().toString();
                description = etDescription.getText().toString();
                date = etDate.getText().toString();
                location = etLocation.getText().toString();

                // Create a new item from fields
                Item item = new Item(name, phone, description, date, location);

                // Insert the item into the database
                db = new DatabaseHelper(CreateActivity.this);
                long newRowId = db.insertItem(item);
                if (newRowId <= 0) {
                    Toast.makeText(CreateActivity.this, "Error saving to database", Toast.LENGTH_SHORT).show();
                    return;
                }

                finish(); // closes this activity
            }
        });
    }
}