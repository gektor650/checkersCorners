package net.mydebug.chessgames.drive.db;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import net.mydebug.chessgames.drive.db.HistoryProviderMetaData.HistoryTableMetaData;

public class HistoryProvider extends ContentProvider{


		//Logging helper tag. No significance to providers.
	    private static final String TAG = "HistoryProvider";

	    //Projection maps are similar to "as" construct
	    //in an sql statement where by you can rename the 
	    //columns.
	    private static HashMap<String, String> sHistoryProjectionMap;
	    static 
	    {
	    	sHistoryProjectionMap = new HashMap<String, String>();
	    	sHistoryProjectionMap.put(HistoryTableMetaData._ID, 
	    							HistoryTableMetaData._ID);
	    	

	    	sHistoryProjectionMap.put(HistoryTableMetaData.HISTORY_GAME_ID, 
	    			   				HistoryTableMetaData.HISTORY_GAME_ID);
	    	sHistoryProjectionMap.put(HistoryTableMetaData.HISTORY_TURN_ID, 
	    			                HistoryTableMetaData.HISTORY_TURN_ID);
	    	sHistoryProjectionMap.put(HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN, 
	    			HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN);
	    	sHistoryProjectionMap.put(HistoryTableMetaData.HISTORY_GAME_DATA, 
	    			                HistoryTableMetaData.HISTORY_GAME_DATA);
	    	sHistoryProjectionMap.put(HistoryTableMetaData.HISTORY_GAME_TIME,
	    			                HistoryTableMetaData.HISTORY_GAME_TIME);
	    	
	    	//created date
	    	sHistoryProjectionMap.put(HistoryTableMetaData.CREATED_DATE, 
	    			                HistoryTableMetaData.CREATED_DATE);
	    }

	    //Provide a mechanism to identify
	    //all the incoming uri patterns.
	    private static final UriMatcher sUriMatcher;
	    private static final int INCOMING_HISTORY_COLLECTION_URI_INDICATOR = 1;
	    private static final int INCOMING_SINGLE_HISTORY_URI_INDICATOR = 2;
	    static {
	        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	        sUriMatcher.addURI(HistoryProviderMetaData.AUTHORITY, "history", 
	        				INCOMING_HISTORY_COLLECTION_URI_INDICATOR);
	        sUriMatcher.addURI(HistoryProviderMetaData.AUTHORITY, "history/#", 
	        		          INCOMING_SINGLE_HISTORY_URI_INDICATOR);

	    }

	    /**
	     * This class helps open, create, and upgrade the database file.
	     */
	    private static class DatabaseHelper extends SQLiteOpenHelper {

	        DatabaseHelper(Context context) {
	            super(context, 
	            		HistoryProviderMetaData.DATABASE_NAME, 
	            	null, 
	            	HistoryProviderMetaData.DATABASE_VERSION);
	        }

	        @Override
	        public void onCreate(SQLiteDatabase db) 
	        {
	            db.execSQL("CREATE TABLE " + HistoryTableMetaData.TABLE_NAME + " ("
	                    + HistoryTableMetaData._ID + " INTEGER PRIMARY KEY,"
	                    + HistoryTableMetaData.HISTORY_GAME_ID + " INTEGER,"
	                    + HistoryTableMetaData.HISTORY_TURN_ID + " INTEGER,"
	                    + HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN + " INTEGER,"
	                    + HistoryTableMetaData.HISTORY_GAME_TIME + " INTEGER,"
	                    + HistoryTableMetaData.HISTORY_GAME_DATA + " BLOB,"
	                    + HistoryTableMetaData.CREATED_DATE + " INTEGER"
	                    + ");");
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	        {
	            db.execSQL("DROP TABLE IF EXISTS " + 
	            		 HistoryTableMetaData.TABLE_NAME);
	            onCreate(db);
	        }
	    }

	    private DatabaseHelper mOpenHelper;

	    @Override
	    public boolean onCreate() 
	    {
	        mOpenHelper = new DatabaseHelper(getContext());
	        return true;
	    }

