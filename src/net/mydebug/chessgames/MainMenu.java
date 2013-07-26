package net.mydebug.chessgames;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.androidgames.framework.Pixmap;
import net.mydebug.chessgames.drive.db.HistoryDb;

import android.app.Activity;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.impl.AndroidGame;


public class MainMenu extends Screen{
	
	public class ImpossibleActions {
		public String text;
		public int x = 0;
		public int y = 0;
		public ImpossibleActions( String text ) {
			this.text = text;
		}
	}
	
	List<ImpossibleActions> menus = new ArrayList<ImpossibleActions>();
	Graphics  g = game.getGraphics();
	int  height = g.getHeight();
	int   width = g.getWidth();
	int       x = width/2;
	int padding = 0;
	boolean changes = true;
    boolean onlyNewGame = true;
	HistoryDb historyDb;
	
	public MainMenu ( Game game ) {
		super(game);
		historyDb = new HistoryDb( game.getActivity() );
		menus.add( new ImpossibleActions("New game" )) ;
		if( historyDb.getCount() > 1 ) {
			menus.add( new ImpossibleActions("Resume last game" )) ;	
			onlyNewGame = false;
		}
		menus.add( new ImpossibleActions("Prefences")) ;
		menus.add( new ImpossibleActions("Statistics")) ;
		menus.add( new ImpossibleActions(   "Exit"   )) ;
        int paddingUp = (int)( height * 0.2 ) ;
        padding  = ( height - paddingUp )  / menus.size() ;
	}
	
	@Override
	public void update( float time ) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent>     keyEvents = game.getInput().getKeyEvents();   
        int len = touchEvents.size();

        
        for(int j = 0; j < len; j++) {
            TouchEvent event = touchEvents.get(j);
            if( event.type != TouchEvent.TOUCH_UP ) continue;
    		for(int i = 0 ; i < menus.size() ; i++ ) {
    			if( event.y < menus.get(i).y+15 && event.y > menus.get(i).y-15 ) {
    	            if( i == 0 ) {
    	            	game.setScreen( new СheckersCornersActivity( game , true ) );	            	
    	            } else {
    	            	if( onlyNewGame ) {
    	            		switch( i ) {
    	    	            	case 1 : break;
    	    	            	case 2 : break;
    	    	            	case 3 : System.exit(0);break;
    	            		}
    	            	} else {
    	            		switch( i ) {
		    	            	case 1 : game.setScreen( new СheckersCornersActivity( game , false ) );break;
		    	            	case 2 : break;
		    	            	case 3 : break;
		    	            	case 4 : System.exit(0);break;
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
        Pixmap chessBoardImage = game.getGraphics().newPixmap("old_wood_and_paper_vector_1.jpg", Graphics.PixmapFormat.ARGB8888 );

        game.getGraphics().drawPixmap(chessBoardImage, 0, 0, g.getWidth(), g.getHeight() );
		for(int i = 0 ; i < menus.size() ; i++ ) {
			menus.get(i).y = padding * ( i + 1 ) ;
			menus.get(i).x = x;
			g.drawText( menus.get(i).text , menus.get(i).x , menus.get(i).y , 20 , 0xff000000);
		}
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
