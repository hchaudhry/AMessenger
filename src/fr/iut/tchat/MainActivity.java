package fr.iut.tchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
 
import fr.iut.tchat.library.UserFunctions;
 
public class MainActivity extends Activity {
    UserFunctions userFunctions;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
        	Intent contactActivity = new Intent(getApplicationContext(), Contact.class);
        	contactActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(contactActivity); 
        }else{
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish();
        }       
    }
}