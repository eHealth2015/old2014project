package enseirb.t3.e_health.entity;

import java.util.Date;

public class Data {
	
	private int idData;
	private int idPatient;
	private String dataname;
	private String value;
	private Date date;
	
	public Data (String dataname, String value, Date date, int idPatient) {
		this.dataname = dataname;
		this.value = value;
		this.date = date;
		this.idPatient = idPatient;
	}

	public int getIDData() {
		return idData;
	}
	public void setIDData(int idData) {
		this.idData = idData;
	}
	
	public int getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(int idPatient) {
		this.idPatient = idPatient;
	}
	
	public String getDataname() {
		return dataname;
	}
	public void setDataname(String dataname) {
		this.dataname = dataname;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
