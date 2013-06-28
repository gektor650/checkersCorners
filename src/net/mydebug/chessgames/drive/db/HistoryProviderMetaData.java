package net.mydebug.chessgames.drive.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class HistoryProviderMetaData {
	  public static final String AUTHORITY = "net.mydebug.chessgames.drive.db.HistoryProvider";
	    
	    public static final String DATABASE_NAME = "history.db";
	    public static final int DATABASE_VERSION = 4;
	    public static final String BOOKS_TABLE_NAME = "history";
	    
	    private HistoryProviderMetaData() {}
	    //inner class describing columns and their types
	    public static final class HistoryTableMetaData implements BaseColumns 
	    {
	        private HistoryTableMetaData() {}
	        public static final String TABLE_NAME = "history";

	        //uri and mime type definitions
	        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/history");
	        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mydebug.history";
	        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mydebug.history";

	        public static final String DEFAULT_SORT_ORDER = "created DESC";
	        
	        //Additional Columns start here.
	        //string type
	        public static final String HISTORY_GAME_ID  = "game_id";
	        //string type
	        public static final String HISTORY_TURN_ID   = "turn_id";
	        //string type
	        public static final String HISTORY_GAME_DATA = "data";
	        
	        public static final String HISTORY_GAME_WHOS_TOURN = "whos_turn";
	        //Integer from System.currentTimeMillis()
	        public static final String CREATED_DATE      = "created";

	    }
}
