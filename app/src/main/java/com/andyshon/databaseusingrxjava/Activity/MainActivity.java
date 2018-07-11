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
    private String userId;

    public UserController controller;

    private FilterOperation.ByAge filterByAge;
    private FilterOperation.ByGender filterByGender;
    private int filterAgeNumber;


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
        //userId = "";
        //controller.getAllUsers();
    }


    private void initUI() {

        filterByAge = UserPreferences.getMyEnumByAge(this);
        System.out.println("FILTER:" + filterByAge);
        filterByGender = UserPreferences.getMyEnumByGender(this);
        System.out.println("FILTER GENDER:" + filterByGender);
        filterAgeNumber = UserPreferences.getFilterByAge(this);
        System.out.println("USERNUMBER:" + filterAgeNumber);

        controller = new UserController(this);

        filterLayout = findViewById(R.id.filterLayout);
        filterLayout.setVisibility(View.GONE);

        EditText etFilterAge = findViewById(R.id.etFilterAge);

        etFilterAge.setText(String.valueOf(filterAgeNumber));

        Spinner spinnerFilterAge = findViewById(R.id.spinnerFilterAge);
        spinnerFilterAge.setAdapter(new ArrayAdapter<FilterOperation.ByAge>(this, R.layout.simple_spinner_item, FilterOperation.ByAge.values()));

        if (filterByAge.toString().equals("age equals")) {
            spinnerFilterAge.setSelection(2);
        }
        else if (filterByAge.toString().equals("less than")) {
            spinnerFilterAge.setSelection(0);
        }
        else spinnerFilterAge.setSelection(1);


        Spinner spinnerFilterGender = findViewById(R.id.spinnerFilterGender);
        spinnerFilterGender.setAdapter(new ArrayAdapter<FilterOperation.ByGender>(this, R.layout.simple_spinner_item, FilterOperation.ByGender.values()));

        if (filterByGender.toString().equals("male")) {
            spinnerFilterGender.setSelection(0);
        }
        else if (filterByGender.toString().equals("female")) {
            spinnerFilterGender.setSelection(1);
        }
        else spinnerFilterGender.setSelection(3);


        Button btnFilter = findViewById(R.id.btnFilter);
        Button btnNoFilter = findViewById(R.id.btnNoFilter);
        Button btnFilterApply = findViewById(R.id.btnApply);
        Button btnGetAllUsers = findViewById(R.id.btnGetAllUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        btnFilter.setOnClickListener(view -> {
            filterLayout.setVisibility(View.VISIBLE);
        });

        btnNoFilter.setOnClickListener(view -> {
            filterLayout.setVisibility(View.GONE);
            controller.getAllUsers();
        });

        btnFilterApply.setOnClickListener(view -> {


            filterByAge = (FilterOperation.ByAge) spinnerFilterAge.getSelectedItem();
            System.out.println("filter By Age:" + filterByAge);

            filterByGender = (FilterOperation.ByGender) spinnerFilterGender.getSelectedItem();
            System.out.println("filter By Gender:" + filterByGender);

            UserPreferences.SaveUserPreferences(this, filterByAge, filterByGender, Integer.parseInt(etFilterAge.getText().toString()));

            // int -> by age NO
            controller.applyFilter(Integer.parseInt(etFilterAge.getText().toString()), filterByAge, filterByGender);
        });

        btnGetAllUsers.setOnClickListener(view -> {
            controller.getAllUsers();
        });

        btnAddUser.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        listView = findViewById(R.id.listView);
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
    public void onGetUsersByAge(List<User> usersByAge) {
        setAdapter(usersByAge);
    }


    @Override
    public void onDataNotAvailable() {
        Toast.makeText(this, "Data Not Available!", Toast.LENGTH_SHORT).show();
    }
}
