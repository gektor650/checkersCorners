package net.mydebug.chessgames.drive.figures;

public interface Ai {
	public void move();
	
	class FigureAndPosition {
		public Position position;
		public int figureIndex;
	}
}
