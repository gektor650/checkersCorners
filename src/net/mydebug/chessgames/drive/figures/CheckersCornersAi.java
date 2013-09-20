package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import net.mydebug.chessgames.drive.CheckersCornersGame;
import net.mydebug.chessgames.drive.ChessBoard;

public class CheckersCornersAi implements Ai {

	int [][] priorities;
    CheckersCornersGame board;
	int color;
    int level = 0;
	
	public CheckersCornersAi( int color , CheckersCornersGame board ) {
		this.board = board;
		this.color = color;
		priorities = new int[board.getBoardLength()][board.getBoardLength()];
        level = board.getSettings().getGameLevel();
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
			priorities[0] = new int[]{1039, 1038, 1037, 36, 35, 33, 32, 29 };

			priorities[1] = new int[]{1038, 1037, 1036, 37, 35, 33, 32, 28 };

			priorities[2] = new int[]{1037, 1036, 1035, 39, 36, 34, 31, 27 };

			priorities[3] = new int[]{1036, 1035, 1034, 38, 37, 33, 29, 26 };

			priorities[4] = new int[]{35  , 39  , 38  , 36, 33, 30, 27, 24 };

			priorities[5] = new int[]{34  , 35  , 34  , 32, 30, 28, 26, 23 };

			priorities[6] = new int[]{32  , 33  , 31  , 29, 27, 26, 24, 22 };

			priorities[7] = new int[]{31  , 31  , 29  , 27, 25, 23, 22,  1 };
			
		}
	}
	
	public void move() {
		FigureAndPosition result = calcFigureAndPositionOfBestMove( board.getFigures() );
		if( result == null ) return;

		board.setAiTurnShowField( board.getFigures().get(result.figureIndex).getPosition() , result.position);
        board.buildTips(result.figureIndex, result.position.getX(), result.position.getY());

        board.move( result.figureIndex , result.position );
	}
	
	public FigureAndPosition calcFigureAndPositionOfBestMove( List<Figure> figures ) {
		int bestResult = -99;
		int currWeight = 0;
		int tmp;
		List positions;
        FigureAndPosition result = new FigureAndPosition();
        Random rand              = new Random();

        if( level == 1 ) {
            if( rand.nextInt(1) == 1 ) {
                level = 2;
            }
        }

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
        } else {
            /*
            Самый слабый уровень AI - перезаписываем результат(ход, который сделает AI) случайное количество раз и
            до тех пор, пока не будет найден ход,
            который продвинет шашку вперед(вес теперешнего расположения меньше
             чем весом возможного хода).
             */
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
