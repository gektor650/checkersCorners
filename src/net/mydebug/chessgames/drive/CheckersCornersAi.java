package net.mydebug.chessgames.drive;

import java.util.List;

import net.mydebug.chessgames.drive.figures.Ai;
import net.mydebug.chessgames.drive.figures.Figure;
import net.mydebug.chessgames.drive.figures.Position;

public class CheckersCornersAi implements Ai {

	int [][] priorities = new int[ChessBoard.getBoardLength()][ChessBoard.getBoardLength()];
	
	public CheckersCornersAi( int color ) {
		if( color == Figure.WHITE ) {
			for( int i = 0 ; i < priorities.length ; i++ ) {
				for( int j = 0 ; j < priorities.length ; j++ ) {
					if( i > 3 && j > 4 ) {
						priorities[i][j] = (i+1)*(j+1)*50;	
					} else {
						priorities[i][j] = (i+1)*(j+1);	
					}
				}
			}	
		} else {
			for( int i = priorities.length - 1 ; i >= 0 ; i-- ) {
				for( int j =  priorities.length - 1  ; j  >= 0 ; j-- ) {
					if( i < 4 && j < 3 ) {
						priorities[i][j] = (i+1)*(j+1)*50;	
					} else {
						priorities[i][j] = (i+1)*(j+1);	
					}			
				}
			}	
		}
	}
	
	public int getTurn( Figure figure , List<Position> moves ) {
		if( true ) {
			
		}
		return 1;
	}
	
	public int getWeigth() {
		return 0;
	}

	
	@Override
	public int checkPosition( Figure figure , Position position ) {
		
		return 0;
	}


}
