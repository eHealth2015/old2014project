package enseirb.t3.e_health.process;

import java.util.ArrayList;

import android.util.Log;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;

//Effectue les corrélations
public class DataProcess {
	private static Alert alert;
	private Data data;
	private ArrayList<String> alertes = new ArrayList<String>();
	private ArrayList<String> dataNames;
	private String TAG = "DataProcess";

	// Airflow processing variables
	private int noAirflowCount = 0;

	// Position processing variables
	private int position;
	private int previousPosition;
	private int standCount = 0;

	// Temperature processing variables
	private int tempHypoCount = 0;
	private int tempHyperCount = 0;


	public ArrayList<String> getDataNames() {
		return this.dataNames;
	}
	
	public void setStandCount(int standCount) {
		this.standCount = standCount;
	}
	
	public void setNoAirflowCount(int noAirflowCount) {
		this.noAirflowCount = noAirflowCount;
	}
	
	public void setTempHypoCount(int tempHypoCount) {
		this.tempHypoCount = tempHypoCount;
	}
	
	public void setTempHyperCount(int tempHyperCount) {
		this.tempHyperCount = tempHyperCount;
	}

	public DataProcess () {

		this.data = null;
	}

	public DataProcess (Data data) {
		this.data = data;
	}


	public void process (Data data) {

		this.data = data;

		switch (data.getDataname()) {
		case "A":
			if (Double.parseDouble(data.getValue()) < 250) {
				Log.d(TAG, "Valeur airflow : " + Double.parseDouble(data.getValue()));
				Log.d(TAG, "airflow < 128");
				this.noAirflowCount++;
			}
			else 
				this.noAirflowCount = 0;
			Log.d(TAG, "noAirflowCmpt = " + this.noAirflowCount);
			if (this.noAirflowCount == 10) {
				Log.d(TAG, "airflow = 10");
				alertes.add("Apnee");
				this.noAirflowCount = 0;
			}
			break;
		case "B":
			if (Double.parseDouble(data.getValue()) > 100) {
				alertes.add("Tachycardie");
			}
			else if (Double.parseDouble(data.getValue()) < 99) {
				alertes.add("Bradycardie");
			}
			break;
		case "O":
			if (Double.parseDouble(data.getValue()) < 99 && Double.parseDouble(data.getValue()) > 1)
					alertes.add("Hypoxemie");
			break;
		case "P":
			previousPosition = position;
			if (Double.parseDouble(data.getValue()) == 1) {
				position = 1;
			} else if (Double.parseDouble(data.getValue()) == 2) {
				position = 2;
			} else if (Double.parseDouble(data.getValue()) == 3) {
				position = 3;
			} else if (Double.parseDouble(data.getValue()) == 4) {
				position = 4;
			}
			// position debout
			else if (Double.parseDouble(data.getValue()) == 5) {
				position = 5;
				this.standCount++;
				if (standCount == 20) {
					alertes.add("Somnambulisme");
				}
			}
			break;
		case "T":
			if (Double.parseDouble(data.getValue()) > 38) {
				tempHypoCount = 0;
				tempHyperCount++;
			}
			else if (Double.parseDouble(data.getValue()) < 35 && Double.parseDouble(data.getValue()) > 1) {
				tempHyperCount = 0;
				tempHypoCount++;
			}
			else {
				tempHyperCount = 0;
				tempHypoCount = 0;
			}
			if (tempHypoCount == 10) {
				alertes.add("Hypothermie");
				tempHypoCount = 0;
			} else if (tempHyperCount == 10) {
				alertes.add("Hyperthermie");
				tempHyperCount = 0;
			}
			break;
		case "C":
			if (Math.abs(Double.parseDouble(data.getValue())) > 5) {
				Log.d("alerte", "sueur");
				alertes.add("Sueur");
			}
			break;
		case "R":
			// Sueur déterminée par la conductivité
			// Sueur : R < 1500 Ohm
			break;
		case "V":
			// Sueur déjà déterminée par la conductivité
			break;
		default :
			break;
		}
	}

	public Alert correlation () {
		alert = null;
		this.dataNames = new ArrayList<String>();

		if (alertes.contains("Tachycardie") && alertes.contains("Sueur") && position != 5 && previousPosition == 5) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Malaise");
			this.dataNames.add("B");
			this.dataNames.add("R");
			this.dataNames.add("P");
			alertes = new ArrayList<String>();
			return alert;
		}

		if (alertes.contains("Bradycardie") && alertes.contains("Hypoxemie")) {
			if (alertes.contains("Sueur")) {
				alert = new Alert(data.getIdPatient(), data.getDate(), "Angine de poitrine");
				this.dataNames.add("B");
				this.dataNames.add("O");
				this.dataNames.add("R");
				alertes = new ArrayList<String>();
				return alert;
			}
		}

		if (alertes.contains("Apnee")) {

			alert = new Alert(data.getIdPatient(), data.getDate(), "Apnee");
			this.dataNames.add("A");
			this.dataNames.add("B");
			this.dataNames.add("O");
			alertes = new ArrayList<String>();
			return alert;
		}

		if (alertes.contains("Tachycardie") && alertes.contains("Hypoxemie") && alertes.contains("Sueur")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Problème cardiaque");
			this.dataNames.add("B");
			this.dataNames.add("O");
			this.dataNames.add("R");
			alertes = new ArrayList<String>();
			return alert;
		}
/*
		if  (alertes.contains("Hypothermie")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hypothermie");
			this.dataNames.add("T");
			alertes = new ArrayList<String>();
			return alert;
		}
*/
		if  (alertes.contains("Hyperthermie")) {
			alert = new Alert(data.getIdPatient(), data.getDate(), "Hyperthermie");
			this.dataNames.add("T");
			alertes = new ArrayList<String>();
			return alert;
		}

		if  (alertes.contains("Somnambulisme")) {
			this.dataNames.add("P");
			alert = new Alert(data.getIdPatient(), data.getDate(), "Somnambulisme");
			alertes = new ArrayList<String>();
			return alert;
		}
		
		alertes = new ArrayList<String>();
		return alert;
	}
}