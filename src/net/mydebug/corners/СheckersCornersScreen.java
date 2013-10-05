package net.mydebug.corners;

import java.util.List;

import net.mydebug.corners.drive.CheckersCornersGame;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Input.KeyEvent;

public class СheckersCornersScreen extends Screen {
	CheckersCornersGame cornersGame ;
    float updateTime = 0;
	public СheckersCornersScreen(Game game, boolean newGame) {
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
        updateTime += deltaTime;
        if( updateTime >= 1 ) {
            cornersGame.drawGameTime();
            updateTime = 0;
        }
        for( int k = 0 ; k < len ; k ++ ) {
			if( keyEvents.get(k).keyCode == android.view.KeyEvent.KEYCODE_BACK ) {
                cornersGame.clearTimer();
				game.setScreen( new MainMenuScreen( game ) );
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

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
