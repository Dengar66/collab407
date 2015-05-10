package com.DPAC.collabormate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by paulhurlbut on 5/8/15.
 */
public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager)
                _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] netInfo = connectivity.getAllNetworkInfo();
            if (netInfo != null) {
                for (int i = 0; i < netInfo.length; i++) {
                    if (netInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }

        return false;
    }

}
