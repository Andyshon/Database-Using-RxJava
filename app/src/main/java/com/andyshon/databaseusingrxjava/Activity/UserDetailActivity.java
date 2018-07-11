package com.andyshon.databaseusingrxjava.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.andyshon.databaseusingrxjava.Database.DatabaseCallback;
import com.andyshon.databaseusingrxjava.Database.UserContract;
import com.andyshon.databaseusingrxjava.Entity.User;
import com.andyshon.databaseusingrxjava.R;
import com.andyshon.databaseusingrxjava.Controller.UserController;

public class UserDetailActivity extends AppCompatActivity implements DatabaseCallback.ViewDetail {

    private EditText etName, etAge, etCity;
    private CheckBox cbGenderMale, cbGenderFemale;
    private String userId;

    private UserController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initUI();
    }


    private void initUI() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        controller = new UserController(this);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etCity = findViewById(R.id.etCity);
        cbGenderMale = findViewById(R.id.cbGenderMale);
        cbGenderFemale = findViewById(R.id.cbGenderFemale);

        cbGenderMale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) cbGenderFemale.setChecked(false);
        });

        cbGenderFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) cbGenderMale.setChecked(false);
        });

        Button btnAddOrUpdateUser = findViewById(R.id.btnAddOrUpdateUser);
        btnAddOrUpdateUser.setOnClickListener(view -> AddOrUpdateUser());

        Button btnDeleteUser = findViewById(R.id.btnDeleteUser);
        btnDeleteUser.setOnClickListener(view -> deleteUser(Integer.parseInt(userId), etName.getText().toString()));


        if (userId != null && !userId.isEmpty()) {
            controller.getUserById(userId);
        }
    }


    private void deleteUser(int id, String name) {
        controller.deleteUser(id, name);
    }


    private void AddOrUpdateUser() {

        int gender = UserContract.UserEntry.GENDER_UNKNOWN;
        if (cbGenderMale.isChecked()) gender = UserContract.UserEntry.GENDER_MALE;
        else if (cbGenderFemale.isChecked()) gender = UserContract.UserEntry.GENDER_FEMALE;

        if (userId.isEmpty() || userId == null) {
            //todo: add new user

            if (etName.getText().toString().isEmpty() || etAge.getText().toString().isEmpty()
                    || etCity.getText().toString().isEmpty() || gender == UserContract.UserEntry.GENDER_UNKNOWN) {
                Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show();
            }
            else {
                ContentValues values = new ContentValues();
                values.put(UserContract.UserEntry.COLUMN_NAME, etName.getText().toString().trim());
                values.put(UserContract.UserEntry.COLUMN_AGE, etAge.getText().toString().trim());
                values.put(UserContract.UserEntry.COLUMN_CITY, etCity.getText().toString().trim());
                values.put(UserContract.UserEntry.COLUMN_GENDER, gender);

                controller.addUser(values);
            }
        }
        else {
            //todo: update user

            controller.updateUser(Integer.parseInt(userId), etName.getText().toString(), Integer.parseInt(etAge.getText().toString()),
                    etCity.getText().toString(), gender);
        }

    }


    @Override
    public void onUserAdded(String name) {
        Toast.makeText(this, "User " + name + " was added!", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onGetUserById(User user) {
        etName.setText(user.getName());
        etAge.setText(String.valueOf(user.getAge()));
        etCity.setText(user.getCity());
        if (user.getGender() == UserContract.UserEntry.GENDER_FEMALE)
            cbGenderFemale.setChecked(true);
        else
            cbGenderMale.setChecked(true);
    }


    @Override
    public void onUserDeleted(String name) {
        Toast.makeText(this, "User " + name + " was deleted!", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onUserUpdate(String name) {
        Toast.makeText(this, "user " + name + " was updated!", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onDataNotAvailable() {
        Toast.makeText(this, "Data Not Available!", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
