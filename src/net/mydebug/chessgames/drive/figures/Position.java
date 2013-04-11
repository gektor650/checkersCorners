package net.mydebug.chessgames.drive.figures;

public class Position {
	public int x;
	public int y;
	public Position( int x , int y ) {
		setPosition(x,y);
	}
	public Position(  ) {
	}
	
	public void setPosition( int x , int y) {
		this.x = x;
		this.y = y;
	}

}
