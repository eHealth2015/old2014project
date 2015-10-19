package enseirb.t3.e_health.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import enseirb.t3.e_health.activity.AuthentificationActivity;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "userSession";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User id (make variable public to access from outside)
	public static final String KEY_ID_USER = "idUser";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(int userId) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing user id in pref
		editor.putInt(KEY_ID_USER, userId);

		// commit changes
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, AuthentificationActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}

	}

	/**
	 * Get stored session data
	 * */
	public int getUserDetails() {
		return pref.getInt(KEY_ID_USER, 0);
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser (){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}	

	public boolean isContains (String key) {
		return pref.contains(key);
	}
}
