package net.mydebug.chessgames.drive;

import android.app.Activity;
import com.badlogic.androidgames.framework.Game;
import net.mydebug.chessgames.drive.db.StatisticDb;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 09.09.13
 * Time: 0:42
 * To change this template use File | Settings | File Templates.
 */
public class Statistic {
    StatisticDb statisticDb;

    public Statistic( Activity activity ) {
        statisticDb = new StatisticDb( activity );
    }

    public List<StatisticRow> getStatistics( int limit ) {
        return statisticDb.getList( limit );
    }

    public void add( int turnsCnt , int gameTime , int gameLevel , int figureColor ) {
        statisticDb.add( turnsCnt , gameTime , gameLevel , figureColor );
    }

}
