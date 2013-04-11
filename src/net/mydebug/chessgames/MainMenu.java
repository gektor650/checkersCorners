package net.mydebug.chessgames;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Input.KeyEvent;


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
	
	public MainMenu ( Game game ) {
		super(game);
		menus.add( new ImpossibleActions("Chesses corners game" )) ;
		menus.add( new ImpossibleActions("Classic chess"   )) ;
		menus.add( new ImpossibleActions("Chess campaign"   )) ;
		menus.add( new ImpossibleActions("Exit"   )) ;
		padding  = ( height - 20 * menus.size() ) / menus.size() ;
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
    			if( event.y < menus.get(i).y+10 && event.y > menus.get(i).y-10 ) {
    	            switch(i) {
    	            	case 0 : game.setScreen( new СheckersCornersActivity( game ) );break;
    	            	case 3 : System.exit(0);break;
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
		for(int i = 0 ; i < menus.size() ; i++ ) {
			menus.get(i).y = padding * ( i + 1 ) ;
			menus.get(i).x = x;
			g.drawText( menus.get(i).text , menus.get(i).x , menus.get(i).y , 20 , 0xffffffff);	
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
