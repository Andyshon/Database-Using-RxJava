package com.andyshon.databaseusingrxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements UserInterface.View {

    private Button btnGetAllUsers, btnAddUser;
    private TextView tv;
    private String userName;

    UserController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();

        controller = new UserController(this);

        // one time we need to insert users
        //controller.insertUsers();
        //controller.removeAllDatabase();
    }


    private void initUI() {
        userName = "";
        btnGetAllUsers = findViewById(R.id.btnGetAllUsers);
        btnAddUser = findViewById(R.id.btnAddUser);
        tv = findViewById(R.id.tv);

        btnGetAllUsers.setOnClickListener(view -> {
            controller.getAllUsers();
        });
        btnAddUser.setOnClickListener(view -> {
            /*Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);*/
        });
    }


    @Override
    public void populateData(List</*Api*/User> apiUserList) {
        tv.setText("");
        for (/*Api*/User user : apiUserList)
            tv.setText(tv.getText().toString().concat(user.getName()).concat(" "));
    }
}
