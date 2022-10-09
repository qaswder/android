package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    ArrayList<String> addString;
    ArrayAdapter<String> adapter;
    ArrayList<String> selectedString;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        addString = new ArrayList<>();
        adapter = new ArrayAdapter <>(this, android.R.layout.simple_list_item_multiple_choice, addString);
        selectedString = new ArrayList<>();

        Button buttonAdd =  findViewById(R.id.button1);
        listView = findViewById(R.id.listView);
        ImageButton imgBtn = findViewById(R.id.imgBut);

        Bundle arguments = getIntent().getExtras();
        String name = arguments.get("login").toString();
        addString.add(name);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // получаем нажатый элемент
                String res = adapter.getItem(position);
                if(listView.isItemChecked(position))
                    selectedString.add(res);

                else
                    selectedString.remove(res);
            }
        });



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
        // получаем и удаляем выделенные элементы

        for(int i=0; i< selectedString.size();i++){
            adapter.remove(selectedString.get(i));
        }
        // снимаем все ранее установленные отметки
        listView.clearChoices();
        // очищаем массив выбраных объектов
        selectedString.clear();

        adapter.notifyDataSetChanged();
    }

}