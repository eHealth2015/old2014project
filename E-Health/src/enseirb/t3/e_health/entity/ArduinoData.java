package enseirb.t3.e_health.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.project.e_health.R;

import enseirb.t3.e_health.activity.EHealth;
import enseirb.t3.e_health.activity.GraphAlertActivity;
import enseirb.t3.e_health.bluetooth.BtThread;
import enseirb.t3.e_health.process.DataProcess;

import java.lang.Math;

public class ArduinoData  {

	private Date date;
	private ArrayList<Data> arrayData = new ArrayList<Data>();
	private static int numberSensor;
	
	// Nombre de données par capteurs à enregistrer dans la table buffer
	private final static int numberDataPerSensor = 15;
	private static int numberData;
	private DataProcess dataProcess = null;
	private Alert alert;
	
	// Nombre de données restantes à sauvegarder (tous capteurs confondus)
	private int cmpNeedToSave = 0;
	private BtThread btThread;
	private String TAG = "ArduinoData";
	private ArrayList<String> arrayDataname = new ArrayList<String>();
	private int idAlert;
	private int mId=2;
	private Context context;
	
	public ArduinoData(BtThread bt, DataProcess dataProcess) {
		btThread = bt;
		this.dataProcess = dataProcess;
	}
	
	public ArduinoData(BtThread bt, DataProcess dataProcess, Context context) {
		btThread = bt;
		this.dataProcess = dataProcess;
		this.context=context;
	}
    
    // Récupère le timestamp d'envoi
    public String getPaquetTimestamp(String firstChunk) {
    	return firstChunk.substring(4, firstChunk.length());
    }
    
    public ArrayList<Data> stockData(String[] chunks, int idPatient) {
    	String[] chunkTmp;
    	Data dataTmp = null;
    	String paquetTimestampStr = this.getPaquetTimestamp(chunks[0]);
    	Log.d("timestamp", paquetTimestampStr);
    	long paquetTimestamp = Long.parseLong(paquetTimestampStr);
    	arrayData = new ArrayList<Data>();
    	long currentTime = System.currentTimeMillis();
    	
		// Pour chaque type de données
    	for (int i = 1; i < chunks.length; i++) {
    		
			// Tableau contenant pour une donnée le timestamp, le type de donnée et la valeur
    		chunkTmp = chunks[i].trim().split("\\||\\\n");
    		if(chunkTmp.length < 3){
    			Log.d("erreur","donn�es invalides");
    			break;
    		}
    		
    		if (chunkTmp[1].equals("A")) {
    			Double lng = Math.ceil(Double.parseDouble(chunkTmp[2]));
    			chunkTmp[2] = Double.toString(lng*8000/1024);
    		}
    		
    		date = new Date(currentTime - (paquetTimestamp - Long.parseLong(chunkTmp[0])));
			
			//Instanciation de l'objet Data
    		dataTmp = new Data(chunkTmp[1], chunkTmp[2], date, idPatient);
    		
    		Log.d("gt",dataTmp.getDataname());
    		Log.d("gt",dataTmp.getValue()+"\n");
    		Log.d("date", date.toString());
    		
			// Si le type de données corresponds avec ceux à sauvegarder pour le type d'alerte déclenchée et s'il faut encore en sauvegarder
    		if (cmpNeedToSave != 0 && arrayDataname.contains(dataTmp.getDataname())) {
    			Log.d(TAG, "idAlert no alert = " + Integer.toString(idAlert));
				
				//Enregistrement de la donnée dans la table SavedData contenant les données à sauvegarder et liées à une alerte
    			EHealth.db.createSavedData(dataTmp, idAlert);
    			cmpNeedToSave--;
    			Log.d(TAG, "cmpNeedToSave = " + Integer.toString(cmpNeedToSave));
    		} else if (cmpNeedToSave == 0 && arrayDataname.contains(dataTmp.getDataname())) {
			
				//Envoi un message permettant de reprendre une fech = 1Hz
    			btThread.write("STOP\n".getBytes());
				
				//Réinitialisation des paramètres
    			arrayDataname = new ArrayList<String>();
    			idAlert = 0;
				
			//Si plus de données à sauvegarder, ré-effectuer une corrélation et enregistrer les données dans une table buffer au cas ou une alerte est levée et afin de pouvoir les récupérer
    		} else if (cmpNeedToSave == 0) {
        		dataProcess.process(dataTmp);
    			EHealth.db.createData(dataTmp);
				// Supprime les données trop anciennes dans la table buffer
        		if (EHealth.db.getNumberData() > (chunks.length - 1)*numberDataPerSensor)
        			EHealth.db.deleteLastData();
    		}
    		arrayData.add(dataTmp);
    	}
		
		// Si une alerte à été créee
    	if ((alert = dataProcess.correlation()) != null) {
		
			//Ré-initialisation des paramètres
    		dataProcess.setNoAirflowCount(0);
    		dataProcess.setStandCount(0);
    		dataProcess.setTempHyperCount(0);
    		dataProcess.setTempHypoCount(0);

			//Création de l'alerte
    		idAlert = EHealth.db.createAlert(alert
			
			//Création de la notification push
    		sendNotification(alert.getAlertName(), idAlert);
    		
			// Augmentation de fech à 3Hz
    		try {
				btThread.write("MORE\n".getBytes("UTF-8"));
				btThread.write("MORE\n".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		arrayDataname = dataProcess.getDataNames();
    		
        	numberSensor = arrayDataname.size();
        	numberData = numberDataPerSensor*numberSensor;
			
			// Initialise le nombre de données à sauvegarder
    		cmpNeedToSave = numberData;
    		Log.d(TAG, "SensorNbre = " + Integer.toString(numberSensor));

			// Copie les données enregistrées dans la table buffer dans la table de données à enregistrer
    		for (String dataname : arrayDataname) {
	    		EHealth.db.moveDataToSavedData(dataname, idAlert);
	    		Log.d(TAG, "idAlert alerte = " + Integer.toString(idAlert));
    		}
			
			// Vide la table buffer
			EHealth.db.deleteAllData();
    	}
		//Retourne les données pour affichage
    	return arrayData;
    }
    
    public String[] getChunks(String ArduinoData) {
    	return ArduinoData.split(";");
    }
    
    private void sendNotification(String typeAlert, int idAlert){
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(context)
    	        .setSmallIcon(R.drawable.ic_launcher)
    	        .setContentTitle("Alert!")
    	        .setContentText("Une nouvelle alerte "+ typeAlert +" vient d'etre detectee.");
    	// Creates an explicit intent for an Activity in your app
    	Intent resultIntent = new Intent(context, GraphAlertActivity.class);
    	resultIntent.putExtra("alertId", idAlert);
    	PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    	mBuilder.setContentIntent(contentIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(mId, mBuilder.build());
    }
}

