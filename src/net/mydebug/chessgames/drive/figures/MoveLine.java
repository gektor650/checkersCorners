package net.mydebug.chessgames.drive.figures;

public class MoveLine {
	
	public Position position1;
	public Position position2;
	
	public MoveLine( int x1 , int y1 , int x2 , int y2 ) {
		position1 = new Position(x1,y1);
		position2 = new Position(x2,y2);
	}
	
}
