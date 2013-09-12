package net.mydebug.chessgames;


import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class Main extends AndroidGame {
	
    @Override
    public Screen getStartScreen() {  
        return new MainMenuScreen( this );
    }

}