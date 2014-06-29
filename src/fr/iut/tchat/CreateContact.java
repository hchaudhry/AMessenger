package fr.iut.tchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateContact extends Activity
{

    //create button widgets
    private Button SaveButton;
    private Button CancelButton;
    private EditText nom;
    private EditText prenom;
    private EditText email;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);

        final ContactDB ContatsDB = new ContactDB(this);

        // reference widgets
        SaveButton = (Button)this.findViewById(R.id.add);
        CancelButton = (Button)this.findViewById(R.id.cancel);
        nom = (EditText)this.findViewById(R.id.nom);
        prenom = (EditText)this.findViewById(R.id.prenom);
        email = (EditText)this.findViewById(R.id.email);

        // create listener for the button
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put code here to execute when button is clicked
                long intNewID = 0;

                String strNom = nom.getText().toString();
                String strPrenom = prenom.getText().toString();
                String strMail = email.getText().toString();

                ContatsDB.open();
                intNewID = ContatsDB.insertContacts(strNom, strPrenom,strMail);
                ContatsDB.close();

                Intent Intent = new Intent(getApplicationContext(), Contact.class);
                setResult(RESULT_OK, Intent);
                finish();
            }
          });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent Intent = new Intent(getApplicationContext(), Contact.class);
                setResult(RESULT_OK, Intent);
                finish();
            }
          });
    }
}


