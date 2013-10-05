package net.mydebug.corners.drive;

import android.app.Activity;
import net.mydebug.corners.drive.db.StatisticDb;

import java.util.List;


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
