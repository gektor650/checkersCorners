package net.mydebug.corners.drive.figures;

public interface Ai {
	public void move();

    public void clearMoveHistory();

    public void resetAi();

    class FigureAndPosition {

		public Position position = new Position();
		public int figureIndex   = 0;
        public float weight      = -99;

	}
}
