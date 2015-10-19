package enseirb.t3.e_health.entity;

import java.util.Date;

public class Alert {

	private int idAlert;
	private int idPatient;
	private Date date;
	private String alertName;
	
	public Alert(int idPatient, Date date, String alertName){
		this.idPatient = idPatient;
		this.date=date;
		this.alertName = alertName;
	}
	
	public void setIDAlert(int idAlert) {
		this.idAlert = idAlert;
	}
	public int getIDAlert() {
		return idAlert;
	}
	public int getIDPatient(){
		return this.idPatient;
	}
	public void setPatient(int idPatient){
		this.idPatient = idPatient;
	}
	public Date getDate(){
		return this.date;
	}
	public void setDate(Date date){
		this.date = date;
	}
	public String getAlertName(){
		return this.alertName;
	}
	public void setAlertName(String alertName){
		this.alertName = alertName;
	}
}
