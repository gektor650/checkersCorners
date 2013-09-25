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
        this.level = board.getGameLevel()+1;

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

			priorities[2] = new int[]{1037, 1036, 1035, 38, 36, 34, 31, 27 };

			priorities[3] = new int[]{1036, 1035, 1034, 38, 37, 33, 29, 26 };

			priorities[4] = new int[]{35  , 38  , 38  , 36, 33, 30, 27, 24 };

			priorities[5] = new int[]{34  , 35  , 34  , 32, 30, 28, 26, 23 };

			priorities[6] = new int[]{32  , 33  , 31  , 29, 27, 26, 24, 22 };

			priorities[7] = new int[]{31  , 31  , 29  , 27, 25, 23, 22,  1 };
			
		}
	}
	
	public void move() {
        board.save();
		FigureAndPosition result = calcFigureAndPositionOfBestMove( board.getFigures() , level );
        board.load();
		board.setAiTurnShowField(board.getFigures().get(result.figureIndex).getPosition(), result.position);
        board.buildTips(result.figureIndex, result.position.getX(), result.position.getY());

        board.move( result.figureIndex , result.position );
	}

    /**
     * Рекурсивная функция просчета весов возможных ходов фигур.
     * @param figures - фигуры на шахматной доске
     * @param depth   - сколько раз проверять рекурсивно ходы фигур (для каждой фигуры depth раз остальные фигуры)
     * @return результат из индекс фигуры с максимальным весом хода , позицию максимального хода, вес хода
     */
	public FigureAndPosition calcFigureAndPositionOfBestMove( List<Figure> figures , int depth ) {
		int bestResult = -99;
		int currWeight = 0;
		int tmp;

		List<Position> positions;
        FigureAndPosition result = new FigureAndPosition();

        for( int i = 0 ; i < figures.size() ; i++ ) {
            if( figures.get(i).getColor() != color ) continue;
            Position tmpPosition1 = figures.get(i).getPosition();
            positions  = figures.get(i).getAviableMoves();
            currWeight = positionToFieldWeight( figures.get(i).getPosition() );
            for ( Position position : positions ) {
                tmp = positionToFieldWeight( position ) - currWeight;
                if( depth > 0 ) {
                    Position tmpPosition2 = figures.get(i).getPosition();
                    figures.get(i).setPosition( position );
                    result = calcFigureAndPositionOfBestMove( figures , depth - 1 );
                    tmp   += result.weightInt;
                    figures.get(i).setPosition( tmpPosition2 );
                }
                if (tmp > bestResult ) {
                    bestResult         = tmp;
                    result.weightInt   = bestResult;
                    result.figureIndex = i;
                    result.position    = position;
                }
            }
            figures.get(i).setPosition( tmpPosition1 );
        }

		return result;
	}
	
	public int positionToFieldWeight( Position position ) {
		return priorities[ position.x ][ position.y ];
    }


}
