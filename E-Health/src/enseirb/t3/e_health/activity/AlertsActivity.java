package enseirb.t3.e_health.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Doctor;
import enseirb.t3.e_health.entity.Patient;
import enseirb.t3.e_health.session.SessionManager;

public class AlertsActivity extends Activity {

	SessionManager session;
	private ListView list;
	private ListAlertAdapter adapter;
	static View view;
	private Doctor doctor;
	private int idDoctor;
	
	// Nombre maximale d'alertes à afficher
	public static final int NbreAlertPrinted = 10;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_patient:
		//Création de patient (pop up)
			add();
			return true;
		case R.id.deconnexion:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alerts);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

		session = new SessionManager(getApplicationContext());
		idDoctor = session.getUserDetails();

		TextView titre = (TextView) findViewById(R.id.title);
		titre.setText(Html.fromHtml("MES <b>ALERTES</b>"));
		
		list = (ListView) findViewById(R.id.list_alerts);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				TextView tv1=(TextView)arg1.findViewById(R.id.alertId);
				TextView tv2=(TextView)arg1.findViewById(R.id.alert_type);
				
				Intent intent = new Intent(AlertsActivity.this, GraphAlertActivity.class);
				
				//Ajout d'un extra pour permettre à GraphActivity de récupérer l'id de l'alerte
				intent.putExtra("alertId", Integer.parseInt(tv1.getText().toString()));
				intent.putExtra("dataName", tv2.getText().toString());
				startActivity(intent);
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				return false;
			}

		});

		//Récupérer la liste des dernières alertes (n'affiche que celles relatives au bon docteur)
		ArrayList<Integer> idPatientList = EHealth.db.retrieveIdPatientListByDoctor(idDoctor);
		ArrayList<Alert> alertList = EHealth.db.retrieveAlert(idPatientList);

		adapter = new ListAlertAdapter(AlertsActivity.this, alertList);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.alerts, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// ajout d'un patient
	// add new feed
	private void add() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View addPatient = inflater.inflate(
				R.layout.activity_patient_registration, null);

		final DialogWrapper wrapper = new DialogWrapper(addPatient);

		new AlertDialog.Builder(this)
		.setTitle(R.string.menu_add_patient)
		.setView(addPatient)
		.setPositiveButton(R.string.register,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				processAdd(wrapper);
			}
		})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
			}
		}).show();
	}

	private void processAdd(DialogWrapper wrapper) {
		addPatient(wrapper.getFirstname(), wrapper.getLastname(),
				wrapper.getUsername(), wrapper.getPassword(),
				wrapper.getConfirmPwd());
	}

	class DialogWrapper {
		EditText firstname = null;
		EditText lastname = null;
		EditText username = null;
		EditText password = null;
		EditText confirmPwd = null;
		View base = null;

		DialogWrapper(View base) {
			this.base = base;
			this.firstname = (EditText) base.findViewById(R.id.firstname);
			this.lastname = (EditText) base.findViewById(R.id.lastname);
			this.username = (EditText) base.findViewById(R.id.username);
			this.password = (EditText) base.findViewById(R.id.password);
			this.confirmPwd = (EditText) base.findViewById(R.id.passwordCheck);
		}

		String getFirstname() {
			return (getFirstnameField().getText().toString());
		}

		String getLastname() {
			return (getLastnameField().getText().toString());
		}

		String getUsername() {
			return (getUsernameField().getText().toString());
		}

		String getPassword() {
			return (getPasswordField().getText().toString());
		}

		String getConfirmPwd() {
			return (getConfirmPwdField().getText().toString());
		}

		private EditText getFirstnameField() {
			if (this.firstname == null) {
				this.firstname = (EditText) base.findViewById(R.id.firstname);
			}

			return (this.firstname);
		}

		private EditText getLastnameField() {
			if (this.lastname == null) {
				this.lastname = (EditText) base.findViewById(R.id.lastname);
			}

			return (this.lastname);
		}

		private EditText getUsernameField() {
			if (this.username == null) {
				this.username = (EditText) base.findViewById(R.id.username);
			}

			return (this.username);
		}

		private EditText getPasswordField() {
			if (this.password == null) {
				this.password = (EditText) base.findViewById(R.id.password);
			}

			return (this.password);
		}

		private EditText getConfirmPwdField() {
			if (this.confirmPwd == null) {
				this.confirmPwd = (EditText) base
						.findViewById(R.id.passwordCheck);
			}

			return (this.confirmPwd);
		}
	}

	private void addPatient(String firstname, String lastname, String username,
			String password, String confirmPwd) {
		if (firstname.length() > 0 && lastname.length() > 0
				&& username.length() > 0 && password.length() > 0
				&& confirmPwd.length() > 0) {

			// Vérifie la disponibilité des identifiants
			if (EHealth.db.doesUserExist(username))
				createDialog("Username already in use.");
			else {
				// Check password
				if (password.equals(confirmPwd)) {

					Patient patient = new Patient(firstname, lastname,
							idDoctor, username, password);
					EHealth.db.createPatient(patient);

					createDialog("Patient account created !");
				} else {

					createDialog("Passwords don't match.");
				}
			}
		} else {

			createDialog("Empty field(s).");
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

	@Override
	public void onBackPressed() {
		session.logoutUser();
		super.onBackPressed();
	}
}
