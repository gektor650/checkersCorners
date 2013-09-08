package net.mydebug.chessgames.drive.db;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author gektor650
 * Операции с таблицей истории ходов
 */

public class HistoryDb 
{

	protected Context mContext;
	public HistoryDb( Context ctx )
	{
		mContext = ctx;
	}

	/** Добавляем в базу данных ход. 
	 * @param gameId - айдишник игры
	 * @param turnId - айдишник хода
	 * @param data   - байтовое значение сериализированного масива FigureData
	 */
	public void addTurn( int gameId , int turnId , byte[] data , int whosTurn , int gameTime )
	{
		ContentValues cv = new ContentValues();
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_ID, gameId );
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_TURN_ID, turnId );
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_DATA, data );
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN, whosTurn );
		cv.put(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_TIME, gameTime );

		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		cr.insert(uri, cv);
	}
	
	/**
	 * Удаляем все записи из истории
	 */
	public void clearAll() {
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		cr.delete(uri, null, null);		
	}
	
	/**
	 * Удаляем самую последнюю запись в таблице 
	 */
	public void removeLastTurn()
	{
		int i = getCount();
		ContentResolver cr = this.mContext.getContentResolver();
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Uri delUri = Uri.withAppendedPath(uri, Integer.toString(i));
		cr.delete(delUri, null, null);
	}
	
	
	
	/** Достаем из таблицы запись состояния фигур на поле (массив из FigureData)
	 * @param gameId 
	 * @param turnId
	 * @return сериализированный ArrayList<FigureData>
	 */
	public byte[] getTurn( int gameId , int turnId ) {
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = a.getContentResolver().query(uri, null, "turn_id = " + turnId + " and game_id = " + gameId , null, null);
		byte[] dataDb = null;
		int dataId = c.getColumnIndex(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_DATA);
		c.moveToFirst();
		dataDb = c.getBlob(dataId);
		c.close();
		return dataDb;
	}

	
	/**
	 * @param gameId
	 * @return max turnId by gameId
	 */
	public int getLastTurnId( int gameId ) {
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = a.getContentResolver().query(uri, new String[] {"MAX(turn_id) AS maxTurnId"} , "game_id = " + gameId , null, null);
		int dataId = c.getColumnIndex("maxTurnId");
		c.moveToFirst();
		int  dataDb = c.getInt(dataId);
		c.close();
		return dataDb;
	}
	
	public int getLastWhosTurn( int gameId , int turnId ) {
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = a.getContentResolver().query(uri, new String[] {HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN} , "game_id = " + gameId + " and turn_id = " + turnId  , null, null);
		int dataId = c.getColumnIndex(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_WHOS_TOURN);
		c.moveToFirst();
		int  dataDb = c.getInt(dataId);
		c.close();
		return dataDb;
	}

	public int getLastGameTime( int gameId , int turnId ) {
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = a.getContentResolver().query(uri, new String[] { HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_TIME} , "game_id = " + gameId + " and turn_id = " + turnId  , null, null);
		int gameTime = c.getColumnIndex(HistoryProviderMetaData.HistoryTableMetaData.HISTORY_GAME_TIME );
		c.moveToFirst();
        gameTime = c.getInt(gameTime);
		c.close();
		return gameTime;
	}

	/**
	 * @return общее количество строк в таблице
	 */
	public int getCount()
	{
		Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
		Activity a = (Activity)this.mContext;
		Cursor c = a.getContentResolver().query(uri, null, null, null, null);
		int numberOfRecords = c.getCount();
		c.close();
		return numberOfRecords;
	}


}
