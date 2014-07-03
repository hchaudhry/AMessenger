package fr.iut.tchat;

import org.json.JSONException;
import org.json.JSONObject;

import fr.iut.tchat.library.DatabaseHandler;
import fr.iut.tchat.library.UserFunctions;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	
	Button btnRegister;
	// Button btnLinkToLogin;
	Button btnAnnuler;
	EditText inputName;
	EditText inputFirstName;
	EditText inputPseudo;
	EditText inputEmail;
//	EditText inputEmailCheck;
	EditText inputPassword;
//	EditText inputPasswordCheck;
	TextView registerErrorMsg;
	private RadioGroup inputSex;
	private RadioButton radioSexButton;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
//	private static String KEY_ERROR = "error";
//	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "id";
	private static String KEY_NAME = "first_name";
	private static String KEY_EMAIL = "email";
	private static String KEY_PASS = "password";
//	private static String KEY_CREATED_AT = "created_at";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// Importing all assets like buttons, text fields
		inputSex = (RadioGroup) findViewById(R.id.radioSexe);
		inputName = (EditText) findViewById(R.id.editTextNom);
		inputFirstName = (EditText) findViewById(R.id.editTextPrenom);
		inputPseudo = (EditText) findViewById(R.id.EditTextPseudo);
		inputEmail = (EditText) findViewById(R.id.editTextMail1);
//		inputEmailCheck = (EditText) findViewById(R.id.editTextMail2);
		inputPassword = (EditText) findViewById(R.id.editTextMDP1);
//		inputPasswordCheck = (EditText) findViewById(R.id.editTextMDP2);
		btnRegister = (Button) findViewById(R.id.btnSinscrire);
		btnAnnuler = (Button) findViewById(R.id.btnAnnuler);

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String name = inputName.getText().toString();
				String firstName = inputFirstName.getText().toString();
				String pseudo = inputPseudo.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				
				int selected = inputSex.getCheckedRadioButtonId();
				radioSexButton = (RadioButton) findViewById(selected);
				String sex = radioSexButton.getText().toString();
				
				
				new MyAsyncTask().execute(sex, name, firstName, pseudo, email, password);
			}
		});

		// Clear all inputs
		btnAnnuler.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				inputName.setText("");
				inputFirstName.setText("");
				inputPseudo.setText("");
				inputEmail.setText("");
//				inputEmailCheck.setText("");
				inputPassword.setText("");
//				inputPasswordCheck.setText("");
			}
		});
	}

	// AsyncTask
	private class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length != 6)
				return null;
			JSONObject json = userFunction.registerUser(params[0], params[1],
					params[2], params[3], params[4], params[5]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			// check for login response
			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
//					registerErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// user successfully registred
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");

						// Clear all previous data in database
						UserFunctions userFunction = new UserFunctions();
						userFunction.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_NAME),
								json_user.getString(KEY_EMAIL),
								json_user.getString(KEY_PASS),
								json.getString(KEY_UID)
//								json_user.getString(KEY_CREATED_AT)
								);
						// Launch Dashboard Screen
						Intent dashboard = new Intent(getApplicationContext(),
								MainActivity.class);
						// Close all views before launching Dashboard
						dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(dashboard);
						// Close Registration Screen
						finish();
					} else {
						// Error in registration
//						registerErrorMsg
//								.setText("Error occured in registration");
						System.out.println("erreur");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
