package net.mydebug.chessgames.drive.figures;

import java.util.List;

import net.mydebug.chessgames.drive.ChessBoard;
import net.mydebug.chessgames.drive.Serialize;


public abstract class Figure  {

    protected Figure(){}
	
	public static int WHITE = 1;
	public static int BLACK = 0;
	
	protected int FIGURE_TYPE_ID = 0;
	
	protected String image       = null;
	protected String imageActive = null;
	protected int x;
	protected int y;
	protected int color;
	protected ChessBoard ChessBoard;
	protected final int DIRECTION_X  = 0;
	protected final int DIRECTION_Y  = 1;
	
	public Figure( int color , int x , int y , ChessBoard board ) {
		this.color = color;
		this.x     = x;
		this.y     = y;
		this.ChessBoard = board;
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

	public void setPosition( Position position ) {
		x = position.getX();
        y = position.getY();
	}
	public Position getPosition() {
		return new Position(x,y);
	}
	
	public int getType() {
		return this.FIGURE_TYPE_ID;
	}
	
	public byte[] getSerialized() {
		byte[] data = Serialize.serialize( new FigureData( this.getX(), this.getY(), this.getColor(), this.getType() ) );
		return data;
	}


	
	public abstract List<Position> getAviableMoves( );
	public abstract List<MoveLine> getAviableDirectionsLines( );

	

}



