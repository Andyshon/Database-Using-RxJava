package com.andyshon.databaseusingrxjava.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andyshon.databaseusingrxjava.Adapter.Adapter;
import com.andyshon.databaseusingrxjava.Database.DatabaseCallback;
import com.andyshon.databaseusingrxjava.Entity.User;
import com.andyshon.databaseusingrxjava.R;
import com.andyshon.databaseusingrxjava.Controller.UserController;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DatabaseCallback.ViewMain {

    private ListView listView;
    private String userId;

    private UserController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }


    @Override
    protected void onResume() {
        super.onResume();
        userId = "";
        controller.getAllUsers();
    }


    private void initUI() {

        controller = new UserController(this);

        Button btnGetAllUsers = findViewById(R.id.btnGetAllUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);

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
    public void onDataNotAvailable() {
        Toast.makeText(this, "Data Not Available!", Toast.LENGTH_SHORT).show();
    }
}
