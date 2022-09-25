package com.example.lab1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.FrameMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    int count1 = 0;
    int count2 = 0;
    int r =1;
    int g =135;
    int b =134;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        TextView textView1 = (TextView) findViewById(R.id.text1);
        TextView textView2 = (TextView) findViewById(R.id.text2);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText("Количество нажатий: " + ++count1);
                r += 15;
                b -= 15;
                g -= 25;
                if(r >= 255){
                    r = 75;
                }
                if(b <= 0){
                    b = 0;
                }
                if(g <= 0){
                    g = 0;
                }
                frameLayout.setBackgroundColor(Color.rgb(r, g, b));

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText("Количество нажатий: " + ++count2);
                r -= 15;
                b += 15;
                g -= 25;
                if(r <= 0){
                    r = 0;
                }
                if(b >= 255){
                    b = 75;
                }
                if(g <= 0){
                    g = 0;
                }
                frameLayout.setBackgroundColor(Color.rgb(r, g, b));

            }
        });
    }
}