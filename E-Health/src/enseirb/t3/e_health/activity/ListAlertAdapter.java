package enseirb.t3.e_health.activity;

import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.entity.Alert;

public class ListAlertAdapter extends ArrayAdapter<Alert>{
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return super.isEnabled(position);
	}
	private LayoutInflater inflater;
	private List<Alert> alerts;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=inflater.inflate(R.layout.activity_alert_row, null);
		Alert alert=alerts.get(position);
		if(alert!=null) {
		
			//Rempli les informations affichées dans la liste des alertes
		
			TextView firstname=(TextView)row.findViewById(R.id.firstname);
			firstname.setText(EHealth.db.retrievePatient(alert.getIDPatient()).getFirstname());
			
			TextView lastname=(TextView)row.findViewById(R.id.lastname);
			lastname.setText(EHealth.db.retrievePatient(alert.getIDPatient()).getLastname());
			
			TextView date=(TextView)row.findViewById(R.id.date);
			DateTime dt=new DateTime(alert.getDate());
			date.setText(dt.toString("dd/MM/yyyy, HH:mm:ss"));
			
			TextView dataName=(TextView)row.findViewById(R.id.alert_type);
			dataName.setText(alert.getAlertName());
			
			TextView alertId=(TextView)row.findViewById(R.id.alertId);
			alertId.setText(Integer.toString(alert.getIDAlert()));
		}
		return row;
	}

	public ListAlertAdapter(Context context, List<Alert> alerts) {
		super(context, R.layout.activity_alert_row, alerts);
		this.inflater = LayoutInflater.from(context);
		this.alerts = alerts;
	}
}
