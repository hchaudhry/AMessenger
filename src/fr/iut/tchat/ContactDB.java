package fr.iut.tchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// ContactDb class that encapsulates the database functions of this application
public class ContactDB 
{

    // define constants
    public static final String KEY_ROWID = "ContactID";
    public static final String KEY_NOM= "Nom";
    public static final String KEY_PRENOM = "Prenom";
    public static final String KEY_EMAIL = "Email";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "ContactsDB";
    private static final String DATABASE_TABLE = "Contacts";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table Contacts (ContactID integer primary key autoincrement, Nom text not null, Prenom text not null, Email text not null);";

    private final Context context;  

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ContactDB(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // creates the db if it does not exist
        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        // called when the db needs upgrading
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                              int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }

  //open db
    public ContactDB open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close db
    public void close() 
    {
        DBHelper.close();
    }

    //add a record
    public long insertContacts(String nom, String prenom, String email) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOM, nom);
        initialValues.put(KEY_PRENOM, prenom);
        initialValues.put(KEY_EMAIL, email);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //delete a record
    public boolean deleteContacts(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    //delete a record with email
    public boolean deleteContactWithEmail(String email) 
    {
        return db.delete(DATABASE_TABLE, KEY_EMAIL + "='" + email + "'", null) > 0;
    }

    //get all record
    public Cursor getAllContacts() 
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_EMAIL}, null, null, null, null, null);
    }

    //get a record
    public Cursor getContacts(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_NOM,KEY_PRENOM,KEY_EMAIL}, 
                        KEY_ROWID + "=" + rowId, 
                        null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    //get a record with email
    public Cursor getContactsWithEmail(String email) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_NOM,KEY_PRENOM,KEY_EMAIL}, 
                        KEY_EMAIL + "='" + email + "'", 
                        null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //record update
    public boolean updateContacts(long rowId, String nom, String prenom, String email) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NOM, nom);
        args.put(KEY_PRENOM, prenom);
        args.put(KEY_EMAIL, email);
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
} 
