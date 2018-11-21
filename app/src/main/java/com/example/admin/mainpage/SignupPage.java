package com.example.admin.mainpage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class SignupPage extends Activity {

    //private static String IP_ADDRESS = "http://14.63.172.130/insert.php";
    private static String TAG = "phptest_MainActivity";

    private EditText mEditTextstd_id;
    private EditText mEditTextstd_name;
    private EditText mEditTextstd_mail;
    private EditText mEditTextstd_telephone;
    private EditText mEditTextstd_pw;
    private TextView mTextViewMac;
    private TextView mTextViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEditTextstd_id = (EditText) findViewById(R.id.editText_main_std_id);
        mEditTextstd_name = (EditText) findViewById(R.id.editText_main_std_name);
        mEditTextstd_mail = (EditText) findViewById(R.id.editText_main_std_mail);
        mEditTextstd_telephone = (EditText) findViewById(R.id.editText_main_std_telephone);
        mEditTextstd_pw = (EditText) findViewById(R.id.editText_main_std_pw);
        mTextViewResult = (TextView) findViewById(R.id.textView_sign_result);

    }

    /*public static String getMacAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : interfaces) {
                if(interfaceName != null) {
                    if (!nif.getName().equalsIgnoreCase(interfaceName)) continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }*/

    public void insert(View v) {

        String std_id = mEditTextstd_id.getText().toString();
        String std_name = mEditTextstd_name.getText().toString();
        String std_mail = mEditTextstd_mail.getText().toString();
        String std_telephone = mEditTextstd_telephone.getText().toString();
        String std_pw = mEditTextstd_pw.getText().toString();

        iTD(std_id, std_name, std_mail, std_telephone, std_pw);

    }

    private void iTD(String std_id, String std_name, String std_mail, String std_telephone,
                     String std_pw)
    {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(SignupPage.this,
                        "Please Wait", null, true, true);
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                mTextViewResult.setText(result);
                Log.d(TAG, "POST response  - " + result);
            }


            @Override
            protected String doInBackground(String... params) {
                try {
                    String std_id = (String) params[0];
                    String std_name = (String) params[1];
                    String std_mail = (String) params[2];
                    String std_telephone = (String) params[3];
                    String std_pw = (String) params[4];
                    String serverURL = "http://14.63.172.130/insert.php";
                    String postParameters = "std_id=" + std_id + "&std_name=" + std_name +
                            "&std_mail=" + std_mail + "&std_telephone=" + std_telephone + "&std_pw="
                            + std_pw;


                    URL url = new URL(serverURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();


                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(postParameters.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();


                    int responseStatusCode = httpURLConnection.getResponseCode();
                    Log.d(TAG, "POST response code - " + responseStatusCode);

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();

                    return sb.toString();
                } catch (Exception e) {
                    Log.d(TAG, "InsertData: Error ", e);
                    return new String("Error: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(std_id, std_name, std_mail, std_telephone, std_pw);
    }
}

