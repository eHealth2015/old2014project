package enseirb.t3.e_health.entity;

public class User {

	private int idUser;
	private String username;
	private String password;
	private String type;
	
	public User(){
		this.username=null;
		this.password=null;
		this.type = null;
	}
	
	public User(String username, String password, String type) {
		this.username = username;
		this.password = password;
		this.type = type;
	}
	
	public User(int idUser, String username, String password, String type) {
		this.idUser = idUser;
		this.username = username;
		this.password = password;
		this.type = type;
	}
	
	public int getIDUser() {
		return idUser;
	}
	public void setIDUser(int idUser) {
		this.idUser = idUser;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
