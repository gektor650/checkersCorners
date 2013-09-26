package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;

public interface Ai {
	public void move();
	
	class FigureAndPosition {

		public Position position = new Position();
		public int figureIndex   = 0;
        public int weight        = -99;

	}
}
