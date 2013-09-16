package net.mydebug.chessgames;

import com.badlogic.androidgames.framework.*;
import net.mydebug.chessgames.drive.ChessBoard;
import net.mydebug.chessgames.drive.History;
import net.mydebug.chessgames.drive.Settings;
import net.mydebug.chessgames.drive.figures.Figure;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 12.09.13
 * Time: 0:53
 * To change this template use File | Settings | File Templates.
 */
public class SettingsScreen extends Screen {

    private Settings settings;
    private Game game;
    private int paddingY;

    public SettingsScreen(Game game) {
        super(game);
        this.game = game;
        settings = new Settings( game.getActivity().getBaseContext().getFilesDir().toString() );
        settings.load();
        this.drawSettings();

    }

    private void drawSettings() {
        Pixmap image = game.getGraphics().newPixmap("old_wood_and_paper_vector_1.jpg", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image, 0, 0 , game.getGraphics().getWidth(), game.getGraphics().getHeight() );
        game.getGraphics().drawText("Back", game.getGraphics().getWidth() - 60, game.getGraphics().getHeight() - 40, 20, 0xff000000);

        int paddingX = game.getGraphics().getWidth() / 2 - 70;
        paddingY = ( game.getGraphics().getHeight() - 40 ) / 4  ;

        image = game.getGraphics().newPixmap("gameMode.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image, paddingX, 40 );

        if( settings.getSettings().gameMode == ChessBoard.ONE_PLAYER ) {
            image = game.getGraphics().newPixmap("1player.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, paddingX, 60 );
        } else {
            image = game.getGraphics().newPixmap("2players.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, paddingX, 60 );
        }

        if( settings.getSettings().showTips == true ) {
            image = game.getGraphics().newPixmap("showTipsYes.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, paddingX, 40 + paddingY );
        } else {
            image = game.getGraphics().newPixmap("showTipsNo.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, paddingX, 40 + paddingY );
        }

        image = game.getGraphics().newPixmap("playerColor.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image, paddingX, 40 + paddingY * 2 );
        if( settings.getSettings().playerColor == Figure.WHITE ) {
            image = game.getGraphics().newPixmap( "checkerWhite1.png" , Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, game.getGraphics().getWidth() / 2 - 10 , 80 + paddingY * 2 , 20 , 20 );
        } else {
            image = game.getGraphics().newPixmap("checkerBlack1.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, game.getGraphics().getWidth() / 2 - 10 , 80 + paddingY * 2 , 20 , 20  );
        }

        image = game.getGraphics().newPixmap("gameLevel.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image, paddingX, 40 + paddingY * 3 );
        if( settings.getSettings().gameLevel == 0 ) {
            image = game.getGraphics().newPixmap("easy.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, paddingX, 60 + paddingY * 3 );
        } else if( settings.getSettings().gameLevel == 1 ) {
            image = game.getGraphics().newPixmap("medium.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, paddingX, 60 + paddingY * 3 );
        } else  if( settings.getSettings().gameLevel == 2 ) {
            image = game.getGraphics().newPixmap("hard.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image, paddingX, 60 + paddingY * 3 );
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
                if( event.y > 60 &&  event.y  < 100 ) {
                    settings.changeGameMode();
                }
                if( event.y > 100 &&  event.y  < 100 + paddingY ) {
                    settings.changeShowTips();
                }
                if( event.y >  100 + paddingY  &&  event.y  < 100 + paddingY *2 ) {
                    settings.changePlayerColor();
                }
                if( event.y >  100 + paddingY * 2  &&  event.y  < 100 + paddingY * 3 ) {
                    settings.changeGameLevel();
                }
                drawSettings();
                settings.save();
                History history   = new History( game.getActivity() , false );
                history.clear();


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
