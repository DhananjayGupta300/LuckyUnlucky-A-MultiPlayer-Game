package com.example.dhananjaygupta.dhananjaygupta_project4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;


public class StartingActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        Button button = (Button) findViewById(R.id.B) ;
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivity(intent);
            }
        }) ;
    }
}
