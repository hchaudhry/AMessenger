package fr.iut.tchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 
import fr.iut.tchat.library.UserFunctions;
 
public class MainActivity extends Activity {
    UserFunctions userFunctions;
    Button btnLogout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        /**
         * Dashboard Screen for the application
         * */       
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
       // user already logged in show databoard
        	
        	Intent jabberActivity = new Intent(getApplicationContext(), MessagingActivity.class);
        	startActivity(jabberActivity);
        	
//        	Intent contactActivity = new Intent(getApplicationContext(), Contact.class);
//        	startActivity(contactActivity);
             
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }       
    }
}