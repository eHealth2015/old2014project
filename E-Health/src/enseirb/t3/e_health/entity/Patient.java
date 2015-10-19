package enseirb.t3.e_health.entity;

public class Patient extends User{

	private int idPatient;
	private int idDoctor;
	private String lastname;
	private String firstname;
	
	public Patient(){
		super();
	}
	
	public Patient(String firstname, String lastname, int idDoctor, String username, String password) {
		super(username, password, "Patient");
		this.idDoctor = idDoctor;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	public int getIDPatient() {
		return idPatient;
	}

	public void setIDPatient(int idPatient) {
		this.idPatient = idPatient;
	}
	
	public int getIDDoctor() {
		return idDoctor;
	}

	public void setIDDoctor(int idDoctor) {
		this.idDoctor = idDoctor;
	}
	
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
}