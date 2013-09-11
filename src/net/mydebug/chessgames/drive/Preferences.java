package net.mydebug.chessgames.drive;

import net.mydebug.chessgames.drive.figures.Figure;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: gektor650
 * Date: 12.09.13
 * Time: 1:03
 * To change this template use File | Settings | File Templates.
 */
public class Preferences {

    public class PreferencesRow implements Serializable {
        public int     gameMode;
        public int     gameLevel;
        public boolean showTips;
        public int     playerColor;
    }

    String path;

    public Preferences( String path ) {
        this.path = path + "/";
        preferences = new PreferencesRow();
        load();
    }

    private PreferencesRow preferences ;
    private String settingsFile = ".cornersSettings";

    public void save() {
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream( path + this.settingsFile );
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(preferences);
            out.close();
            fileOut.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    public void load() {
        try
        {
            FileInputStream fileIn = new FileInputStream( path + this.settingsFile );
            ObjectInputStream in = new ObjectInputStream(fileIn);
            preferences = ( PreferencesRow ) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Load");
        }catch(IOException i)
        {
            System.out.println("Create");
            preferences.gameLevel   = 0;
            preferences.gameMode    = ChessBoard.ONE_PLAYER;
            preferences.playerColor = Figure.WHITE;
            preferences.showTips    = true;

            save();
        }catch(ClassNotFoundException c)
        {
            c.printStackTrace();
            return;
        }
    }

}
