package net.mydebug.chessgames.drive;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 12.09.13
 * Time: 1:52
 * To change this template use File | Settings | File Templates.
 */
public class PreferencesRow implements Serializable {
    public int     gameMode;
    public int     gameLevel;
    public boolean showTips;
    public int     playerColor;
}