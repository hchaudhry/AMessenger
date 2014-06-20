package fr.iut.tchat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import fr.iut.tchat.library.DatabaseHandler;
import fr.iut.tchat.library.UserFunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Contact extends Activity {

	private ImageView btnAddContact;
	private Cursor CursorList;
    private ListView ContactsListView;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
 
        btnAddContact = (ImageView) findViewById(R.id.btnAjoutContact);
        
        ContactsListView = (ListView)this.findViewById(R.id.contactsList);
        List<String> ListContact = new ArrayList<String>();

        ContactDB PhonebookDB = new ContactDB(this);
        PhonebookDB.open();
        CursorList = PhonebookDB.getAllContacts();

        if (CursorList.moveToFirst()) 
        {
            do
            {
                ListContact.add(CursorList.getString(1).toString());
            }while (CursorList.moveToNext());
        }

        ContactsListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ListContact));  

        PhonebookDB.close();
        
        ContactsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
        
        btnAddContact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),CreateContact.class);
				startActivity(intent);
				finish();
			}
		});
    }
}
