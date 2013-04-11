package net.mydebug.chessgames;

import android.graphics.Paint;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

public class LoadingScreen extends Screen {
	Pixmap awesomePic;
	Graphics g;
	int x;
	Paint p = new Paint();
	
    public LoadingScreen(Game game ) {
        super(game);
        awesomePic = game.getGraphics().newPixmap("ic_launcher.png", PixmapFormat.RGB565 );
        g = game.getGraphics();
    }

    @Override
    public void update(float deltaTime) {
        x += (int) 2;
        if( x > g.getWidth() ) 
        	x = 0;       
    }

    @Override
    public void present(float deltaTime) {
    	game.getGraphics().clear(0);
    	int color = 0xccddff00;
    	game.getGraphics().drawRect(0 , 0, x, x, color);
    	color = 0xccddff00;
    	game.getGraphics().drawRect(0 , 0, x, x, color);

    	game.getGraphics().drawPixmap(awesomePic, x, 0, 0, 0, awesomePic.getWidth(), awesomePic.getHeight() );
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
    
    public Game getGame(){
    	return game;
    }
   
}