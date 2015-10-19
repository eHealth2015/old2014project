package enseirb.t3.e_health.DAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;
import enseirb.t3.e_health.activity.AlertsActivity;
import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.Doctor;
import enseirb.t3.e_health.entity.Patient;
import enseirb.t3.e_health.entity.User;

public class DatabaseHandler extends SQLiteOpenHelper {

	private String TAG = "DatabaseHandler";
	
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "E-health";

	// Users table name
	private static final String TABLE_USERS = "Users";
	// Patient table name
	private static final String TABLE_PATIENT = "Patient";
	// Doctor table name
	private static final String TABLE_DOCTOR = "Doctor";
	// Data table name
	private static final String TABLE_DATA = "Data";
	// Alert table name
	private static final String TABLE_ALERT = "Alert";
	// SavedData table name
	private static final String TABLE_SAVEDDATA = "SavedData";

	// Table Columns names
	private static final String KEY_ID_DATA = "idData";
	private static final String KEY_ID_USER = "idUser";
	private static final String KEY_ID_PATIENT = "idPatient";
	private static final String KEY_ID_DOCTOR = "idDoctor";
	private static final String KEY_ID_ALERT = "idAlert";
	private static final String KEY_ID_SAVEDDATA = "idSavedData";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_TYPE = "type";
	private static final String KEY_DATANAME = "dataname";
	private static final String KEY_ALERTNAME = "alertname";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DATE = "date";
	private static final String KEY_LASTNAME = "lastname";
	private static final String KEY_FIRSTNAME = "firstname";

	private static final String CREATE_USER_TABLE = "CREATE TABLE "
			+ TABLE_USERS + "(" + KEY_ID_USER + " INTEGER PRIMARY KEY,"
			+ KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_TYPE
			+ " TEXT )";

	private static final String CREATE_PATIENT_TABLE = "CREATE TABLE "
			+ TABLE_PATIENT + "(" + KEY_ID_PATIENT + " INTEGER,"
			+ KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT,"
			+ KEY_ID_DOCTOR + " INTEGER" + " )";

	private static final String CREATE_DOCTOR_TABLE = "CREATE TABLE "
			+ TABLE_DOCTOR + "(" + KEY_ID_DOCTOR + " INTEGER" + " )";

	private static final String CREATE_DATA_TABLE = "CREATE TABLE "
			+ TABLE_DATA + "(" + KEY_ID_DATA + " INTEGER PRIMARY KEY,"
			+ KEY_DATANAME + " TEXT," + KEY_VALUE + " TEXT," + KEY_DATE
			+ " TEXT," + "" + KEY_ID_PATIENT + " INTEGER" + " )";

	private static final String CREATE_ALERT_TABLE = "CREATE TABLE "
			+ TABLE_ALERT + "(" + KEY_ID_ALERT + " INTEGER PRIMARY KEY,"
			+ KEY_ID_PATIENT + " INTEGER," + KEY_DATE + " TEXT,"
			+ KEY_ALERTNAME + " TEXT" + " )";

