package com.example.lab5;

import static com.example.lab5.db.DBContract.UserEntry.TABLE_NAME;

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

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    final String LOGIN = "login";
    final String PASS = "pass";
    //List<User> userList;
    EditText login, pass;
    Button lang, singIn, singUp;
    DatabaseHandler db;
    SharedPreferences preferences;

    final String STATE = "state";
    int state = 0;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        login = findViewById(R.id.login);
        pass = findViewById(R.id.pass);
        lang = findViewById(R.id.lang);
        singIn = findViewById(R.id.singIn);
        singUp = findViewById(R.id.singUp);

        singIn.setOnClickListener(this);
        singUp.setOnClickListener(this);
        lang.setOnClickListener(this);
        db = new DatabaseHandler(this);
        loadState();
    }

    @Override
    public void onClick(View view){
            switch (view.getId()){
                case R.id.lang:
                    count  += 1;
                    if(count % 2 == 0){
                        state = 0;
                    }else state = 1;

                    setLang(state);
                    Log.e("state", String.valueOf(state));
                    Log.e("count", String.valueOf(count));
                    break;
                case R.id.singUp:
                    String messageAdd = db.addUser(new User(login.getText().toString(), pass.getText().toString()));
                    if(messageAdd.equals("Логин доступен")){
                        Toast.makeText(MainActivity.this, "Пользователь зарегистрирован <Логин доступен>", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, messageAdd, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.singIn:
                    singInApp();

                    break;
                default:
                    break;
            }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        Toast.makeText(MainActivity.this, "onDestroy", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart(){
        super.onStart();
        loadState();
        setLang(state);
        Log.e(TAG, "onStart");
        Toast.makeText(MainActivity.this, "onStart", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        saveState();
        Log.e(TAG, "onPause");
        Toast.makeText(MainActivity.this, "onPause", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.e(TAG, "onRestart");
        Toast.makeText(MainActivity.this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    public void singInApp(){
        int condition = 0;
        String thisLoginUser = login.getText().toString();
        String passCmp= pass.getText().toString();

        /*userList = db.getAllUsers();

        for (User user : userList){
            String log = "Id: "+user.getID()+" ,Login: " + user.getLogin() + " ,Password: " + user.getPass();
            Log.i("Loading...", log);
        }*/

        String dataUser = db.getUser(thisLoginUser);

        if(!dataUser.equals("null")){
            String[] cmpData = dataUser.split(" ");

            if(cmpData[0].equals(thisLoginUser)){
                Log.e("cmplog", cmpData[0]);
                condition += 1;
            }else{
                Toast.makeText(MainActivity.this, "Неверный логин", Toast.LENGTH_SHORT).show();
                condition = 0;
            }

            if (cmpData[1].equals(passCmp)){
                Log.e("cmppass", cmpData[1]);
                condition += 1;
            }else{
                Toast.makeText(MainActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                condition = 0;
            }

            if(condition == 2){
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra(LOGIN, cmpData[0]);
                intent.putExtra(PASS, cmpData[1]);
                login.setText("");
                pass.setText("");
                startActivity(intent);
            }
            Log.e("cmp", cmpData[0] +" "+ cmpData[1]);
        }
        else{
            Toast.makeText(MainActivity.this, "Неверные данные", Toast.LENGTH_SHORT).show();
        }
    }
    public void setLang(int stateLang){
        if(stateLang == 1){
            login.setHint(R.string.nameEn);
            pass.setHint(R.string.passEn);
            singIn.setText(R.string.singInEn);
            singUp.setText(R.string.singUpEn);
        }
        if(stateLang == 0){
            login.setHint(R.string.name);
            pass.setHint(R.string.pass);
            singIn.setText(R.string.singIn);
            singUp.setText(R.string.singUp);
        }
    }
    public void saveState(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(STATE, state);
        editor.apply();
        Log.e("stateSave", String.valueOf(state));
    }
    public void loadState(){
        int loadState = preferences.getInt(STATE,3);
        state = loadState;
        Log.e("stateLoad", String.valueOf(state));
    }

}