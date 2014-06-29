package fr.iut.tchat;

import org.json.JSONException;
import org.json.JSONObject;

import fr.iut.tchat.library.Mail;
import fr.iut.tchat.library.UserFunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MdpPerdu extends Activity {

	private EditText inputEmail;
	private Button send;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	// Email sending
	private String toSender;
	private String messagePassword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_reset);

		inputEmail = (EditText) findViewById(R.id.eMailReset);
		send = (Button) findViewById(R.id.sendButton);

		// Send Button Click event
		send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				new MyAsyncTask().execute(email);
				
				new EmailSendingTask().execute();
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
					// registerErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {

						JSONObject json_user = json.getJSONObject("user");

						toSender = json_user.getString("email");
						messagePassword = json_user.getString("password");
						
						
//						sendMail("amessenger02@gmail.com", "@ltec77210", toSender, "pR", messagePassword);

						// Launch Dashboard Screen
//						Intent dashboard = new Intent(getApplicationContext(),
//								MainActivity.class);
//						// Close all views before launching Dashboard
//						dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						startActivity(dashboard);
//						// Close Registration Screen
//						finish();
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

	
	private class EmailSendingTask extends AsyncTask<String, Void, String>{
		
		private String success;
		
		@Override
		protected String doInBackground(String... params) {
			
			Mail m = new Mail("amessenger02@gmail.com", "@ltec77210");

			String[] toArr = { toSender };
			m.setTo(toArr);
			m.setFrom("amessenger02@gmail.com");
			m.setSubject("Password recovery from AMessenger");
			m.setBody("Your password is: " +messagePassword);

			try {

				if (m.send()) {
					Toast.makeText(MdpPerdu.this, "Email was sent successfully.",
							Toast.LENGTH_LONG).show();
					success = "1";
					
				} else {
					Toast.makeText(MdpPerdu.this, "Email was not sent.",
							Toast.LENGTH_LONG).show();
				}
					success = "0";
			} catch (Exception e) {
				Log.e("MdpPerdu", "Could not send email", e);
			}
			return success;
		}
		
		protected void onPostExecute(String success) {
			
			Intent dashboard = new Intent(getApplicationContext(),
					MainActivity.class);
			
			dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			startActivity(dashboard);
		}
		
	}	


}
