package fr.iut.tchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DeleteContact extends Activity{

	private boolean contactDel;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
        String mail = i.getStringExtra("mail");
        
        final ContactDB contatsDB = new ContactDB(this);
        
        contatsDB.open();
        contactDel = contatsDB.deleteContactWithEmail(mail);
        contatsDB.close();

        Intent intent = new Intent(getApplicationContext(), Contact.class);
		startActivity(intent);
		finish();
	}
}
