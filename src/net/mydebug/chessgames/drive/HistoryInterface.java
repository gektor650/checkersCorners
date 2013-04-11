package net.mydebug.chessgames.drive;

import java.util.List;

import net.mydebug.chessgames.drive.figures.Figure;

public interface HistoryInterface {
	public void add( List<Figure> figures );
	public void save();
	public List<Figure> load();
	public List<Figure> back();
}