	private static final String CREATE_SAVEDDATA_TABLE = "CREATE TABLE "
			+ TABLE_SAVEDDATA + "(" + KEY_ID_SAVEDDATA
			+ " INTEGER PRIMARY KEY," + KEY_ID_ALERT + " INTEGER,"
			+ KEY_DATANAME + " TEXT," + KEY_VALUE + " TEXT," + KEY_DATE
			+ " TEXT" + "" + " )";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_DATA_TABLE);
		db.execSQL(CREATE_PATIENT_TABLE);
		db.execSQL(CREATE_DOCTOR_TABLE);
		db.execSQL(CREATE_ALERT_TABLE);
		db.execSQL(CREATE_SAVEDDATA_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVEDDATA);

		// Create tables again
		onCreate(db);
	}

	private int createUser(User object) {
		SQLiteDatabase db = this.getWritableDatabase();
		int userID;

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());
		values.put(KEY_TYPE, object.getType());

		db.insert(TABLE_USERS, null, values);

		userID = retrieveUser(object.getUsername(), object.getPassword())
				.getIDUser();

		return userID;
	}

	public User retrieveUser(String username, String password) {
		SQLiteDatabase db = this.getReadableDatabase();
		User user = null;

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USERNAME + " = ?" + " AND " + KEY_PASSWORD + " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { username,
				password });

		if (cursor.moveToFirst())
			user = new User(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					cursor.getString(3));
		return user;
	}

	public User retrieveUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		User user = null;

		Cursor cursor = db.query(TABLE_USERS, null, KEY_ID_USER + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);

		if (cursor.moveToFirst())
			user = new User(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					cursor.getString(3));
		return user;
	}

	private int updateUser(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());

		return db.update(TABLE_USERS, values, KEY_ID_USER + " = ?",
				new String[] { Integer.toString(object.getIDUser()) });
	}

	private void deleteUser(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID_USER + " = ?",
				new String[] { Integer.toString(id) });
		db.close();
	}

	public boolean doesUserExist(String username) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USERNAME + " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { username });

		return cursor.moveToFirst();
	}

	public void createData(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy");
		String date = df.format(object.getDate());
		Log.d(TAG, "date enregistrée avec format = " + date);
		
		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, date);
		values.put(KEY_ID_PATIENT, object.getIdPatient());
		
		db.insert(TABLE_DATA, null, values);
	}

	public void moveDataToSavedData(String dataname, int idAlert) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		Cursor cursor = db.query(TABLE_DATA, null, KEY_DATANAME + "=?",
				new String[] { dataname }, null, null, null, null);

		while (cursor.moveToNext()) {
			values.put(KEY_ID_ALERT, Integer.toString(idAlert));
			values.put(KEY_DATANAME, cursor.getString(1));
			values.put(KEY_VALUE, cursor.getString(2));
			values.put(KEY_DATE, cursor.getString(3));

			db.insert(TABLE_SAVEDDATA, null, values);
			Log.d(TAG, "Date = " + cursor.getString(3) + " enregistrée");
		}
		Log.d(TAG, "DataName = " + dataname + " enregistrée");
	}

	public int updateData(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate().toString());

		return db.update(TABLE_DATA, values, KEY_ID_DATA + " = ?",
				new String[] { Integer.toString(object.getIDData()) });
	}

	public void deleteData(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, KEY_ID_DATA + " = ?",
				new String[] { Integer.toString(id) });
		db.close();
	}

	public void deleteAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, null, null);
		db.close();
	}

	public int getNumberData() {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_DATA;
		int number = 0;

		Cursor cursor = db.rawQuery(selectQuery, null);

		while (cursor.moveToNext())
			number++;

		return number;
	}

	public void deleteLastData() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_DATA;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst())
			db.delete(TABLE_DATA, KEY_ID_DATA + "=?", new String[] { cursor
					.getString(cursor.getColumnIndex(KEY_ID_DATA)) });
		db.close();
	}

	public void createPatient(Patient object) {
		SQLiteDatabase db = this.getWritableDatabase();
		int userID;

		userID = createUser((User) object);

		ContentValues values = new ContentValues();
		values.put(KEY_ID_PATIENT, userID);
		values.put(KEY_LASTNAME, object.getLastname());
		values.put(KEY_FIRSTNAME, object.getFirstname());
		values.put(KEY_ID_DOCTOR, object.getIDDoctor());

		db.insert(TABLE_PATIENT, null, values);
	}

	public Patient retrievePatient(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Patient patient = null;
		User user = retrieveUser(id);
		int i = 10;

		Cursor cursor = db.query(TABLE_PATIENT, null, KEY_ID_PATIENT + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);

		if (cursor.moveToFirst()) {
			i = cursor.getType(3);
			patient = new Patient(cursor.getString(1), cursor.getString(2), i,
					user.getUsername(), user.getPassword());
			patient.setIDPatient(id);
			patient.setIDUser(id);
		}
		Log.d("i", Integer.toString(i));
		return patient;
	}

	public ArrayList<Integer> retrieveIdPatientListByDoctor(int idDoctor) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Integer> idPatientList = new ArrayList<Integer>();

		String selectQuery = "SELECT * FROM " + TABLE_PATIENT + " WHERE "
				+ KEY_ID_DOCTOR + " = ?";

		Cursor cursor = db.rawQuery(selectQuery,
				new String[] { Integer.toString(idDoctor) });

		while (cursor.moveToNext())
			idPatientList.add(cursor.getInt(0));
		return idPatientList;
	}

	public void deleteAllPatient() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PATIENT, null, null);
		db.delete(TABLE_USERS, KEY_TYPE + "=?", new String[] { "Patient" });
		db.close();
	}

	public void createDoctor(Doctor object) {
		SQLiteDatabase db = this.getWritableDatabase();
		int userID;

		userID = createUser((User) object);

		ContentValues values = new ContentValues();
		values.put(KEY_ID_DOCTOR, userID);

		db.insert(TABLE_DOCTOR, null, values);
	}

	public Doctor retrieveDoctor(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Doctor doctor = null;
		User user = retrieveUser(id);

		Cursor cursor = db.query(TABLE_DOCTOR, null, KEY_ID_DOCTOR + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);

		if (cursor.moveToFirst()) {
			doctor = new Doctor(user.getUsername(), user.getPassword());
			doctor.setIDDoctor(id);
			doctor.setIDUser(id);
		}
		return doctor;
	}

	public void deleteAllDoctor() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DOCTOR, null, null);
		db.delete(TABLE_USERS, KEY_TYPE + "=?", new String[] { "Doctor" });
		db.close();
	}

	public int createAlert(Alert object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID_PATIENT, object.getIDPatient());
		values.put(KEY_DATE, object.getDate().toString());
		values.put(KEY_ALERTNAME, object.getAlertName());

		db.insert(TABLE_ALERT, null, values);

		return retrieveLastIdAlert();
	}

	public ArrayList<Alert> retrieveAlert(ArrayList<Integer> idPatientList) {
		SQLiteDatabase db = this.getReadableDatabase();
		Alert alert = null;
		ArrayList<Alert> alertList = new ArrayList<Alert>();
		Date date = null;
		int cmpt = 0;

		Cursor cursor = db.query(TABLE_ALERT, null, null, null, null, null,
				null, null);

		cursor.moveToLast();
		cursor.moveToNext();
		while (cursor.moveToPrevious() && cmpt < AlertsActivity.NbreAlertPrinted)
			if (idPatientList.contains(cursor.getInt(1))) {
				try {
					Log.d(TAG, "Date non parsée dans retrieveAlert = " + cursor.getString(2));
					date = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).parse(cursor.getString(2));
					Log.d(TAG, "Date parsée dans retrieveAlert = " + date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				alert = new Alert(cursor.getInt(1), date, cursor.getString(3));
				alert.setIDAlert(cursor.getInt(0));
				alertList.add(alert);
				cmpt++;
			}

		return alertList;
	}

	public int retrieveLastIdAlert() {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT *" + " FROM " + TABLE_ALERT;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToLast()) {
			return cursor.getInt(0);
		} else {
			return 0;
		}
	}

	public void deleteAllAlert() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALERT, null, null);
		db.close();
	}

	public void createSavedData(Data object, int idAlert) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy");
		String date = df.format(object.getDate());

		ContentValues values = new ContentValues();
		values.put(KEY_ID_ALERT, Integer.toString(idAlert));
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, date);
		
		Log.d(TAG, "Date après alerte = " + date + " enregistrée");

		db.insert(TABLE_SAVEDDATA, null, values);
	}
	
	public List<Data> retrieveDatasForAlert(int alertId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Data data=null;
		Date date=null;
		List<Data> datas=new LinkedList<Data>();
		
		Cursor cursor = db.query(TABLE_SAVEDDATA, null, KEY_ID_ALERT + "=?",
				new String[] { Integer.toString(alertId) }, null, null, null, null);

		while (cursor.moveToNext()) {
			Log.d(TAG, "Date dans retrieve data = " + cursor.getString(4) + " / " + cursor.getString(2) + " enregistrée");
			try {
				date = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy").parse(cursor.getString(4));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data = new Data(cursor.getString(2), cursor.getString(3), date, this.retrieveAlertFromId(alertId).getIDPatient());
			datas.add(data);
		}
		return datas;
	}
	
	public void deleteAllASavedData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SAVEDDATA, null, null);
		db.close();
	}
	
	public Alert retrieveAlertFromId(int alertId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Alert alert=null;
		Date date=null;
		

		Cursor cursor = db.query(TABLE_ALERT, null, KEY_ID_ALERT + "=?",
				new String[] { Integer.toString(alertId) }, null, null, null, null);

		
		if (cursor.moveToFirst()) {
			try {
				date = new SimpleDateFormat("EEE MMM dd HH:mm:ss.SSS z yyyy").parse(cursor.getString(2));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			alert = new Alert(cursor.getInt(1), date, cursor.getString(3));
		}
		return alert;
	}
	
	

}
