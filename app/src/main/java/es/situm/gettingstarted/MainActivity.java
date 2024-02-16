package es.situm.gettingstarted;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import es.situm.gettingstarted.common.SampleActivity;
import es.situm.gettingstarted.samplelist.SamplesActivity;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the title of the application
        setTitle("Indoor Positioning");

        // Find the three buttons in the layout
        Button connectToWifiButton = findViewById(R.id.connectToWifiButton);
        Button wayfindingButton = findViewById(R.id.wayfindingButton);

        // Set click listeners for the buttons
        connectToWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch WifiConnectActivity
                Intent intent = new Intent(MainActivity.this, WifiConnectActivity.class);
                startActivity(intent);
            }
        });
        wayfindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch WifiConnectActivity
                Intent intent = new Intent(MainActivity.this, SamplesActivity.class);
                startActivity(intent);
            }
        });

        // Check and request runtime permissions
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        // Check if the required permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request the missing permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if all permissions were granted
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            // If all permissions are not granted, you can handle it here (e.g., show a message)
            if (!allPermissionsGranted) {
                // Handle the case where the user denied some or all of the permissions
            }
        }
    }
}