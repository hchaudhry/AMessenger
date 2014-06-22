package fr.iut.tchat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class DeleteContact extends Activity{

	private boolean contactDel;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
        String mail = i.getStringExtra("mail");
        Toast.makeText(this, "edit: " + mail, Toast.LENGTH_SHORT).show();
        
        final ContactDB ContatsDB = new ContactDB(this);
        
        ContatsDB.open();
        contactDel = ContatsDB.deleteContactWithEmail(mail);
        ContatsDB.close();

        Intent intent = new Intent(getApplicationContext(), Contact.class);
		startActivity(intent);
		finish();
	}
}
