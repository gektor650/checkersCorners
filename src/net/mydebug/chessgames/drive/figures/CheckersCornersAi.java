package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import net.mydebug.chessgames.drive.ChessBoard;

public class CheckersCornersAi implements Ai {

	int [][] priorities;
	ChessBoard board;
	int color;
    int level = 0;
	
	public CheckersCornersAi( int color , ChessBoard board ) {
		this.board = board;
		this.color = color;
		priorities = new int[board.getBoardLength()][board.getBoardLength()];
        level = board.getSettings().getSettings().gameLevel;
		if( color == Figure.WHITE ) {
			priorities[0] = new int[]{ 1  , 22  , 23  , 24, 25, 26, 27, 28 };
            priorities[1] = new int[]{22  , 24  , 26  , 28, 30, 32, 34, 36 };
            priorities[2] = new int[]{24  , 28  , 34  , 36, 40, 42, 40, 38 };
            priorities[3] = new int[]{28  , 36  , 38  , 39, 48, 46, 44, 42 };
            priorities[4] = new int[]{25, 30, 39, 40, 47, 1030, 1035, 1040 };
            priorities[5] = new int[]{26, 32, 38, 41, 46, 1036, 1042, 1048 };
            priorities[6] = new int[]{27, 34, 41, 42, 45, 1042, 1049, 1056 };
            priorities[7] = new int[]{28, 36, 40, 42, 44, 1048, 1056, 1064 };

		} else {
			priorities[0] = new int[]{1064, 1056, 1048, 44, 43, 40, 38, 28 };
			priorities[1] = new int[]{1056, 1049, 1042, 45, 42, 41, 36, 27 };
			priorities[2] = new int[]{1048, 1042, 1036, 46, 41, 38, 34, 26 };
			priorities[3] = new int[]{1040, 1035, 1030, 47, 40, 39, 32, 28 };
			priorities[4] = new int[]{42  , 44  , 47  , 48, 39, 38, 30, 26 };
			priorities[5] = new int[]{38  , 40  , 42  , 40, 36, 32, 28, 24 };
			priorities[6] = new int[]{36  , 34  , 32  , 30, 28, 26, 24, 22 };
			priorities[7] = new int[]{28  , 27  , 26  , 25, 24, 23, 22,  1 };
			
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
        if( level == 2 ) {
            for( int i = 0 ; i < figures.size() ; i++ ) {
                if( figures.get(i).getColor() != color ) continue;
                positions  = figures.get(i).getAviableMoves();
                currWeight = positionToFieldWeight( figures.get(i).getPosition() );
                for( int j = 0 ; j < positions.size() ; j++ ) {
                    tmp = positionToFieldWeight( (Position) positions.get( j ) ) - currWeight;
                    if( tmp > bestResult ) {
                        bestResult = tmp;
                        result.figureIndex = i;
                        result.position    = (Position) positions.get( j );
                    }
                }
            }
        } else if( level == 11 ) {
            Random rand = new Random();
            for( int i = 0 ; i < figures.size() ; i++ ) {
                if( figures.get(i).getColor() != color ) continue;
                positions  = figures.get(i).getAviableMoves();
                int index   = rand.nextInt( positions.size() - 1 );
                if( index > 0 ) {
                    result.figureIndex = i;
                    result.position    = (Position) positions.get( index );
                }
            }
        } else {
            Random rand = new Random();
            List<FigureAndPosition> movesList = new ArrayList<FigureAndPosition>();
            for( int i = 0 ; i < figures.size() ; i++ ) {
                if( figures.get(i).getColor() != color ) continue;
                positions  = figures.get(i).getAviableMoves();
                if( positions.size() < 1 ) continue;
                int index   = rand.nextInt( positions.size() - 1 );
                currWeight  = positionToFieldWeight( figures.get(i).getPosition() );
                for( int j = 0 ; j < positions.size() ; j++ ) {
                    tmp         = positionToFieldWeight( (Position) positions.get( j ) ) - currWeight;
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
