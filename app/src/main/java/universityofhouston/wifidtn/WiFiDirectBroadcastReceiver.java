package universityofhouston.wifidtn;

///**
// * Created by Daniel on 5/1/2016.
// */
//

import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "Events";
    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;
    private PeerListListener myPeerListListener;

    //For toast, add also context
    private Context context;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MainActivity activity){//, Context context) {

        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
        this.context= context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();


        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

                Toast.makeText(context, "Wi-Fi Direct is enable", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(context, "Wi-Fi Direct is not enable", Toast.LENGTH_LONG).show();
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers

            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (manager != null) {
                manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        Log.d(TAG,String.format("PeerListListener: %d peers available, updating device list", peers.getDeviceList().size()));

                        // DO WHATEVER YOU WANT HERE
                        // YOU CAN GET ACCESS TO ALL THE DEVICES YOU FOUND FROM peers OBJECT

                    }
                });
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}