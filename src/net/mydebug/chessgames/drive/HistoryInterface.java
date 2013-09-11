package net.mydebug.chessgames.drive;

import java.util.ArrayList;

import net.mydebug.chessgames.drive.figures.FigureData;



public interface HistoryInterface {
	public void save(ChessBoard board);
	public int getTurnId();
	public ArrayList<FigureData> back();
	public ArrayList<FigureData> loadLastTurn();
	public int lastWhosTurn();
	public int lastGameTime();
    public void clear();
}
