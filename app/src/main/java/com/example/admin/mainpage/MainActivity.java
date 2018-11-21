package com.example.admin.mainpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.admin.mainpage.MenuPage;
import com.example.admin.mainpage.SignupPage;

public class MainActivity extends AppCompatActivity {

    ViewFlipper Vf;
    Button BtnSignIn, BtnSignUp;
    EditText inputID, inputPW;
    CheckBox chk_auto;
    Boolean loginChecked;
    public SharedPreferences setting;
    HttpPost httppost, httpposta;
    StringBuffer buffer;
    HttpResponse response,response2;
    HttpClient httpclient, httpclient2;
    List<NameValuePair> nameValuePairs, nameValuePairs2;
    ProgressDialog dialog = null;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BtnSignUp = (Button) findViewById(R.id.button_sign_up);
        BtnSignIn = (Button) findViewById(R.id.button_sign_in);
        inputID = (EditText) findViewById(R.id.editText_main_id);
        inputPW = (EditText) findViewById(R.id.editText_main_pw);
        chk_auto = (CheckBox) findViewById(R.id.chk_auto);
        tv = (TextView) findViewById(R.id.textView_main_result);
        setting = getSharedPreferences("setting",Activity.MODE_PRIVATE);
        BtnSignIn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = ProgressDialog.show(MainActivity.this, "",
                            "Validating user...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            login();
                        }
                    }).start();
                }
            });

        chk_auto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
            }
        });
        loginChecked = setting.getBoolean("LoginChecked", false);
        if (loginChecked) {
            inputID.setText(setting.getString("id", ""));
            inputPW.setText(setting.getString("pw", ""));
            chk_auto.setChecked(true);
            BtnSignIn.performClick();
        }
    }
    void login() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://14.63.172.130/login.php");
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", inputID.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", inputPW.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            String it[];
            //System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tv.setText(response);
                    dialog.dismiss();

                    String s_id = inputID.getText().toString();
                    String rp[] = response.split("<BR>");
                    String it[] = new String[rp.length + 1];
                    for (int i = 0; i < rp.length; i++)
                    {
                        it[i] = rp[i];
                    }
                    it[rp.length] = s_id;

                    String login = rp[0];

                    if (login.equalsIgnoreCase("b")) {
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(MainActivity.this, MenuPage.class);
                        //Intent intent4 = new Intent(MainActivity.this, MenuPage.class);
                        intent2.putExtra("strings", it);
                        //intent4.putExtra("id",s_id);

                        startActivity(intent2);
                    } else {
                        Toast.makeText(MainActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }

    public void onStop()
    {
        super.onStop();
        if(chk_auto.isChecked()){
            setting = getSharedPreferences("setting",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("id", inputID.getText().toString());
            editor.putString("pw", inputPW.getText().toString());
            editor.putBoolean("LoginChecked",true);
            editor.commit();
        }
        else{
            setting = getSharedPreferences("setting", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.clear();
            editor.commit();
        }
    }

    public void CliSignUp(View view) {
        Intent intent = new Intent(this, SignupPage.class);
        startActivity(intent);
    }
}