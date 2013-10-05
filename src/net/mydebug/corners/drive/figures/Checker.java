package net.mydebug.corners.drive.figures;
import net.mydebug.corners.drive.ChessGame;


public abstract  class Checker extends Figure {
	
    public Checker(){}

	public Checker(int color , int x , int y , ChessGame board ) {
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
