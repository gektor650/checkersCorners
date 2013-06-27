package net.mydebug.chessgames;


import android.app.Activity;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class MainActivity extends AndroidGame {
	
    @Override
    public Screen getStartScreen() {  
        return new MainMenu( this );
    }

}