package net.mydebug.chessgames.drive.figures;



public abstract  class Checker extends Figure {
	
	public Checker(int color , int x , int y ) {
		super(color , x , y );
		if( color == Figure.WHITE ) 
			image = "checkerWhite.png";
		else 			
			image = "checkerBlack.png";
		imageActive = "checkerActive.png";
	}

}