	    @Override
	    public Cursor query(Uri uri, String[] projection, String selection, 
	    		String[] selectionArgs,  String sortOrder) 
	    {
	        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

	        switch (sUriMatcher.match(uri)) {
	        case INCOMING_HISTORY_COLLECTION_URI_INDICATOR:
	            qb.setTables(HistoryTableMetaData.TABLE_NAME);
	            qb.setProjectionMap(sHistoryProjectionMap);
	            break;

	        case INCOMING_SINGLE_HISTORY_URI_INDICATOR:
	            qb.setTables(HistoryTableMetaData.TABLE_NAME);
	            qb.setProjectionMap(sHistoryProjectionMap);
	            qb.appendWhere(HistoryTableMetaData._ID + "=" 
	            		    + uri.getPathSegments().get(1));
	            break;

	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        // If no sort order is specified use the default
	        String orderBy;
	        if (TextUtils.isEmpty(sortOrder)) {
	            orderBy = HistoryTableMetaData.DEFAULT_SORT_ORDER;
	        } else {
	            orderBy = sortOrder;
	        }

	        // Get the database and run the query
	        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
	        Cursor c = qb.query(db, projection, selection, 
	        		   selectionArgs, null, null, orderBy);
	        
	        //example of getting a count
	        int i = c.getCount();

	        // Tell the cursor what uri to watch, 
	        // so it knows when its source data changes
	        c.setNotificationUri(getContext().getContentResolver(), uri);
	        return c;
	    }

	    @Override
	    public String getType(Uri uri) {
	        switch (sUriMatcher.match(uri)) {
	        case INCOMING_HISTORY_COLLECTION_URI_INDICATOR:
	            return HistoryTableMetaData.CONTENT_TYPE;

	        case INCOMING_SINGLE_HISTORY_URI_INDICATOR:
	            return HistoryTableMetaData.CONTENT_ITEM_TYPE;

	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }
	    }

	    @Override
	    public Uri insert(Uri uri, ContentValues initialValues) {
	        // Validate the requested uri
	        if (sUriMatcher.match(uri) 
	        		!= INCOMING_HISTORY_COLLECTION_URI_INDICATOR) 
	        {
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        ContentValues values;
	        if (initialValues != null) {
	            values = new ContentValues(initialValues);
	        } else {
	            values = new ContentValues();
	        }

	        Long now = Long.valueOf(System.currentTimeMillis());

	        // Make sure that the fields are all set
	        if (values.containsKey(HistoryTableMetaData.CREATED_DATE) == false) 
	        {
	            values.put(HistoryTableMetaData.CREATED_DATE, now);
	        }


	        if (values.containsKey(HistoryTableMetaData.HISTORY_GAME_ID) == false) 
	        {
	            throw new SQLException(
	               "Failed to insert row because Book Name is needed " + uri);
	        }

	        if (values.containsKey(HistoryTableMetaData.HISTORY_TURN_ID) == false) {
	            values.put(HistoryTableMetaData.HISTORY_TURN_ID, "Unknown TURN ID");
	        }
	        if (values.containsKey(HistoryTableMetaData.HISTORY_GAME_DATA) == false) {
	            values.put(HistoryTableMetaData.HISTORY_GAME_DATA, "Unknown DATA");
	        }
	        if (values.containsKey(HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN) == false) {
	        	values.put(HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN, "Unknown WHOS_TOURN");
	        }

	        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	        long rowId = db.insert(HistoryTableMetaData.TABLE_NAME, 
	        		HistoryTableMetaData.HISTORY_GAME_DATA, values);
	        if (rowId > 0) {
	            Uri insertedBookUri = 
	            	ContentUris.withAppendedId(
	            			HistoryTableMetaData.CONTENT_URI, rowId);
	            getContext()
	               .getContentResolver()
	                    .notifyChange(insertedBookUri, null);
	            
	            return insertedBookUri;
	        }

	        throw new SQLException("Failed to insert row into " + uri);
	    }

	    @Override
	    public int delete(Uri uri, String where, String[] whereArgs) {
	        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	        int count;
	        switch (sUriMatcher.match(uri)) {
	        case INCOMING_HISTORY_COLLECTION_URI_INDICATOR:
	            count = db.delete(HistoryTableMetaData.TABLE_NAME, 
	            		where, whereArgs);
	            break;

	        case INCOMING_SINGLE_HISTORY_URI_INDICATOR:
	            String rowId = uri.getPathSegments().get(1);
	            count = db.delete(HistoryTableMetaData.TABLE_NAME, 
	            		HistoryTableMetaData._ID + "=" + rowId
	                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), 
	                    whereArgs);
	            break;

	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	    }

	    @Override
	    public int update(Uri uri, ContentValues values, 
	    		String where, String[] whereArgs) 
	    {
	        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	        int count;
	        switch (sUriMatcher.match(uri)) {
	        case INCOMING_HISTORY_COLLECTION_URI_INDICATOR:
	            count = db.update(HistoryTableMetaData.TABLE_NAME, 
	            		values, where, whereArgs);
	            break;

	        case INCOMING_SINGLE_HISTORY_URI_INDICATOR:
	            String rowId = uri.getPathSegments().get(1);
	            count = db.update(HistoryTableMetaData.TABLE_NAME, 
	            		values, HistoryTableMetaData._ID + "=" + rowId
	                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), 
	                    whereArgs);
	            break;

	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	    }
	    
	    
}
