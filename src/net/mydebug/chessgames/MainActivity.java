package net.mydebug.chessgames;


import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class MainActivity extends AndroidGame {
	
    @Override
    public Screen getStartScreen() {  
        return new MainMenuActivity( this );
    }

}