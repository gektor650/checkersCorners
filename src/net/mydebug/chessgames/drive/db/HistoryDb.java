package net.mydebug.chessgames.drive.db;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class HistoryDb 
{

	protected Context mContext;
	public HistoryDb( Context ctx )
	{
		mContext = ctx;
	}
	private static String tag = "Provider Tester";

	public void addTurn( int gameId , int turnId , byte[] data)
	{
		Log.d(tag,"Adding a book");
		ContentValues cv = new ContentValues();
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_ID, gameId );
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_TURN_ID, turnId );
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_DATA, data );
		
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		cr.insert(uri, cv);

	}
	public void removeLastTurn()
	{
		int i = getCount();
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(i));
		reportString("Del Uri:" + delUri);
		cr.delete(delUri, null, null);
		this.reportString("Newcount:" + getCount());
	}
	
	
	
	public byte[] getTurn( int gameId , int turnId ) {
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = a.getContentResolver().query(uri, null, "turn_id = " + turnId + " and game_id = " + gameId , null, null);
		byte[] dataDb = null;
		int dataId = c.getColumnIndex(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_DATA);
		
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			dataDb = c.getBlob(dataId);
		}
		c.close();
		return dataDb;
	}

	private int getCount()
	{
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = a.getContentResolver().query(uri, null, null, null, null);
		int numberOfRecords = c.getCount();
		c.close();
		return numberOfRecords;
	}

	private void reportString(String s)
	{
		Log.d(tag,s);
	}

}
