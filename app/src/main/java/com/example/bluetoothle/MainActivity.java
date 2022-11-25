package com.example.bluetoothle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BluetoothProfile.ServiceListener profileListener;
    private BluetoothHeadset bluetoothHeadset;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the default adapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Establish connection to the proxy.
        bluetoothAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                if (i == BluetoothProfile.HEADSET) {
                    bluetoothHeadset = (BluetoothHeadset) bluetoothProfile;
                    Log.i("BT", "Dispositivo conectado");
                }
            }

            @Override
            public void onServiceDisconnected(int i) {
                if (i == BluetoothProfile.HEADSET) {
                    bluetoothHeadset = null;
                    Log.i("BT", "Dispositivo desconectado");
                }
            }
        }, BluetoothProfile.HEADSET);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("We need to access your location")
                        .setMessage("We want to track every breath you take")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                                        MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH);
                            }
                        });
                builder.create().show();
            }
            else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {
            // Permission has already been granted

            mostrar();
        }

    // Close proxy connection after use.
        //bluetoothHeadset=null;
        bluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET,bluetoothHeadset);
    }

    @SuppressLint("MissingPermission")
    public void mostrar(){
        for (BluetoothDevice d: bluetoothHeadset.getConnectedDevices()) {
            Log.i("BT","El nombre del dispositivo es: " + d.getName());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_BLUETOOTH:
                // other 'case' lines to check for other
                // permissions this app might request.
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    mostrar();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }
}


