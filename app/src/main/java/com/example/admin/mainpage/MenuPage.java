package com.example.admin.mainpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;


public class MenuPage extends Activity {
    private BluetoothAdapter mBluetoothAdapter;
    String lecture_name, s_id;
    String rp[];
    private Switch s;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent2 = getIntent();
        String it[] = intent2.getStringArrayExtra("strings");
        rp = new String[it.length + 1];
        for (int i = 0; i < it.length; i++)
        {
            rp[i] = it[i];
        }

        s_id = it[it.length - 1];
        //Intent intent4= new Intent(MenuPage.this, LecturePage.class);
        //intent4.putExtra("s_id",s_id);
        //startActivity(intent4);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        String j = rp[1];
        int k = Integer.parseInt(j);
        for (int i = 1; i <= k; i++) {
            Button b = new Button(this);
            b.setText(rp[i + 1]);
            b.setId(100 + i);

            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Button btn = (Button)v;
                    lecture_name = ((Button) v).getText().toString();
                    String it2[] = {s_id, lecture_name};
                    Intent intent3 = new Intent(getApplicationContext(), LecturePage.class);
                    intent3.putExtra("it2", it2);
                    startActivity(intent3);
                }
            });

            layout.addView(b);
        }

        s = new Switch(this);
        s.setId('a');
        s.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();

                if (on) {
                    if (savedInstanceState == null) {
                        mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE))
                                .getAdapter();
                        // Is Bluetooth supported on this device?
                        if (mBluetoothAdapter != null) {
                            // Is Bluetooth turned on?
                            if (mBluetoothAdapter.isEnabled()) {
                                // Are Bluetooth Advertisements supported on this device?
                                if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                                    // Everything is supported and enabled, load the fragments.
                                }
                            } else {
                                // Prompt user to turn on Bluetooth (logic continues in onActivityResult()).
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent,Constants.REQUEST_ENABLE_BT);
                            }
                        }
                    }
                    startAdvertising();
                } else {
                    stopAdvertising();
                    mBluetoothAdapter.disable();
                }
            }
        });

        layout.addView(s);
        setContentView(layout);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (AdvertiserService.running) {
            s.setChecked(true);
        } else {
            s.setChecked(false);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    private static Intent getServiceIntent(Context c)
    {
        return new Intent(c, AdvertiserService.class);
    }

    private void startAdvertising() {
        Context c = getApplicationContext();
        c.startService(getServiceIntent(c));
        Toast.makeText(MenuPage.this, "advertising on", Toast.LENGTH_SHORT).show();
    }

    private void stopAdvertising() {
        Context c = getApplicationContext();
        c.stopService(getServiceIntent(c));
        s.setChecked(false);
        Toast.makeText(MenuPage.this, "advertising off", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                    } else {
                    }
                } else {
                    finish();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
