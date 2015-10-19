package enseirb.t3.e_health.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.e_health.R;

public class PatientRegistrationActivity extends Activity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_registration);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(this);

		EHealth.db.deleteAllData();
		EHealth.db.deleteAllPatient();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// Bouton "register"
		case R.id.register:
			TextView usernameView = (TextView) findViewById(R.id.username);
			TextView passwordView = (TextView) findViewById(R.id.password);
			TextView passwordCheckView = (TextView) findViewById(R.id.passwordCheck);

			if (usernameView.getText().length() > 0
					&& passwordView.getText().length() > 0
					&& passwordCheckView.getText().length() > 0) {

				// Vérifie la disponibilité des identifiants
				if (EHealth.db.doesUserExist(usernameView.getText().toString())) {

					createDialog("Username already in use.");
				} else {

					// Check password
					if (passwordView.getText().toString()
							.equals(passwordCheckView.getText().toString())) {
						createDialog("Patient account created !");

						Intent intent = new Intent(
								PatientRegistrationActivity.this,
								AuthentificationActivity.class);
						startActivity(intent);
					} else
						createDialog("Passwords don't match.");
				}
			} else
				createDialog("Empty field(s).");
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
