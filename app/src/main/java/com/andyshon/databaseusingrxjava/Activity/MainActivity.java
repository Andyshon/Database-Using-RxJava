package com.andyshon.databaseusingrxjava.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.databaseusingrxjava.Adapter.Adapter;
import com.andyshon.databaseusingrxjava.Database.DatabaseCallback;
import com.andyshon.databaseusingrxjava.Entity.User;
import com.andyshon.databaseusingrxjava.FilterOperation;
import com.andyshon.databaseusingrxjava.R;
import com.andyshon.databaseusingrxjava.Controller.UserController;
import com.andyshon.databaseusingrxjava.UserPreferences;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DatabaseCallback.ViewMain {

    private ListView listView;
    private LinearLayout filterLayout;
    private EditText etFilterAge;
    private Spinner spinnerFilterAge, spinnerFilterGender;
    private String userId;

    public UserController controller;

    private FilterOperation.ByAge filterByAge;
    private FilterOperation.ByGender filterByGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        userId = "";
        controller.getAllUsers();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initUI() {

        controller = new UserController(this);

        loadUserPrefs();
        initSpinners();
        initListeners();

        filterLayout = findViewById(R.id.filterLayout);
        filterLayout.setVisibility(View.GONE);

        listView = findViewById(R.id.listView);
    }


    private void loadUserPrefs () {
        filterByAge = UserPreferences.getMyEnumByAge(this);
        filterByGender = UserPreferences.getMyEnumByGender(this);
        int filterAgeNumber = UserPreferences.getFilterByAge(this);

        etFilterAge = findViewById(R.id.etFilterAge);
        etFilterAge.setText(String.valueOf(filterAgeNumber));
    }


    private void initListeners() {

        Button btnFilter = findViewById(R.id.btnFilter);
        Button btnFilterApply = findViewById(R.id.btnApply);
        Button btnNoFilter = findViewById(R.id.btnNoFilter);
        Button btnRefreshUsers = findViewById(R.id.btnGetAllUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);


        btnFilterApply.setOnClickListener(view -> {

            filterByAge = (FilterOperation.ByAge) spinnerFilterAge.getSelectedItem();
            filterByGender = (FilterOperation.ByGender) spinnerFilterGender.getSelectedItem();

            UserPreferences.SaveUserPreferences(this, filterByAge, filterByGender, Integer.parseInt(etFilterAge.getText().toString()));

            controller.applyFilter(Integer.parseInt(etFilterAge.getText().toString()), filterByAge, filterByGender);
        });


        btnFilter.setOnClickListener(view -> {
            filterLayout.setVisibility(View.VISIBLE);
        });


        btnNoFilter.setOnClickListener(view -> {
            filterLayout.setVisibility(View.GONE);
            controller.getAllUsers();
        });


        btnRefreshUsers.setOnClickListener(view -> {
            if (filterLayout.getVisibility() == View.VISIBLE) {
                controller.applyFilter(Integer.parseInt(etFilterAge.getText().toString()), filterByAge, filterByGender);
            }
            else {
                controller.getAllUsers();
            }
        });


        btnAddUser.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }


    private void initSpinners() {

        spinnerFilterAge = findViewById(R.id.spinnerFilterAge);
        spinnerFilterAge.setAdapter(new ArrayAdapter<FilterOperation.ByAge>(this, R.layout.simple_spinner_item, FilterOperation.ByAge.values()));

        switch (filterByAge.toString()) {
            case "age less than":
                spinnerFilterAge.setSelection(0);
                break;
            case "age more than":
                spinnerFilterAge.setSelection(1);
                break;
            default:
                spinnerFilterAge.setSelection(2);
                break;
        }


        spinnerFilterGender = findViewById(R.id.spinnerFilterGender);
        spinnerFilterGender.setAdapter(new ArrayAdapter<FilterOperation.ByGender>(this, R.layout.simple_spinner_item, FilterOperation.ByGender.values()));

        switch (filterByGender.toString()) {
            case "male":
                spinnerFilterGender.setSelection(0);
                break;
            case "female":
                spinnerFilterGender.setSelection(1);
                break;
            default:
                spinnerFilterGender.setSelection(3);
                break;
        }
    }


    private void setAdapter(List<User> users) {
        Adapter adapter = new Adapter(this);
        for (User user : users){
            adapter.add(user);
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            userId = (((TextView)view.findViewById(R.id.user_id)).getText().toString());

            Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }


    @Override
    public void onGetAllUsers(List<User> users) {
        setAdapter(users);
    }


    @Override
    public void onGetUsersWithFilter(List<User> usersByAge) {
        setAdapter(usersByAge);
    }


    @Override
    public void onDataNotAvailable() {
        Toast.makeText(this, "Data Not Available!", Toast.LENGTH_SHORT).show();
    }
}
