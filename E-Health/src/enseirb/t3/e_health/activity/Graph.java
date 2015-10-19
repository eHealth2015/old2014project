package enseirb.t3.e_health.activity;

import java.util.ArrayList;

import org.achartengine.GraphicalView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.project.e_health.R;

import enseirb.t3.e_health.bluetooth.Bluetooth;
import enseirb.t3.e_health.bluetooth.BtThread;
import enseirb.t3.e_health.entity.ArduinoData;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.graph.LineGraph;
import enseirb.t3.e_health.graph.Point;
import enseirb.t3.e_health.process.DataProcess;
import enseirb.t3.e_health.session.SessionManager;

public class Graph extends Activity {

	private SessionManager session;
	private GraphicalView view;
	private LineGraph line;
	
	//Type de données à afficher par défaut
	private String dataname = "A";
	private int cmpt = 0;
	private int idPatient;
	private Bluetooth bt;
	private BtThread btThread = null;
	private static String TAG = "Graph";
	public static DataProcess dataProcess = new DataProcess();
	
	//Nombre de données à afficher sur le graph
	private static int nbreMesuresPrint = 9;
	private ArduinoData arduinoData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measures);
		session = new SessionManager(getApplicationContext());

		//Instancie un nouvel objet Bluetooth
		bt = new Bluetooth(this);
		
		//Activer le Bluetooth
		bt.enableBluetooth();
		
		idPatient = session.getUserDetails();

		//Lance le graphique
		openChart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.graph, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//Appuie sur l'icone Bluetooth
		case R.id.action_bt:
			item.setIcon(R.drawable.ic_action_bluetooth_searching);
			
			//Récupère le bon device
			bt.selectDevice();
			if ( Bluetooth.device != null) {
				// Instanciation et lancement du thread Bluetooth
				btThread = new BtThread(Bluetooth.device, handler);
				btThread.start();
				
				//Instanciation de ArduinoData
				arduinoData = new ArduinoData(btThread, dataProcess, this);
				item.setIcon(R.drawable.ic_action_bluetooth_connected);
			}
			return true;
			
		// Affiche le graphique voulu 
		case R.id.action_A:
			dataname = "A";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_B:
			dataname = "B";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_C:
			dataname = "C";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_O:
			dataname = "O";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_P:
			dataname = "P";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_R:
			dataname = "R";
			cmpt = 0;
			openChart();
			return true;
		case R.id.action_T:
			dataname = "T";
			cmpt = 0;
			openChart();
			return true;
		case R.id.deconnexion:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("HandlerLeak") 
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			Data data = null;
			ArrayList<Data> arrayData = null;
			String message = bundle.getString("msg");
			
			// Séparer selon "DATA"
			String[] aDataArrayStr = message.split("(?=DATA)");

			for (int i = 1; i < aDataArrayStr.length; i++) {
			
				//Vérifie la longueur pour éviter d'enventuels problèmes d'envoi
				if (aDataArrayStr[i].length() > 35) {
				
					// Enregistre les données et crée une nouvelle alerte si besoins est
					// Retourne toutes les données d'une même date
					arrayData = arduinoData.stockData(
							arduinoData.getChunks(aDataArrayStr[i]), idPatient);

					for (Data dataTmp : arrayData) {
						if (dataTmp.getDataname().equals(dataname))
							//Selectionne uniquement le type de données demandé par l'utilisateur
							data = dataTmp;
					}

					//Création d'un nouveau point
					Point p = new Point(data.getDate(), Double.parseDouble(data.getValue()));
					line.addNewPoint(p);
					cmpt++;
					if (cmpt > nbreMesuresPrint) {
						
						// Supprime les anciennes mesures 
						line.removePoint(cmpt - (nbreMesuresPrint + 1));
						cmpt--;
					}
					view.repaint();

					Log.d(TAG, Double.toString(Double.parseDouble(data
							.getValue())));
				}
			}
		}
	};

	private void openChart() {
		line = new LineGraph(dataname);
		view = line.getView(this);
		setContentView(view);
	}

	@Override
	public void onBackPressed() {
		//Deconnexion de l'utilisateur
		session.logoutUser();
		if (btThread != null)
			//Arret du thread Bluetooth
			((BtThread) btThread).close();
		super.onBackPressed();
	}
}
