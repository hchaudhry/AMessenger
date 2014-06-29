package fr.iut.tchat;

import java.util.ArrayList;
import java.util.List;

import fr.iut.tchat.library.UserFunctions;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Contact extends ListActivity {

	private Button btnAddContact;
	private Button logout;
	private Cursor CursorList;
	private ListView ContactsListView;
	private UserFunctions userFunctions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);

		userFunctions = new UserFunctions();
		
		btnAddContact = (Button) findViewById(R.id.btnAjoutContact);
		logout = (Button) findViewById(R.id.logout);

		ContactsListView = (ListView) this.findViewById(android.R.id.list);
		List<String> ListContact = new ArrayList<String>();

		ContactDB PhonebookDB = new ContactDB(this);
		PhonebookDB.open();
		CursorList = PhonebookDB.getAllContacts();

		if (CursorList.moveToFirst()) {
			do {
				ListContact.add(CursorList.getString(1).toString());
			} while (CursorList.moveToNext());
		}

		ContactsListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ListContact));

		PhonebookDB.close();
		
		registerForContextMenu(ContactsListView);

		ContactsListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemValue = ((TextView) view).getText().toString();
				Intent i = new Intent(getApplicationContext(),
						MessagingActivity.class);
				i.putExtra("mail", itemValue);
				startActivity(i);
			}
		});


		btnAddContact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						CreateContact.class);
				startActivity(intent);
				finish();
			}
		});
		
		logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				userFunctions.logoutUser(getApplicationContext());
				Intent login = new Intent(getApplicationContext(),
						LoginActivity.class);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
				finish();
			}
		});
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu_context, menu);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	String selectedWord = ((TextView) info.targetView).getText().toString();
    	switch(item.getItemId()) {
    	case R.id.edit:
    		Intent intentModify = new Intent(this, ModifyContact.class);
    		intentModify.putExtra("mail", selectedWord);
    		startActivity(intentModify);
    		return true;
    	case R.id.delete:
    		Intent i = new Intent(this, DeleteContact.class);
    		i.putExtra("mail", selectedWord);
    		startActivity(i);
    		return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    }
    
    @Override
    public void onRestart(){
    	super.onRestart();
    	
    	Intent contact = new Intent(getApplicationContext(), Contact.class);
		contact.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(contact);
		finish();
    }
}
