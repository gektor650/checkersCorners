package net.mydebug.chessgames;

import android.graphics.Paint;
import com.badlogic.androidgames.framework.*;
import net.mydebug.chessgames.drive.Statistic;
import net.mydebug.chessgames.drive.StatisticRow;
import net.mydebug.chessgames.drive.figures.CheckerCorners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 08.09.13
 * Time: 23:56
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsScreen extends Screen {

    protected int rowsCnt = 10;
    protected Statistic statistic;
    protected List<StatisticRow> statisticsData = new ArrayList<StatisticRow>();

    public StatisticsScreen(Game game) {
        super(game);
        Pixmap background = game.getGraphics().newPixmap("old_wood_and_paper_vector_1.jpg", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight() );
        game.getGraphics().drawText("Back", game.getGraphics().getWidth() - 60, game.getGraphics().getHeight() - 40, 20, 0xff000000);
        statistic      = new Statistic( game.getActivity() );
        statisticsData = statistic.getStatistics( rowsCnt );
        int statisticTableWidth = ( game.getGraphics().getWidth() - game.getGraphics().getWidth() / 6 ) / 4;
        int topPadding = game.getGraphics().getHeight() / 6 ;
        game.getGraphics().drawText( "Ходов" , statisticTableWidth , topPadding , 14 , 0xff000000, Paint.Align.CENTER );
        game.getGraphics().drawText( "Время" , statisticTableWidth * 2 , topPadding , 14 , 0xff000000 , Paint.Align.CENTER );
        game.getGraphics().drawText( "Уровень" , statisticTableWidth * 3  , topPadding , 14 , 0xff000000 , Paint.Align.CENTER );
        game.getGraphics().drawText( "Цвет" , statisticTableWidth * 4 , topPadding , 14 , 0xff000000 , Paint.Align.CENTER );
        int horizontLineX = statisticTableWidth - statisticTableWidth / 2 + 5 ;
        int horizontLineY = statisticTableWidth * 4 + statisticTableWidth / 2 - 5 ;

        game.getGraphics().drawLine(
                horizontLineX  ,
                topPadding + 5 ,
                horizontLineY  ,
                topPadding + 5 ,
                0xff000000 , 2 );
        for( int i = 1 ; i < 4 ; i++ )  {
            game.getGraphics().drawLine(
                    i * statisticTableWidth + statisticTableWidth / 2,
                    topPadding - 20 ,
                    i * statisticTableWidth + statisticTableWidth / 2,
                    topPadding * 5 + 20 ,
                    0xff000000 );
        }
        CheckerCorners checker = new CheckerCorners();
        topPadding = topPadding + 30;
        int innerPadding = topPadding * 3 / rowsCnt ;
        String turnsCnt;
        String gameTime;
        String gameLevel;
        String figureImage;
        Pixmap delimeterImage = game.getGraphics().newPixmap("dash.jpg", Graphics.PixmapFormat.ARGB8888 );
        Pixmap checkerImage ;
        for ( int i = 0 ; i < rowsCnt ; i++ ) {
            if( i < statisticsData.size() ) {
                turnsCnt    = String.valueOf( statisticsData.get(i).turnsCnt );
                gameTime    = String.valueOf( statisticsData.get(i).gameTime );
                gameLevel   = String.valueOf( statisticsData.get(i).gameLevel );
                checker.setColor( statisticsData.get(i).figureColor );
                checkerImage = game.getGraphics().newPixmap( checker.getImage() , Graphics.PixmapFormat.ARGB8888 ); checker.getImage();

                game.getGraphics().drawText( turnsCnt , statisticTableWidth , topPadding + ( i * innerPadding ) , 14 , 0xff000000, Paint.Align.CENTER );
                game.getGraphics().drawText( gameTime , statisticTableWidth * 2 , topPadding + ( i * innerPadding ) , 14 , 0xff000000 , Paint.Align.CENTER );
                game.getGraphics().drawText( gameLevel , statisticTableWidth * 3  , topPadding + ( i * innerPadding ) , 14 , 0xff000000 , Paint.Align.CENTER );
                game.getGraphics().drawPixmap(
                        checkerImage ,
                        statisticTableWidth * 4 - innerPadding / 4,
                        topPadding + ( i * innerPadding ) - innerPadding / 2 ,
                        innerPadding / 2 ,
                        innerPadding / 2 );
            } else {
                game.getGraphics().drawPixmap(delimeterImage, statisticTableWidth - 7, topPadding + (i * innerPadding) - 10);
                game.getGraphics().drawPixmap(delimeterImage, statisticTableWidth * 2 - 7, topPadding + (i * innerPadding) - 10);
                game.getGraphics().drawPixmap(delimeterImage, statisticTableWidth * 3 - 7, topPadding + (i * innerPadding) - 10);
                game.getGraphics().drawPixmap(delimeterImage, statisticTableWidth * 4 - 7, topPadding + (i * innerPadding) - 10);
            }

            game.getGraphics().drawLine(
                    horizontLineX ,
                    topPadding + ( i * innerPadding ) + 5 ,
                    horizontLineY ,
                    topPadding + ( i * innerPadding ) + 5 ,
                    0xff000000 );

        }

    }

    @Override
    public void update(float deltaTime) {
        List<Input.KeyEvent> keyEvents     = game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        for( int j = 0 ; j < keyEvents.size() ; j ++ ) {
            if( keyEvents.get(j).keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
                game.setScreen(new MainMenuScreen(game));
            }
        }
        for( int i = 0 ; i < touchEvents.size() ; i++ ) {
            Input.TouchEvent event = touchEvents.get( i );
            if( event.type == Input.TouchEvent.TOUCH_UP ) {
                if( event.y > game.getGraphics().getHeight() - 60 ) {
                    // Если нажали в нижний правый угол (иконка выйти в меню)
                    if( event.x  > game.getGraphics().getWidth() - 80 ) {
                        game.setScreen( new MainMenuScreen( game ) );
                    }
                }
            }
        }

    }

    @Override
    public void present(float deltaTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
