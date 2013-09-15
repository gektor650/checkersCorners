package net.mydebug.chessgames.drive.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 31.07.13
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class StatisticProviderMetaData {
    public static final String AUTHORITY = "net.mydebug.chessgames.drive.db.StatisticProvider";

    public static final String DATABASE_NAME = "statistic.db";
    public static final int DATABASE_VERSION = 2;

    private StatisticProviderMetaData() {}
    //inner class describing columns and their types
    public static final class StatisticTableMetaData implements BaseColumns
    {
        private StatisticTableMetaData() {}
        public static final String TABLE_NAME = "statistic";

        //uri and mime type definitions
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/statistic");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mydebug.statistic";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mydebug.statistic";

        public static final String DEFAULT_SORT_ORDER = "turns_cnt ASC, game_time ASC";

        //Additional Columns start here.
        //string type
        public static final String STATISTIC_TURNS_CNT     = "turns_cnt";
        //string type
        public static final String STATISTIC_GAME_TIME     = "game_time";
        //string type
        public static final String STATISTIC_GAME_LEVEL    = "game_level";

        public static final String STATISTIC_FIGURES_COLOR = "figures_color";

        public static final String CREATED_DATE            = "created";

    }
}
