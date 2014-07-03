package fr.iut.tchat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import fr.iut.tchat.library.DatabaseHandler;
import fr.iut.tchat.library.UserFunctions;

public class LoginActivity extends Activity {
	Button btnLogin;
	TextView btnLinkToRegister;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "id";
	private static String KEY_NAME = "first_name";
	private static String KEY_EMAIL = "email";
	private static String kEY_PASS = "password";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.editTextEmail);
		inputPassword = (EditText) findViewById(R.id.editTextMDP);
		btnLogin = (Button) findViewById(R.id.btnConnexion);
		btnLinkToRegister = (TextView) findViewById(R.id.txtInscrire);
	    
	    // Password forgot button Click Event
	    final TextView mdpO = (TextView) findViewById(R.id.txtMDPoublie); 
	    mdpO.setOnClickListener(new OnClickListener() {
	      
	      
	      public void onClick(View v) {
	     Intent intent = new Intent(LoginActivity.this, MdpPerdu.class);
	     startActivity(intent);
	     }
	    });

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				new MyAsyncTask().execute(email, password);
			}
		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	// AsyncTask for the login process
	private class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length != 2)
				return null;
			JSONObject json = userFunction.loginUser(params[0], params[1]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// user successfully logged in
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");

						// Clear all previous data in database
						UserFunctions userFunction = new UserFunctions();
						userFunction.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_NAME),
								json_user.getString(KEY_EMAIL),
								json_user.getString(kEY_PASS),
								json.getString(KEY_UID)
								);

						Intent dashboard = new Intent(getApplicationContext(),
								MainActivity.class);

						dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(dashboard);

						finish();
					} else {
						Context context = getApplicationContext();
						CharSequence text = "Identifiants incorrects";
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
