package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import net.mydebug.chessgames.drive.ChessBoard;

public class CheckersCornersAi implements Ai {

	int [][] priorities;
	ChessBoard board;
	int color;
	
	public CheckersCornersAi( int color , ChessBoard board ) {
		this.board = board;
		this.color = color;
		priorities = new int[board.getBoardLength()][board.getBoardLength()];
		if( color == Figure.WHITE ) {
			for( int i = 0 ; i < priorities.length ; i++ ) {
				for( int j = 0 ; j < priorities.length ; j++ ) {
					if( i > 4 && j > 3 ) {
						priorities[i][j] = i+j+10;	
					} else {
						priorities[i][j] = i+j;	
					}
				}
			}	
//			priorities[0] = new int[]{1064, 1056, 1048, 24, 23, 20, 16, 8 };
//			priorities[1] = new int[]{1056, 1049, 1042, 25, 22, 21, 14, 7 };
//			priorities[2] = new int[]{1048, 1042, 1036, 26, 21, 18, 12, 6 };
//			priorities[3] = new int[]{1040, 1035, 1030, 27, 20, 19, 10, 5 };
//			priorities[4] = new int[]{22  , 24  , 26  , 28, 19, 18, 16, 8 };
//			priorities[5] =  = new int[board.getBoardLength()][board.getBoardLength()];new int[]{18  , 20  , 22  , 20, 16, 12, 8 , 4 };
//			priorities[6] = new int[]{16  , 14  , 12  , 10, 8 , 6 , 4 , 2 };
//			priorities[7] = new int[]{8   , 7   , 6   , 5 , 4 , 3 , 2 , 1 };
			
		} else {
			priorities[0] = new int[]{1064, 1056, 1048, 24, 23, 20, 16, 8 };
			priorities[1] = new int[]{1056, 1049, 1042, 25, 22, 21, 14, 7 };
			priorities[2] = new int[]{1048, 1042, 1036, 26, 21, 18, 12, 6 };
			priorities[3] = new int[]{1040, 1035, 1030, 27, 20, 19, 10, 5 };
			priorities[4] = new int[]{22  , 24  , 26  , 28, 19, 18, 16, 8 };
			priorities[5] = new int[]{18  , 20  , 22  , 20, 16, 12, 8 , 4 };
			priorities[6] = new int[]{16  , 14  , 12  , 10, 8 , 6 , 4 , 2 };
			priorities[7] = new int[]{8   , 7   , 6   , 5 , 4 , 3 , 2 , 1 };
			
		}
//		for( int i = 0 ; i < priorities.length ; i++ ) {
//			for( int j = 0 ; j < priorities[i].length ; j++ ) 
//				System.out.print( priorities[i][j] + " ");
//			System.out.println();
//		}

	}
	
	public void move() {
		FigureAndPosition result = calcFigureAndPositionOfBestMove( board.getFigures() );
		if( result == null ) return;

		board.setAiTurnShowField( board.getFigures().get(result.figureIndex).getPosition() , result.position);
		board.move( result.figureIndex , result.position );
	}
	
	public FigureAndPosition calcFigureAndPositionOfBestMove( List<Figure> figures ) {
		int bestResult = -99;
		int currWeight = 0;
		int tmp;
		FigureAndPosition result = new FigureAndPosition();
		List positions = new ArrayList<Position>();
		for( int i = 0 ; i < figures.size() ; i++ ) {
			if( figures.get(i).getColor() != color ) continue;
			positions  = figures.get(i).getAviableMoves();
			currWeight = positionToFieldWeight( figures.get(i).getPosition() );
			for( int j = 0 ; j < positions.size() ; j++ ) {
				tmp = positionToFieldWeight( (Position) positions.get( j ) ) - currWeight;
				if( tmp > bestResult ) {
					bestResult = tmp;
					result.figureIndex = i;
					result.position = (Position) positions.get( j );
				}
			}
		}
		return result;
	}
	
	public int positionToFieldWeight( Position position ) {
		return priorities[ position.x ][ position.y ];
	}
	
	public int getTurn( Figure figure , List<Position> moves ) {
		if( true ) {
			
		}
		return 1;
	}
	
	public int getWeigth() {
		return 0;
	}




}
