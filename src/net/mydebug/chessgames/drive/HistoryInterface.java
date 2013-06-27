package net.mydebug.chessgames.drive;

import java.util.ArrayList;

import net.mydebug.chessgames.drive.figures.FigureData;



public interface HistoryInterface {
	public void save(ChessBoard board);
	public int getTurn();
	public ArrayList<FigureData> back(ChessBoard board);
}
