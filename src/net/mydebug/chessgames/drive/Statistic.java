package net.mydebug.chessgames.drive;

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
    Game game ;
    StatisticDb statisticDb;

    public Statistic( Game game ) {
        this.game = game;
        statisticDb = new StatisticDb( game.getActivity() );
    }

    public List<StatisticRow> getStatistics( int limit ) {
        return statisticDb.getStatistics( limit );
    }

}
