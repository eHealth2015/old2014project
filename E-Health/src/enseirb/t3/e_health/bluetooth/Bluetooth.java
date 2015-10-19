package enseirb.t3.e_health.bluetooth;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Bluetooth {

	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	public static BluetoothDevice device = null;
	private Activity activity;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	public Bluetooth(Activity activity) {
		this.activity = activity;
	}

	public void enableBluetooth() {

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBlueTooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(enableBlueTooth,
					REQUEST_CODE_ENABLE_BLUETOOTH);
		}
	}

	public void selectDevice() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
			
				//Recherche un appareil ayant pour nom Ehealth dans les liste des appareils déjà apairés
				if (device.getName().equals("Ehealth")) {
					// Si oui alors le selectionner
					Log.d("msg", "Arduino déjà découvert....");
					Bluetooth.device = device;
				}
			}
		} else
			// Si non, alors faire une nouvelle recherche
			discoverDevices();
	} 

	private void discoverDevices() {
		mBluetoothAdapter.startDiscovery();
	}
}
