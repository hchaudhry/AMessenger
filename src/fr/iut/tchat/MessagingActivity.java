package fr.iut.tchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import fr.iut.tchat.library.DatabaseHandler;

public class MessagingActivity extends Activity{

	private final static String SERVER_HOST = "talk.google.com";
	private final static int SERVER_PORT = 5222;
	private final static String SERVICE_NAME = "gmail.com";	
	private static String LOGIN = "";
	private static String PASSWORD = "";


	private List<String> m_discussionThread;
	private ArrayAdapter<String> m_discussionThreadAdapter;
	private XMPPConnection m_connection;
	private Handler m_handler;
	private HashMap<String, String> userInfos;
	private String userMail;
	private String userPassword;
	private EditText recipient;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_messages);
        
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        userInfos = db.getUserDetails();
        
        Iterator iter = userInfos.entrySet().iterator();
        
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			
			if (mEntry.getKey() == "email") {
				userMail = (String) mEntry.getValue();
			}
			
			if (mEntry.getKey() == "password"){
				userPassword = (String) mEntry.getValue();
			}
		}
		
		LOGIN = userMail;
		PASSWORD = userPassword;
		
        m_handler = new Handler();
		try {
			initConnection();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		recipient = (EditText) this.findViewById(R.id.recipient);
		
		Intent i = getIntent();
        String mail = i.getStringExtra("mail");
        recipient.setText(mail);
		
		final EditText message = (EditText) this.findViewById(R.id.message);		
		ListView list = (ListView) this.findViewById(R.id.thread);
		
		m_discussionThread = new ArrayList<String>();
		m_discussionThreadAdapter = new ArrayAdapter<String>(this,
				R.layout.multi_line_list_item, m_discussionThread);
		list.setAdapter(m_discussionThreadAdapter);
		
		Button send = (Button) this.findViewById(R.id.send);
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String to = recipient.getText().toString();
				String text = message.getText().toString();
		
				Message msg = new Message(to, Message.Type.chat);
				msg.setBody(text);
				m_connection.sendPacket(msg);
				m_discussionThread.add("moi :");
				m_discussionThread.add(text);
				m_discussionThreadAdapter.notifyDataSetChanged();
			}
		});
	}


	private void initConnection() throws XMPPException {
		//Initialisation de la connexion
        ConnectionConfiguration config =
                new ConnectionConfiguration(SERVER_HOST, SERVER_PORT, SERVICE_NAME);
        m_connection = new XMPPConnection(config);
        m_connection.connect();
        m_connection.login(LOGIN, PASSWORD);
        Presence presence = new Presence(Presence.Type.available);
        m_connection.sendPacket(presence);
       
        //enregistrement de l'�couteur de messages
		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		m_connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						m_discussionThread.add(recipient.getText().toString() + ":");
						m_discussionThread.add(message.getBody());
						
						m_handler.post(new Runnable() {
							public void run() {
								m_discussionThreadAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}, filter);
	}

}
