package net.mydebug.corners;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import com.badlogic.androidgames.framework.Pixmap;
import net.mydebug.corners.drive.db.HistoryDb;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Input.KeyEvent;


public class MainMenuScreen extends Screen{
    private Graphics  g = game.getGraphics();
    private int  height = g.getHeight();
    private int   width = g.getWidth();
    private int padding = 0;
    private boolean changes     = true;
    private boolean onlyNewGame = true;
    private HistoryDb historyDb;
    private int menuSize = 5;
	
	public MainMenuScreen(Game game) {
		super(game);
		historyDb = new HistoryDb( game.getActivity() );
		if( historyDb.getCount() > 1 ) {
			onlyNewGame = false;
            menuSize++;
		}
        int paddingUp = (int)( height * 0.2 ) ;
        padding  = ( height - paddingUp )  / menuSize ;
	}
	
	@Override
	public void update( float time ) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent>     keyEvents = game.getInput().getKeyEvents();   
        int len = touchEvents.size();

        
        for(int j = 0; j < len; j++) {
            TouchEvent event = touchEvents.get(j);
            if( event.type != TouchEvent.TOUCH_UP ) continue;
    		for(int i = 0 ; i < menuSize ; i++ ) {
    			if( event.y < padding*(i+1)+30 && event.y > padding*(i+1) ) {
    	            if( i == 0 ) {
    	            	game.setScreen( new СheckersCornersScreen( game , true ) );
    	            } else {
                        if( onlyNewGame ) {
    	            		switch( i ) {
    	    	            	case 1 : game.setScreen( new SettingsScreen( game ) );break;
                                case 2 : game.setScreen( new StatisticsScreen( game ) );break;
                                case 3 :
                                    String appName = "net.mydebug.corners";
                                    try {
                                        game.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        game.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
                                    }
                                    break;
    	    	            	case 4 : System.exit(0);break;
    	            		}
    	            	} else {
    	            		switch( i ) {
		    	            	case 1 : game.setScreen( new СheckersCornersScreen( game , false ) );break;
		    	            	case 2 : game.setScreen( new SettingsScreen( game ) );break;
		    	            	case 3 : game.setScreen( new StatisticsScreen( game ) );break;
		    	            	case 4 :
                                    String appName = "net.mydebug.corners";
                                    try {
                                        game.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        game.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
                                    }
                                    break;
		    	            	case 5 : System.exit(0);break;
		            		}
    	            	}
	
    	            }
    			}
    		}
        }
        len = keyEvents.size();
		for( int j = 0 ; j < len ; j ++ ) {
			if( keyEvents.get(j).keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
				System.exit(0);
			}
		}
	}
	public void drawMenu() {
		g.clear(0xff964009);
        Pixmap image = game.getGraphics().newPixmap("old_wood_and_paper_vector_1.jpg", Graphics.PixmapFormat.ARGB8888 );
        int i =1 ;

        game.getGraphics().drawPixmap( image , 0, 0, g.getWidth(), g.getHeight() );

        image = game.getGraphics().newPixmap("newGame.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image , game.getGraphics().getWidth() / 2 - 90 , padding  );
        if( ! onlyNewGame ) {
            image = game.getGraphics().newPixmap("resumeGame.png", Graphics.PixmapFormat.ARGB8888 );
            game.getGraphics().drawPixmap( image , game.getGraphics().getWidth() / 2 - 90 , padding * ++i );
        }

        image = game.getGraphics().newPixmap("settings.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image , game.getGraphics().getWidth() / 2 - 90 , padding * ++i );

        image = game.getGraphics().newPixmap("statistics.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image , game.getGraphics().getWidth() / 2 - 90 , padding * ++i );

        image = game.getGraphics().newPixmap("leaveReview.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image , game.getGraphics().getWidth() / 2 - 90 , padding * ++i );

        image = game.getGraphics().newPixmap("exit.png", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap( image , game.getGraphics().getWidth() / 2 - 90 , padding * ++i );

		this.changes = false;
	}
	
	@Override
	public void present( float time ) {
		if( this.changes ) 
			this.drawMenu();
	}

	@Override
	public void pause() {
		
	}
	@Override
	public void dispose() {
		
	}
	
	@Override
	public void resume() {
		
	}
}
