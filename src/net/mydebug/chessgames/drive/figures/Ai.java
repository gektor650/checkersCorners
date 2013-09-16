package net.mydebug.chessgames.drive.figures;

public interface Ai {
	public void move();
	
	class FigureAndPosition {
        public FigureAndPosition() {}

        public FigureAndPosition( Position position , int figureIndex , int weight ) {

        }
		public Position position;
		public int figureIndex;
        public int weight;
	}
}
