package fr.iut.tchat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyContact extends Activity {
	
	private EditText email;
	private EditText nom;
	private EditText prenom;
	private Cursor contact;
	
	private String emailDB;
	private String nomDB;
	private String prenomDB;
	private String ContactID;
	
	private Button modify;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_contact);
        
        email = (EditText) this.findViewById(R.id.emailModify);
        nom = (EditText) this.findViewById(R.id.nomModify);
        prenom = (EditText) this.findViewById(R.id.prenomModify);
        modify = (Button) this.findViewById(R.id.modify);
        
        Intent i = getIntent();
        String mail = i.getStringExtra("mail");
        
        final ContactDB ContatsDB = new ContactDB(this);
        
        ContatsDB.open();
        contact = ContatsDB.getContactsWithEmail(mail);
        
        if (contact.moveToFirst()) {
			prenomDB = contact.getString(contact.getColumnIndex("Prenom"));
			nomDB = contact.getString(contact.getColumnIndex("Nom"));
			ContactID = contact.getString(contact.getColumnIndex("ContactID"));
		}
        
        prenom.setText(prenomDB);
        nom.setText(nomDB);
        email.setText(mail);
        
        modify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        
				long id = Long.parseLong(ContactID);
				String emailString = email.getText().toString();
				String prenomString = prenom.getText().toString();
				String nomString = nom.getText().toString(); 
				
		        boolean contactUpdate = ContatsDB.updateContacts(id, nomString, prenomString, emailString);
		        ContatsDB.close();
		        
		        Intent intent = new Intent(getApplicationContext(), Contact.class);
				startActivity(intent);
				finish();
			}
		});
    }

}
