package net.mydebug.chessgames.drive.figures;
import net.mydebug.chessgames.drive.ChessBoard;


public abstract  class Checker extends Figure {
	
    public Checker(){}

	public Checker(int color , int x , int y , ChessBoard board ) {
		super(color , x , y , board );
        setColor( color );
	}

    public void setColor( int color ) {
        if( color == Figure.WHITE )
            image = "checkerWhite1.png";
        else
            image = "checkerBlack1.png";
        imageActive = "checkerActive.png";
    }

}
