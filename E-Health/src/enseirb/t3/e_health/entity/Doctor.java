package enseirb.t3.e_health.entity;

public class Doctor extends User{
	private int idDoctor;

	public Doctor(String username, String password) {
		super(username, password, "Doctor");
	}
	
	public int getIDDoctor() {
		return idDoctor;
	}

	public void setIDDoctor(int idDoctor) {
		this.idDoctor = idDoctor;
	}

}
