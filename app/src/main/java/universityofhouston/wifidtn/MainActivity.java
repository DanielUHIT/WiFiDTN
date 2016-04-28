package universityofhouston.wifidtn;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtx1;
    TextView txvw1;
    Button btn;
    String Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edtx1 = (EditText) findViewById(R.id.editText1);
                txvw1 = (TextView) findViewById(R.id.textView1);
                //txvw1.setText(edtx1.getText().toString());
                makeJSON();
            }
        });
    }

    public JSONArray makeJSON() {
        JSONArray jArr = new JSONArray();
        JSONObject jObj = new JSONObject();
        try {

            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            jObj.put("Created_on: ", ts);
            jObj.put("Sent_by:", Build.USER);
            jObj.put("MAC_Address:", macAddress);
            jObj.put("Number_of_Hops:", 1);
            jObj.put("Message:", edtx1.getText());

            txvw1.setText(jObj.toString());

            jArr.put(jObj);

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }

        return jArr;
    }
}