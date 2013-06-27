package net.mydebug.chessgames.drive.figures;

import java.io.Serializable;



public class Position   implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x = 0;
	public int y = 0;
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
