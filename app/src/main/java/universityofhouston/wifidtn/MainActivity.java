package universityofhouston.wifidtn;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText edtx1;
    TextView txvw1;
    TextView txvw2;
    TextView txvw3;
    TextView txvw4;
    TextView txvw5;
    Button btn;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    Random rand = new Random();
    int r = rand.nextInt(5);

//    Wifi Code

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;

    IntentFilter mIntentFilter;

    Button discover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

//      Register the Broadcast
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel =   mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        final View.OnClickListener ScanListener=new View.OnClickListener(){
            public void onClick(View v){

//          DEBUG: Scanning Peers
                Toast.makeText(MainActivity.this, "Scanning for peers", Toast.LENGTH_LONG).show();

                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

                    public void onSuccess() {

//                  DEBUG: Peers found
                        Toast.makeText(MainActivity.this, "Peers found", Toast.LENGTH_LONG).show();

                    }

                    public void onFailure(int reasonCode) {

//                  DEBUG: finding Peers
                        Toast.makeText(MainActivity.this, "Error on canning process", Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        discover = (Button) findViewById(R.id.buttonDiscover);
        discover.setOnClickListener(ScanListener);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edtx1 = (EditText) findViewById(R.id.editText1);
                txvw1 = (TextView) findViewById(R.id.textView1);
                txvw2 = (TextView) findViewById(R.id.textView2);
                txvw3 = (TextView) findViewById(R.id.textView3);
                txvw4 = (TextView) findViewById(R.id.textView4);
                txvw5 = (TextView) findViewById(R.id.textView5);
                makeJSON();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    // unregister the broadcast receiver
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }



    //Show the action bar wifi button.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wifi, menu);
        return true;
    }

    //ActionBar button action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wifi:
                startActivity(new Intent(MainActivity.this,Devices.class));

//                mReceiver.discoverPeers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public JSONArray makeJSON() {
        JSONArray jArr = new JSONArray();
        JSONObject jObj = new JSONObject();
        try {

            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            String deviceName = myDevice.getName();

            //Timestamp
            //Long tsLong = System.currentTimeMillis() / 1000;
            //String ts = tsLong.toString();

            jObj.put("Created_on:", currentDateTimeString);
            jObj.put("Sent_by:", deviceName);
            jObj.put("MAC_Address:", macAddress);
            jObj.put("Number_of_Hops:", r);
            jObj.put("Message:", edtx1.getText());

            txvw1.setText(jObj.getString("Created_on:"));
            txvw2.setText(jObj.getString("Sent_by:"));
            txvw3.setText(jObj.getString("MAC_Address:"));
            txvw4.setText(jObj.getString("Number_of_Hops:"));
            txvw5.setText(jObj.getString("Message:"));

            jArr.put(jObj);

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }

        return jArr;
    }
}