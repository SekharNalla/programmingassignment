package com.sekhar.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String TAG = "DBAdapter";

	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	
	public static final String KEY_IMAGE_PATH = "imagepath";
	public static final int COL_IMAGE_PATH = 1;

	public static final String KEY_CITY_LOCATION = "city";
	public static final int COL_CITY_NAME = 2;
	
	public static final String KEY_LATITUDE = "latitude";
	public static final int COL_LATITUDE = 3;
	
	public static final String KEY_LONGITUDE = "longitude";
	public static final int COL_LONGITUDE = 4;
	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_IMAGE_PATH,KEY_CITY_LOCATION,KEY_LATITUDE, KEY_LONGITUDE};
	
	//Database name and table name
	public static final String DATABASE_NAME = "photosDb";
	public static final String DATABASE_TABLE = "mainTable";

	// Track DB version
	public static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE_SQL = "create table " + DATABASE_TABLE + " (" +
			KEY_ROWID  + " integer primary key autoincrement, " + 
			KEY_IMAGE_PATH + " text not null, " +
			KEY_CITY_LOCATION + " text not null, " +
			KEY_LATITUDE + " text not null, " +
			KEY_LONGITUDE + " text not null " +
			");";

	@SuppressWarnings("unused")
	private final Context context;
	
	private static DBAdapter instance;

	private static DatabaseHelper myDBHelper;
	private static SQLiteDatabase db;

	//private Constructor: SingleTon Model
	private  DBAdapter(Context context) {
		this.context = context;
		myDBHelper = new DatabaseHelper(context);
	}
	
	//method for getting an instance of DB
	public static DBAdapter getInstance(Context context) {
		
		if(instance == null) {
			synchronized (DBAdapter.class) {
				instance = new DBAdapter(context);
			}
		}
		
		return instance;
	}

	// Open the database connection.
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}

	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}

	// Add a new set of values to the database.
	public long insertRow(String photoPath, String cityLocation, String latitude, String longitude) {
		// Create row's data:
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_IMAGE_PATH, photoPath);
		initialValues.put(KEY_CITY_LOCATION, cityLocation);
		initialValues.put(KEY_LATITUDE, latitude);
		initialValues.put(KEY_LONGITUDE, longitude);
		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}

	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all data in the database.
		public Cursor getAllRows() {
			String where = null;
			Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
								where, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		}

		// Get a specific row (by rowId)
		public Cursor getRow(long rowId) {
			String where = KEY_ROWID + "=" + rowId;
			Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		}

	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_SQL);
			Log.i(TAG, "onCreate Database");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			// Destroy the old database:
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// Recreate new database:
			onCreate(db);
		}
	}
}