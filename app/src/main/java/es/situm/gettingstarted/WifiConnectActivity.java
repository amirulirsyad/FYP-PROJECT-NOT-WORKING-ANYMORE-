package es.situm.gettingstarted;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private List<ScanResult> wifiScanResults;
    private ArrayList<String> wifiSsids;

    private ListView wifiListView;
    private Button connectButton;

    private ArrayList<String> selectedWifiSsids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect);

        wifiListView = findViewById(R.id.wifiListView);
        connectButton = findViewById(R.id.connectButton);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        selectedWifiSsids = new ArrayList<>();

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Wi-Fi is not enabled. Please enable Wi-Fi and try again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        wifiSsids = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, wifiSsids);
        wifiListView.setAdapter(adapter);
        wifiListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ssid = wifiSsids.get(position);
                if (selectedWifiSsids.contains(ssid)) {
                    selectedWifiSsids.remove(ssid);
                } else {
                    selectedWifiSsids.add(ssid);
                }
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToSelectedWifiNetworks();
            }
        });

        registerWifiScanReceiver();
        scanWifiNetworks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterWifiScanReceiver();
    }

    private void registerWifiScanReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
    }

    private void unregisterWifiScanReceiver() {
        unregisterReceiver(wifiScanReceiver);
    }

    private void scanWifiNetworks() {
        wifiManager.startScan();
    }

    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wifiScanResults = wifiManager.getScanResults();
            updateWifiList();
        }
    };

    private void updateWifiList() {
        wifiSsids.clear();
        for (ScanResult result : wifiScanResults) {
            wifiSsids.add(result.SSID);
        }
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) wifiListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void connectToSelectedWifiNetworks() {
        for (ScanResult result : wifiScanResults) {
            if (selectedWifiSsids.contains(result.SSID)) {
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = "\"" + result.SSID + "\"";
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                int networkId = wifiManager.addNetwork(wifiConfig);
                wifiManager.enableNetwork(networkId, true);
                wifiManager.reconnect();

                Log.d("WifiConnectActivity", "Connected to: " + result.SSID);
            }
        }

        Toast.makeText(this, "Connecting to selected Wi-Fi networks...", Toast.LENGTH_SHORT).show();
    }
}