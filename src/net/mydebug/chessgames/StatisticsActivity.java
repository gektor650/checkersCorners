package net.mydebug.chessgames;

import com.badlogic.androidgames.framework.*;
import net.mydebug.chessgames.drive.Statistic;
import net.mydebug.chessgames.drive.StatisticRow;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 08.09.13
 * Time: 23:56
 * To change this template use File | Settings | File Templates.
 */
public class StatisticsActivity extends Screen {

    protected Statistic statistic;
    protected List<StatisticRow> statisticsData = null;

    public StatisticsActivity(Game game) {
        super(game);
        Pixmap background = game.getGraphics().newPixmap("old_wood_and_paper_vector_1.jpg", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( background, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight() );
        game.getGraphics().drawText("Back" ,  game.getGraphics().getWidth() - 60 , game.getGraphics().getHeight() - 40 , 20 , 0xff000000 );
        statistic      = new Statistic( game );
        statisticsData = statistic.getStatistics( 5 );

    }

    @Override
    public void update(float deltaTime) {
        List<Input.KeyEvent> keyEvents     = game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        for( int j = 0 ; j < keyEvents.size() ; j ++ ) {
            if( keyEvents.get(j).keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
                game.setScreen(new MainMenuActivity(game));
            }
        }
        for( int i = 0 ; i < touchEvents.size() ; i++ ) {
            Input.TouchEvent event = touchEvents.get( i );
            if( event.type == Input.TouchEvent.TOUCH_UP ) {
                if( event.y > game.getGraphics().getHeight() - 60 ) {
                    // Если нажали в нижний правый угол (иконка выйти в меню)
                    if( event.x  > game.getGraphics().getWidth() - 40 ) {
                        game.setScreen( new MainMenuActivity( game ) );
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
