package com.example.vikikkdi.qrprayatna;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class eventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        RequestQueue requestQueue = Volley.newRequestQueue(eventActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.74:8081/new.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(eventActivity.this, response, Toast.LENGTH_LONG).show();
                        String[] parts = response.split(":");
                        String name = parts[0];
                        String[] parts1 = parts[0].split(";");
                        String[] parts2 = parts[1].split(";");
                        name = "Name : \n";
                        for(int i=0;i<parts1.length;i++)    name+=parts1[i]+" "+parts2[i]+"\n";
                        String[] p = new String[2];
                        p[0]=name; p[1]=parts[2];
                        ArrayAdapter adapter = new ArrayAdapter<String>(eventActivity.this,android.R.layout.simple_list_item_1, p);

                        ListView listView = (ListView) findViewById(R.id.event_list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //       Toast.makeText(getApplicationContext(),String.valueOf(intt.size()),Toast.LENGTH_SHORT).show();
                                if(position==0){
                                    Toast.makeText(eventActivity.this, "Editing not allowed",Toast.LENGTH_LONG).show();
                                }else if(position==1){
                                    LayoutInflater layoutInflater = LayoutInflater.from(eventActivity.this);
                                    View promptView = layoutInflater.inflate(R.layout.inputdialog, null);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(eventActivity.this);
                                    alertDialogBuilder.setView(promptView);

                                    final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
                                    // setup a dialog window
                                    alertDialogBuilder.setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Toast.makeText(eventActivity.this,editText.getText().toString(),Toast.LENGTH_LONG).show();
                                                    String res = editText.getText().toString();
                                                    Log.d("NVIKRAMA","hello");
                                                    RequestQueue requestQueue1 = Volley.newRequestQueue(eventActivity.this);
                                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.43.74:8081/updateroom.php",
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    if(response.equals("ss"))
                                                                        Toast.makeText(eventActivity.this, response, Toast.LENGTH_LONG).show();
                                                                    else{
                                                                        Toast.makeText(eventActivity.this, response,Toast.LENGTH_LONG).show();
                                                                    }
                                                                    Intent inte = new Intent(eventActivity.this, eventActivity.class);
                                                                    inte.putExtra("eventname",intent.getStringExtra("eventname"));
                                                                    startActivity(inte);
                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError volleyError) {
                                                            Toast.makeText(eventActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                                            volleyError.printStackTrace();
                                                            requestQueue.stop();
                                                        }
                                                    }){
                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put("eventname",intent.getStringExtra("eventname"));
                                                            params.put("roomno",res);
                                                            return params;
                                                        }
                                                    };
                                                    requestQueue1.add(stringRequest1);
                                                }
                                            })
                                            .setNegativeButton("Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                    // create an alert dialog
                                    AlertDialog alert = alertDialogBuilder.create();
                                    alert.show();
                                }
                            }
                        });
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(eventActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                volleyError.printStackTrace();
                requestQueue.stop();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventname",intent.getStringExtra("eventname"));
                return params;
            }
        };
        requestQueue.add(stringRequest);
        Button b1 = (Button)findViewById(R.id.button1);
        Button b2 = (Button)findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(eventActivity.this,displayActivity.class);
                i.putExtra("eventname",intent.getStringExtra("eventname"));
                i.putExtra("button","finalist");
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(eventActivity.this,winners.class);
                i.putExtra("eventname",intent.getStringExtra("eventname"));
                i.putExtra("button","winners");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent inte = new Intent(eventActivity.this, Initial.class);
        startActivity(inte);
    }

}
