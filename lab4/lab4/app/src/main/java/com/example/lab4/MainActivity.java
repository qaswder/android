package com.example.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    EditText editText1, editText2;
    Button singIn, singUp, lang;
    SharedPreferences preferences_main;

    final String LOGIN = "login";
    final String PASS = "pass";
    final String STATE = "state";
    int state = 0;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText1 = findViewById(R.id.login);
        editText2 = findViewById(R.id.pass);
        singIn = findViewById(R.id.singIn);
        singUp = findViewById(R.id.singUp);
        lang = findViewById(R.id.lang);
        preferences_main = getSharedPreferences("authentication", MODE_PRIVATE);

            singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String login = preferences_main.getString(LOGIN,"нет данных");
                String pass = preferences_main.getString(PASS, "нет данных");
                Log.i("login", login);
                Log.i("password", pass);
                int condition = 0;

                if (!(pass.isEmpty() && login.isEmpty())){
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

            }
        });

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences_main.edit();
                editor.putString(LOGIN, editText1.getText().toString());
                editor.putString(PASS, editText2.getText().toString());
                editor.apply();
                Toast.makeText(MainActivity.this, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show();
            }
        });

        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count  += 1;
                if(count % 2 == 0){
                    state = 0;

                }else
                    state = 1;
                setLang(state);

                Log.i("state", String.valueOf(state));
                Log.i("count", String.valueOf(count));

            }
        });
    }
    public void save(){
        SharedPreferences.Editor editor = preferences_main.edit();
        editor.putInt(STATE, state);
        editor.apply();
        Log.i("stateSave", String.valueOf(state));
        int loadState = preferences_main.getInt(STATE,3);

        Log.i("loooad", String.valueOf(loadState));

        Toast.makeText(MainActivity.this, "SaveState", Toast.LENGTH_SHORT).show();
    }
    public void load(){

        int loadState = preferences_main.getInt(STATE,3);
        state = loadState;
        Log.i("stateLoad", String.valueOf(state));
    }
    public void setLang(int stateLang){
        if(stateLang == 1){
            editText1.setHint(R.string.nameEn);
            editText2.setHint(R.string.passEn);
            singIn.setText(R.string.singInEn);
            singUp.setText(R.string.singUpEn);
        }
        if(stateLang == 0){
            editText1.setHint(R.string.name);
            editText2.setHint(R.string.pass);
            singIn.setText(R.string.singIn);
            singUp.setText(R.string.singUp);
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Toast.makeText(MainActivity.this, "onDestroy", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStop(){
        super.onStop();

        Log.d(TAG, "onStop");
        Toast.makeText(MainActivity.this, "onStop", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart(){
        super.onStart();
        load();
        setLang(state);
        Log.d(TAG, "onStart");
        Toast.makeText(MainActivity.this, "onStart", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        save();
        Log.d(TAG, "onPause");
        Toast.makeText(MainActivity.this, "onPause", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
        //load();
        Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        Log.d(TAG, "onRestart");
        Toast.makeText(MainActivity.this, "onRestart", Toast.LENGTH_SHORT).show();
    }
}