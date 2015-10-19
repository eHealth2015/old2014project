package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.entity.Doctor;
import enseirb.t3.e_health.entity.Patient;
import enseirb.t3.e_health.entity.User;
import enseirb.t3.e_health.session.SessionManager;

/**
 * @author catdiop
 * 
 */
public class AuthentificationActivity extends Activity implements
		OnClickListener {
	SessionManager session;
	private Intent intent;
	private TextView usernameView;
	private TextView passwordView;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.authentificate, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			break;
		case R.id.add_doctor:
			// Add Doctor
			intent = new Intent(AuthentificationActivity.this,
					DoctorRegistrationActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentification);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

		// Instancie une session
		session = new SessionManager(getApplicationContext());

		Button connexion = (Button) findViewById(R.id.connexion);
		connexion.setOnClickListener(this);
		
		TextView titleView = (TextView) findViewById(R.id.title);
		titleView.setText(Html.fromHtml("ME <b>CONNECTER<\b>"));
	}

	// @Override
	protected void onResume() {
		if (session.isContains(SessionManager.KEY_ID_USER)) {
			//Si une dession est déjà en cours, ré-ouvre le menu Doctor ou Patient selon le cas
			switch (EHealth.db.retrieveUser(session.getUserDetails()).getType()) {
			case "Doctor":
				intent = new Intent(AuthentificationActivity.this,
						AlertsActivity.class);
				break;
			case "Patient":
				intent = new Intent(AuthentificationActivity.this, Graph.class);
				startActivity(intent);
			default:
				break;
			}
			startActivity(intent);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		User user;
		String username;
		String password;

		switch (v.getId()) {
		// Si l'utilisateur appuie sur connexion
		case R.id.connexion:
			usernameView = (TextView) findViewById(R.id.username);
			passwordView = (TextView) findViewById(R.id.password);

			username = usernameView.getText().toString();
			password = passwordView.getText().toString();

			//Selon les informations récupérées, ouvre le menu Doctor ou Patient
			if ((user = EHealth.db.retrieveUser(username, password)) != null) {
				session.createLoginSession(user.getIDUser());
				if (user.getType().equals("Doctor"))
					intent = new Intent(AuthentificationActivity.this,
							AlertsActivity.class);
				else if (user.getType().equals("Patient"))
					intent = new Intent(AuthentificationActivity.this,
							Graph.class);
				startActivity(intent);
			} else
				createDialog("Le nom d'utilisateur ou le mot de passe est incorrect");
			break;
		}
	}

	private void createDialog(String msg) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(msg);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		dialog.show();
	}
}
