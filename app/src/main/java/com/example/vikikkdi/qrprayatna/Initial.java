package com.example.vikikkdi.qrprayatna;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class Initial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        final ArrayList<String> mobileArray = new ArrayList<>(Arrays.asList("OSPC","Reverse Coding","Debugging","Linux Mate",
                "Web Design","Hackathon","Sql Scholar"));


        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //       Toast.makeText(getApplicationContext(),String.valueOf(intt.size()),Toast.LENGTH_SHORT).show();

                mobileArray.get(position);
                Intent i = new Intent(Initial.this, eventActivity.class);
                i.putExtra("eventname",mobileArray.get(position));
                startActivity(i);

            }
        });

    }

}
