package net.mydebug.chessgames.drive.figures;

import java.io.Serializable;
import java.util.List;



public abstract class Figure implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int WHITE = 1;
	public static int BLACK = 0;
	
	protected String image       = null;
	protected String imageActive = null;
	protected int x;
	protected int y;
	protected int color;
	
	public Figure( int color , int x , int y ) {
		this.color = color;
		this.x     = x;
		this.y     = y;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getImage(){
		return this.image;
	}
	
	public String getActiveImage(){
		return this.imageActive;
	}
	
	public int getColor() {
		return color;
	}

	public void setPosition(int x , int y) {
		this.x     = x;
		this.y     = y;
	}
	public Position getPosition() {
		return new Position(x,y);
	}
	public abstract List<Position> getAviableMoves( int[][] figuresOnBoard , List<Figure> figures );
	// -1 - за предлелами поля, 0 - пусто, 1 - занято
	public abstract int checkFieldIsEmpty( Position position , int[][] figuresOnBoard );
}
