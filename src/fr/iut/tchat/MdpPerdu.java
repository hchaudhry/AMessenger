package fr.iut.tchat;

import org.json.JSONException;
import org.json.JSONObject;

import fr.iut.tchat.library.DatabaseHandler;
import fr.iut.tchat.library.UserFunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MdpPerdu extends Activity {
	
	private EditText inputEmail;
	private Button send;
	
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_reset);
		
		inputEmail = (EditText) findViewById(R.id.eMailReset);
		send = (Button) findViewById(R.id.sendButton);
		
		// Send Button Click event
				send.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
						String email = inputEmail.getText().toString();
						new MyAsyncTask().execute(email);
					}
				});
	}
	
	
	// AsyncTask
	private class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length != 1)
				return null;
			JSONObject json = userFunction.passwordReset(params[0]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			// check for login response
			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
//					registerErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
//						// user successfully registred
//						// Store user details in SQLite Database
//						DatabaseHandler db = new DatabaseHandler(
//								getApplicationContext());
//						JSONObject json_user = json.getJSONObject("user");
//
//						// Clear all previous data in database
//						UserFunctions userFunction = new UserFunctions();
//						userFunction.logoutUser(getApplicationContext());
//						db.addUser(json_user.getString(KEY_NAME),
//								json_user.getString(KEY_EMAIL),
//								json.getString(KEY_UID)
////								json_user.getString(KEY_CREATED_AT)
//								);
						
						Context context = getApplicationContext();
						CharSequence text = "Un email vous été envoyé";
						int duration = Toast.LENGTH_SHORT;

						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
						
						// Launch Dashboard Screen
						Intent dashboard = new Intent(getApplicationContext(),
								MainActivity.class);
						// Close all views before launching Dashboard
						dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(dashboard);
						// Close Registration Screen
						finish();
					} else {
						Context context = getApplicationContext();
						CharSequence text = "Une erreur s'est produite";
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
