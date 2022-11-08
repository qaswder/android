package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab5.db.DatabaseHandler;
import com.example.lab5.db.User;

import java.util.HashSet;
import java.util.Set;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    Button settingEditBut, settingDelBut;
    EditText oldPass, newPass;
    SharedPreferences preferences;
    DatabaseHandler db;
    String log, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("listApp", MODE_PRIVATE);
        db = new DatabaseHandler(this);

        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        settingDelBut = findViewById(R.id.settingDelBut);
        settingEditBut = findViewById(R.id.settingEditBut);

        settingEditBut.setOnClickListener(this);
        settingDelBut.setOnClickListener(this);

        Bundle arguments = getIntent().getExtras();
        String loginDB = arguments.get("login").toString();
        String passDB = arguments.get("pass").toString();

        log = loginDB;
        pass = passDB;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settingEditBut:
                updatePass();
                break;
            case R.id.settingDelBut:
                deleteUserPref();
                break;
            default:
                break;
        }
    }

    public void updatePass(){
        String passwordNew = newPass.getText().toString();

        if(pass.equals(oldPass.getText().toString()))
            db.updatePassword(log, passwordNew);
    }
    public void deleteUserPref(){
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear().commit();
        db.deleteUser(log);

        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
    }


}