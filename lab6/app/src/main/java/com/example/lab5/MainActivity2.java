package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab5.db.DatabaseHandler;
import com.example.lab5.db.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity2 extends AppCompatActivity {
    private final static String TAG = "MainActivity2";
    final String LIST = "list";
    final String STATE = "state";
    final String LOGIN = "login";
    final String PASS = "pass";

    List<User> userList;
    DatabaseHandler db;
    ImageButton imgButton, imgButSettings;
    Button buttonAdd, lang;
    EditText editText;
    TextView text;
    ArrayList<String> addString;
    ArrayAdapter<String> adapter;
    ArrayList<String> selectedString;
    ListView listView;
    SharedPreferences preferences, preferences_settings;

    int state = 0;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle arguments = getIntent().getExtras();
        String loginToSett = arguments.get("login").toString();
        String passToSett = arguments.get("pass").toString();

        db = new DatabaseHandler(this);
        preferences = getSharedPreferences(loginToSett, MODE_PRIVATE);
        preferences_settings = getSharedPreferences("settings", MODE_PRIVATE);
        addString = new ArrayList<>();
        adapter = new ArrayAdapter <>(this, android.R.layout.simple_list_item_multiple_choice, addString);
        selectedString = new ArrayList<>();
        listView = findViewById(R.id.listView);

        imgButton = findViewById(R.id.imgBut);
        lang = findViewById(R.id.lang);
        buttonAdd = findViewById(R.id.button1);
        imgButSettings = findViewById(R.id.imgButSettings);
        editText = findViewById(R.id.edit);
        text = findViewById(R.id.text1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String res = adapter.getItem(position);
                if(listView.isItemChecked(position)){
                    selectedString.add(res);
                    imgButSettings.setVisibility(View.GONE);
                    imgButton.setVisibility(View.VISIBLE);
                }
                else{
                    selectedString.remove(res);
                    imgButton.setVisibility(View.GONE);
                    imgButSettings.setVisibility(View.VISIBLE);
                }
            }
        });

        imgButSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, Settings.class);
                intent.putExtra(LOGIN, loginToSett);
                intent.putExtra(PASS, passToSett);
                startActivity(intent);
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
                Log.e("state", String.valueOf(state));
                Log.e("count", String.valueOf(count));
            }
        });
        load();
    }

    public void setLang(int stateLang){
        if(stateLang == 1){
            editText.setHint(R.string.hintEn);
            buttonAdd.setText(R.string.butAddEn);
            text.setText(R.string.listEn);
        }
        if(stateLang == 0){
            editText.setHint(R.string.hint);
            buttonAdd.setText(R.string.butAdd);
            text.setText(R.string.list);
        }
    }
    public void saveLang(){
        SharedPreferences.Editor editor = preferences_settings.edit();
        editor.putInt(STATE, state);
        editor.apply();
        Log.e("stateSave2", String.valueOf(state));
    }
    public void loadLang(){
        int loadState = preferences_settings.getInt(STATE,3);
        state = loadState;
        Log.e("stateLoad2", String.valueOf(state));
    }
    public void save(){
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> itemList = new HashSet<>();

        for(int i=0; i<addString.size(); i++){
            itemList.add(addString.get(i));
        }
        editor.putStringSet(LIST, itemList);
        editor.apply();
    }
    public void load(){
        Set<String> list = preferences.getStringSet(LIST, new HashSet<>());

        for(String string : list){
            addString.add(string);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
        new Thread(() -> {
            Log.i("Thread_MainAct_2", Thread.currentThread().getName());
            userList = db.getAllUsers();
            if(userList.size() != 0) {
                for (User user : userList) {
                    String log = "Id: " + user.getID() + " ,Login: " + user.getLogin() + " ,Password: " + user.getPass();
                    //Log.i("Loading...", log);
                    addString.add(log);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            }
        }).start();
    }
    public void add(View view){
        EditText editText = findViewById(R.id.edit);
        String res = editText.getText().toString();
        if(res.isEmpty()){
            editText.setHintTextColor(Color.rgb(255,0,0));
        }
        else{
            editText.setHintTextColor(Color.GRAY);
            addString.add(res);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            editText.setText("");
        }
    }
    public void remove(View view){
        for(int i=0; i< selectedString.size();i++){
            adapter.remove(selectedString.get(i));
        }
        listView.clearChoices();
        selectedString.clear();
        adapter.notifyDataSetChanged();

        imgButton.setVisibility(View.GONE);
        imgButSettings.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadLang();
        setLang(state);
        Log.e(TAG, "onStart");
        Toast.makeText(MainActivity2.this, "APP onStart", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        saveLang();
        save();
        Log.e(TAG, "onPause");
        Toast.makeText(MainActivity2.this, "APP onPause", Toast.LENGTH_SHORT).show();
    }
}