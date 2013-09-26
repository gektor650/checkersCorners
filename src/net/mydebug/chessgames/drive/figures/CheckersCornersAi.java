package net.mydebug.chessgames.drive.figures;

import java.util.List;
import net.mydebug.chessgames.drive.CheckersCornersGame;

public class CheckersCornersAi implements Ai {

	int [][] priorities;

    CheckersCornersGame board;
	int color;
    int level = 0;
	
	public CheckersCornersAi( int color , CheckersCornersGame board ) {
		this.board = board;
		this.color = color;
        level      = board.getSettings().getGameLevel() ;

        priorities = new int[board.getBoardLength()][board.getBoardLength()];
        // Массив приоритетов полей для АИ черных фигур
        priorities[0] = new int[]{44, 43, 40, 36, 35, 33, 32, 29 };

        priorities[1] = new int[]{43, 42, 40, 37, 35, 33, 32, 28 };

        priorities[2] = new int[]{42, 41, 40, 38, 36, 34, 31, 27 };

        priorities[3] = new int[]{40, 40, 39, 37, 36, 33, 29, 26 };

        priorities[4] = new int[]{35, 38, 37, 36, 33, 30, 27, 25 };

        priorities[5] = new int[]{34, 35, 34, 32, 30, 28, 26, 23 };

        priorities[6] = new int[]{32, 33, 31, 29, 27, 26, 24, 22 };

        priorities[7] = new int[]{31, 31, 29, 27, 25, 23, 22, 20 };

        // Если цвет АИ белый - инвертинуем наши приоритеты полей
		if( color == Figure.WHITE ) {
            int[][] prioritiesReverse = new int[board.getBoardLength()][board.getBoardLength()];
            for( int i = 0 ; i < priorities.length ; i++ ) {
                for( int j = 0 ; j < priorities.length ; j++) {
                    prioritiesReverse[i][j] = priorities[ priorities.length - i - 1 ][ priorities.length - j - 1 ];
                }
            }
            priorities = prioritiesReverse;

        }
	}
	
	public void move() {
		FigureAndPosition result = calcFigureAndPositionOfBestMove( board.getFigures() , level );
		board.setAiTurnShowField(board.getFigures().get(result.figureIndex).getPosition(), result.position);
        board.buildTips(result.figureIndex, result.position.getX(), result.position.getY());
//        System.out.println("---------------------");
//        System.out.println( "x:" + result.position.getX() + " y:" + result.position.getY() + " w:" + result.weight );
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
        FigureAndPosition result    = new FigureAndPosition();
        FigureAndPosition resultTmp;

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
                    resultTmp = calcFigureAndPositionOfBestMove( figures , depth - 1 );
                    tmp   += resultTmp.weight;
                    figures.get(i).setPosition( tmpPosition2 );
                }
                if (tmp > bestResult ) {
                    bestResult         = tmp;
                    result.weight      = bestResult;
                    result.figureIndex = i;
                    result.position    = position;
                }
            }
            figures.get(i).setPosition( tmpPosition1 );
//            System.out.println( "x:" + result.position.getX() + " y:" + result.position.getY() + " w:" + result.weight );
        }

		return result;
	}
	
	public int positionToFieldWeight( Position position ) {
		return priorities[ position.x ][ position.y ];
    }

}
