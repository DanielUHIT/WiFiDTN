package universityofhouston.wifidtn;

/**
 * Created by Daniel on 5/1/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.ConnectionService;

/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "PTP_Recv";

    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = new Intent(context, ConnectionService.class);  // start ConnectionService
        serviceIntent.setAction(action);   // put in action and extras
        serviceIntent.putExtras(intent);
        context.startService(serviceIntent);  // start the connection service
    }
}