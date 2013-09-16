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
            priorities[4] = new int[]{25, 30, 39, 40, 47, 1034, 1035, 1036 };
            priorities[5] = new int[]{26, 32, 38, 41, 46, 1035, 1036, 1037 };
            priorities[6] = new int[]{27, 34, 41, 42, 45, 1036, 1037, 1038 };
            priorities[7] = new int[]{28, 36, 40, 42, 44, 1037, 1038, 1040 };

		} else {
			priorities[0] = new int[]{1040, 1038, 1037, 32, 31, 30, 29, 28 };
			priorities[1] = new int[]{1038, 1037, 1036, 31, 30, 29, 28, 27 };
			priorities[2] = new int[]{1037, 1036, 1035, 30, 29, 28, 27, 26 };
			priorities[3] = new int[]{1036, 1035, 1034, 29, 28, 27, 26, 25 };
			priorities[4] = new int[]{31  , 30  , 29  , 28, 27, 26, 25, 24 };
			priorities[5] = new int[]{30  , 29  , 28  , 27, 26, 25, 24, 23 };
			priorities[6] = new int[]{29  , 28  , 27  , 26, 25, 24, 23, 22 };
			priorities[7] = new int[]{28  , 27  , 26  , 25, 24, 23, 22,  1 };
			
		}
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
		List positions;
        if( level == 2 ) {
            for( int i = 0 ; i < figures.size() ; i++ ) {
                if( figures.get(i).getColor() != color ) continue;
                positions  = figures.get(i).getAviableMoves();
                currWeight = positionToFieldWeight( figures.get(i).getPosition() );
                for (Object position : positions) {
                    tmp = positionToFieldWeight((Position) position) - currWeight;
                    if (tmp > bestResult) {
                        bestResult = tmp;
                        result.figureIndex = i;
                        result.position = (Position) position;
                    }
                }
            }
        } else if( level == 1 ) {
            Random rand      = new Random();
            int randIndex    = rand.nextInt( figures.size() / 2 );
            int aiFiguresCnt = 0;
            FIGURES : for( int i = 0 ; i < figures.size() ; i++ ) {
                if( figures.get(i).getColor() != color ) continue;
                aiFiguresCnt++;
                positions  = figures.get(i).getAviableMoves();
                if( positions.size() < 1 ) continue;
                currWeight  = positionToFieldWeight( figures.get(i).getPosition() );
                for (Object position : positions) {
                    tmp = positionToFieldWeight((Position) position) - currWeight;
                    result.figureIndex = i;
                    result.position = (Position) position;
                    if ( tmp > 0 && randIndex < aiFiguresCnt ) {
                        break FIGURES;
                    }
                }
            }
        } else {
            /*
            Самый слабый уровень AI - перезаписываем результат, случайное количество раз и
            до тех пор, пока не будет найден ход,
            который продвинет шашку вперед.
             */
            Random rand      = new Random();
            int randIndex    = rand.nextInt( figures.size() / 2 );
            int aiFiguresCnt = 0;
            FIGURES : for( int i = 0 ; i < figures.size() ; i++ ) {
                if( figures.get( i ).getColor() != color ) continue;
                aiFiguresCnt++;
                positions  = figures.get(i).getAviableMoves();
                if( positions.size() < 1 ) continue;
                currWeight  = positionToFieldWeight( figures.get(i).getPosition() );
                for (Object position : positions) {
                    tmp = positionToFieldWeight((Position) position) - currWeight;
                    result.figureIndex = i;
                    result.position    = (Position) position;
                    if ( tmp > 0 && randIndex < aiFiguresCnt ) {
                        break FIGURES;
                    }
                }
            }
        }

		return result;
	}
	
	public int positionToFieldWeight( Position position ) {
		return priorities[ position.x ][ position.y ];
    }


}
