package net.mydebug.corners.drive;

import net.mydebug.corners.drive.figures.Figure;

import java.io.*;

public class Settings {


    String path;

    public Settings( String path ) {
        this.path = path + "/";
        settings = new SettingsRow();
        load();
    }

    private SettingsRow settings ;
    private String settingsFile = ".cornersSettings";

    public void save() {
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream( path + this.settingsFile );
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(settings);
            out.close();
            fileOut.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    public void load() {
        settings.gameLevel   = 1;
        settings.gameMode    = ChessGame.ONE_PLAYER;
        settings.playerColor = Figure.WHITE;
        settings.showTips    = true;
        try
        {
            FileInputStream fileIn = new FileInputStream( path + this.settingsFile );
            ObjectInputStream in = new ObjectInputStream(fileIn);
            settings = (SettingsRow) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            save();
        }catch(ClassNotFoundException c)
        {
            c.printStackTrace();
        }
    }

    public int getGameMode() {
        return settings.gameMode;
    }

    public int getGameLevel() {
        return settings.gameLevel;
    }

    public int getPlayerColor() {
        return settings.playerColor;
    }

    public boolean getShowTips() {
        return settings.showTips;
    }

    public void changeGameMode() {
        if( settings.gameMode == ChessGame.ONE_PLAYER ) {
            settings.gameMode = ChessGame.TWO_PLAYERS;
        } else {
            settings.gameMode = ChessGame.ONE_PLAYER;
        }
    }

    public void changeGameLevel() {
        settings.gameLevel++;
        if( settings.gameLevel == 3 ) {
            settings.gameLevel = 0;
        }
    }

    public void changePlayerColor() {
        if( settings.playerColor == Figure.WHITE ) {
            settings.playerColor = Figure.BLACK;
        } else {
            settings.playerColor = Figure.WHITE;
        }
    }

    public void changeShowTips() {
        this.settings.showTips = ! this.settings.showTips;
    }

}
