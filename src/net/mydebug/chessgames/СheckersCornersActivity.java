package net.mydebug.chessgames;

import java.util.List;

import net.mydebug.chessgames.drive.CheckersCornersGame;
import net.mydebug.chessgames.drive.ChessBoard;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Input.KeyEvent;

public class СheckersCornersActivity extends Screen {
	CheckersCornersGame cornersGame ;
	public СheckersCornersActivity( Game game  , boolean newGame ) {
		super( game );
		game.getGraphics().clear(0xff964009);
		cornersGame = new CheckersCornersGame( game , newGame );
		cornersGame.draw();
	}


	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent>     keyEvents = game.getInput().getKeyEvents();  
		int len;
		len = keyEvents.size();
		for( int k = 0 ; k < len ; k ++ ) {
			if( keyEvents.get(k).keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
				game.setScreen( new MainMenu( game ) );
			}
		}
		len = touchEvents.size();
		for( int i = 0 ; i < len ; i++ ) {
			TouchEvent event = touchEvents.get( i );
			if( event.type == TouchEvent.TOUCH_UP ) {
				cornersGame.touch( event.x , event.y );
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
