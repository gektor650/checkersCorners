package net.mydebug.chessgames.drive.db;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import net.mydebug.chessgames.drive.StatisticRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 31.07.13
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class StatisticDb {
    protected Context mContext;
    public StatisticDb( Context ctx )
    {
        mContext = ctx;
    }

    public static final String STATISTIC_TURNS_CNT     = "turns_cnt";
    public static final String STATISTIC_GAME_TIME     = "game_time";
    public static final String STATISTIC_GAME_LEVEL    = "game_level";
    public static final String STATISTIC_FIGURES_COLOR = "figures_color";
    public static final String CREATED_DATE            = "created";

    public void addRow( int turns_cnt , int game_time , int game_level , int figures_color )
    {
        ContentValues cv = new ContentValues();
        cv.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_TURNS_CNT, turns_cnt );
        cv.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_TIME, game_time );
        cv.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_LEVEL, game_level );
        cv.put(StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_FIGURES_COLOR , figures_color );

        ContentResolver cr = this.mContext.getContentResolver();
        Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
        cr.insert(uri, cv);
    }

    public List<StatisticRow> getStatistics( int limit ) {
        if(true) return null;
        List<StatisticRow> rows = new ArrayList<StatisticRow>();
        Uri uri = StatisticProviderMetaData.StatisticTableMetaData.CONTENT_URI;
        Activity a = (Activity)this.mContext;
        Cursor c = a.getContentResolver().query(uri, null, null, null, null);
        int turnsCnt    = c.getColumnIndex( StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_TURNS_CNT );
        int gameTime    = c.getColumnIndex( StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_TIME );
        int gameLevel   = c.getColumnIndex( StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_GAME_LEVEL );
        int figureColor = c.getColumnIndex( StatisticProviderMetaData.StatisticTableMetaData.STATISTIC_FIGURES_COLOR );
        c.moveToFirst();
        while( ! c.isLast() && rows.size() < limit ) {
            StatisticRow row = new StatisticRow();
            row.turnsCnt    = c.getInt( turnsCnt );
            row.gameLevel   = c.getInt( gameLevel );
            row.gameTime    = c.getInt( gameTime );
            row.figureColor = c.getInt( figureColor );
            rows.add( row );
            c.moveToNext();
        }
        c.close();

        return rows;
    }

    /**
     * Удаляем все записи из истории
     */
    public void clearAll() {
        ContentResolver cr = this.mContext.getContentResolver();
        Uri uri = HistoryProviderMetaData.HistoryTableMetaData.CONTENT_URI;
        cr.delete(uri, null, null);
    }

}
