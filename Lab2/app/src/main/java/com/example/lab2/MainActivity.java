package com.example.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editText1, editText2;
    Button singIn, singUp;
    SharedPreferences preferences;

    final String LOGIN = "login";
    final String PASS = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.login);
        editText2 = findViewById(R.id.pass);
        singIn = findViewById(R.id.singIn);
        singUp = findViewById(R.id.singUp);
        preferences = getSharedPreferences("authentication", MODE_PRIVATE);

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String login = preferences.getString(LOGIN,"");
                String pass = preferences.getString(PASS, "");
                int condition = 0;

                String cmpLogin = editText1.getText().toString();
                String cmpPass = editText2.getText().toString();

                if(cmpLogin.equals(login)){
                    condition += 1;
                    editText1.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "Неверный логин", Toast.LENGTH_SHORT).show();
                    editText1.setText("");
                    condition = 0;
                }

                if (cmpPass.equals(pass)){
                    condition += 1;
                    editText2.setText("");

                }else{
                    Toast.makeText(MainActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    editText2.setText("");
                    condition = 0;
                }

                if(condition == 2){
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra(LOGIN, cmpLogin);
                    startActivity(intent);
                }

            }
        });

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(LOGIN, editText1.getText().toString());
                editor.putString(PASS, editText2.getText().toString());
                editor.apply();
                Toast.makeText(MainActivity.this, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show();

            }
        });
    }
}