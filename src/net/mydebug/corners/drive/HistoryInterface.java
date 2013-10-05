package net.mydebug.corners.drive;

import java.util.ArrayList;


public interface HistoryInterface {
	public void save(ChessGame board);
	public int getTurnId();
	public ArrayList back();
	public ArrayList loadLastTurn();
	public int lastWhosTurn();
	public int lastGameTime();
    public void clear();
}
