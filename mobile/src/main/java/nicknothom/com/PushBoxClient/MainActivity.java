package nicknothom.com.PushBoxClient;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Vibrator;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.*;
import java.io.*;

import java.io.IOException;

public class MainActivity extends Activity {

    public boolean isHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        isHome = isConnectedTo("DeltaChiFi");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lightOn(View v) throws IOException {
        String cmd = "a=0&b=0";
        //outletControl(cmd);
        new HTTPCalls().execute(cmd);
    }
    public void lightOff(View v) throws IOException {
        String cmd = "a=1&b=1";
        new HTTPCalls().execute(cmd);
    }

    public void fanOn(View v) throws IOException {
        String cmd = "g=0";
        new HTTPCalls().execute(cmd);
    }
    public void fanOff(View v) throws IOException {
        String cmd = "g=1";
        new HTTPCalls().execute(cmd);
    }

    public void candleOn(View v) throws IOException {
        String cmd = "h=0";
        new HTTPCalls().execute(cmd);
    }
    public void candleOff(View v) throws IOException {
        String cmd = "h=1";
        new HTTPCalls().execute(cmd);
    }


    public boolean isConnectedTo(String testSSID){
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID", wifiInfo.getSSID());

        if (wifiInfo.getSSID().contains(testSSID)){
            return true;
        }
        else {
            return false;
        }
    }

    public void vibe(){
        int time = 10;
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    // Async Task Class
    class HTTPCalls extends AsyncTask<String, Void, Void> {
        public boolean isHome;
        String piAddress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            vibe();

            isHome = isConnectedTo("DeltaChiFi");

            //Use LAN if available
            if (isHome) {
                piAddress = "http://192.168.1.106:3000?";
            }
            else {
                piAddress = "http://nicknothom.com:3001?";
            }
        }

        @Override
        protected Void doInBackground(String... args) {
            //args[0] should be _query_
            String call = piAddress + args[0];

            try {
                URL url = new URL(call);
                InputStream is = url.openStream();
                is.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void file_url) {
            //System.out.println("Done!");
        }
    }


    //Deprecated
    public void outletControl(String query) throws IOException {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(30);

        isHome = isConnectedTo("DeltaChiFi");

        String piAddress;

        //Use LAN if available
        if (isHome) {
            piAddress = "http://192.168.1.106:3000?";
        }
        else {
            piAddress = "http://nicknothom.com:3001?";
        }

        String call = piAddress + query;

        try {
            URL url = new URL(call);
            InputStream is = url.openStream();
            is.close();
        }
        catch (Throwable e) {
            Toast.makeText(getApplicationContext(), "HTTP Error!",
                    Toast.LENGTH_LONG).show();
        }
    }
}

