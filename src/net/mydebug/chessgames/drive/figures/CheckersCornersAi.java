package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;
import net.mydebug.chessgames.drive.CheckersCornersGame;

public class CheckersCornersAi implements Ai {

	int [][] priorities;

    CheckersCornersGame board;
	int color;
    int level = 0;

    // Сюда мы будем складировать ходы фигур, дабы они не зацикливались
    // Хранить будем в варианте номера на поле ( x * 8 + y = 64 наших поля ), для более быстрого подсчета
    // Массив массивов, в котором первый индекс - индекс фигуры, а в содержащамсе массиве - номера полей, в который уже фигура ходила
    List<List> movesHistory = new ArrayList<List>();
	
	public CheckersCornersAi( int color , CheckersCornersGame board ) {
		this.board = board;
		this.color = color;
        level      = board.getSettings().getGameLevel();
        //Заполняем лист пустыми листами
        for( Figure figure : board.getFigures() ) {
            movesHistory.add( new ArrayList<Integer>());
        }

        priorities = new int[board.getBoardLength()][board.getBoardLength()];
        // Массив приоритетов полей для АИ черных фигур
        priorities[0] = new int[]{46, 45, 42, 36, 35, 33, 32, 29 };

        priorities[1] = new int[]{45, 44, 42, 37, 36, 33, 32, 28 };

        priorities[2] = new int[]{44, 43, 42, 38, 37, 34, 31, 27 };

        priorities[3] = new int[]{42, 42, 41, 37, 36, 33, 29, 26 };

        priorities[4] = new int[]{39, 39, 38, 36, 33, 30, 27, 25 };

        priorities[5] = new int[]{35, 35, 34, 32, 30, 28, 26, 23 };

        priorities[6] = new int[]{33, 33, 31, 29, 27, 26, 24, 22 };

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
        movesHistory.get( result.figureIndex ).add( result.position.getY() * 8 + result.position.getX()  );
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
                    if( depth == board.getSettings().getGameLevel()  && movesHistory.get( result.figureIndex ).contains( position.getY() * 8 + position.getX() ) ) {
                        System.out.println(depth);
                    } else {
                        bestResult         = tmp;
                        result.weight      = bestResult;
                        result.figureIndex = i;
                        result.position    = position;
                    }
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
