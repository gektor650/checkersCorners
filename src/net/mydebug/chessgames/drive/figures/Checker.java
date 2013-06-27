package net.mydebug.chessgames.drive.figures;
import net.mydebug.chessgames.drive.ChessBoard;


public abstract  class Checker extends Figure {
	


	public Checker(int color , int x , int y , ChessBoard board ) {
		super(color , x , y , board );
		if( color == Figure.WHITE ) 
			image = "checkerWhite.png";
		else 			
			image = "checkerBlack.png";
		imageActive = "checkerActive.png";
	}

}
