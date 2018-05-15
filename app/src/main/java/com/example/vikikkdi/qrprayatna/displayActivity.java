package com.example.vikikkdi.qrprayatna;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.SEND_SMS;

public class displayActivity extends AppCompatActivity {
    private static final int REQUEST_READ_PHONE_STATE=0;
    private static final int REQUEST_READ_SEND_SMS=2;
    ArrayAdapter adapter;
    ListView listView;
    String[] mobileArray;
    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE ) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean checkPermission1() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS ) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }
    private void requestPermission1() {
        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, REQUEST_READ_SEND_SMS);
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(displayActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{READ_PHONE_STATE},
                                                        REQUEST_READ_PHONE_STATE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
            case REQUEST_READ_SEND_SMS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{SEND_SMS},
                                                        REQUEST_READ_SEND_SMS);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;

            default:
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();

            } else {
                requestPermission();
            }
        }
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission1()) {
                Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();

            } else {
                Log.d("NVIKRAMA","kk");
                requestPermission1();
            }
        }
        Button btn1 = (Button) findViewById(R.id.mssg);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<mobileArray.length;i++) {
                    Toast.makeText(displayActivity.this, mobileArray[i], Toast.LENGTH_LONG).show();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mobileArray[i], null, "sms message", null, null);
                }
            }
        });
        Button btn = (Button)findViewById(R.id.addFinal);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(displayActivity.this);
                View promptView = layoutInflater.inflate(R.layout.enterpass, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(displayActivity.this);
                alertDialogBuilder.setView(promptView);

                final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String res = editText.getText().toString();
                                Log.d("NVIKRAMA","hello");
                                RequestQueue requestQueue1 = Volley.newRequestQueue(displayActivity.this);
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.43.74:8081/check_pass.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("true")){
                                                    Toast.makeText(displayActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(displayActivity.this, MainActivity.class);
                                                    intent.putExtra("eventname", getIntent().getStringExtra("eventname"));
                                                    intent.putExtra("type","finalist");
                                                    startActivity(intent);
                                                }else{
                                                    Intent intent=new Intent(displayActivity.this, eventActivity.class);
                                                    intent.putExtra("eventname",getIntent().getStringExtra("eventname"));
                                                    intent.putExtra("button",getIntent().getStringExtra("button"));
                                                    startActivity(intent);
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(displayActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                        volleyError.printStackTrace();
                                        requestQueue1.stop();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("eventname",getIntent().getStringExtra("eventname"));
                                        params.put("password",res);
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
        });
        RequestQueue requestQueue1 = Volley.newRequestQueue(displayActivity.this);
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://192.168.43.74:8081/finalist_check.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("empty"))
                        {
                            Toast.makeText(displayActivity.this, "No finalist added!!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            mobileArray=response.split(":");
                            adapter = new ArrayAdapter<String>(displayActivity.this,android.R.layout.simple_list_item_1, mobileArray);
                            listView = (ListView) findViewById(R.id.listView);
                            listView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(displayActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                volleyError.printStackTrace();
                requestQueue1.stop();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventname",getIntent().getStringExtra("eventname"));
                //params.put("roomno",res);
                return params;
            }
        };
        requestQueue1.add(stringRequest1);

    }
    public void onBackPressed() {
        Intent inte = new Intent(displayActivity.this, eventActivity.class);
        inte.putExtra("eventname",getIntent().getStringExtra("eventname"));
        startActivity(inte);
    }
}
