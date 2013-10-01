package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.mydebug.chessgames.drive.CheckersCornersGame;

public class CheckersCornersAi implements Ai {

	int [][] priorities;

    CheckersCornersGame board;
	int color;
    int level      = 0;
    int aiTurns    = 0;
    int operations = 0;

    // Сюда мы будем складировать ходы фигур, дабы они не зацикливались
    // Хранить будем в варианте номера на поле ( x * 8 + y = 64 наших поля ), для более быстрого подсчета
    // Массив массивов, в котором первый индекс - индекс фигуры, а в содержащамсе массиве - номера полей, в который уже фигура ходила
    List<List> movesHistory = new ArrayList<List>();
	
	public CheckersCornersAi( int color , CheckersCornersGame board ) {
		this.board = board;
		this.color = color;
        level      = board.getSettings().getGameLevel();
        //Заполняем лист пустыми листами
        clearMoveHistory();

        priorities = new int[board.getBoardLength()][board.getBoardLength()];
        // Массив приоритетов полей для АИ черных фигур
        priorities[0] = new int[]{46, 45, 43, 33, 31, 30, 28, 26 };

        priorities[1] = new int[]{45, 44, 42, 33, 31, 29, 28, 25 };

        priorities[2] = new int[]{44, 43, 42, 32, 30, 29, 27, 24 };

        priorities[3] = new int[]{42, 42, 41, 31, 29, 27, 25, 23 };

        priorities[4] = new int[]{31, 31, 30, 29, 27, 26, 24, 22 };

        priorities[5] = new int[]{30, 29, 29, 27, 26, 25, 23, 20 };

        priorities[6] = new int[]{28, 28, 27, 25, 24, 23, 21, 19 };

        priorities[7] = new int[]{26, 25, 24, 23, 22, 20, 19, 16 };

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

    public void clearMoveHistory() {
        movesHistory = new ArrayList<List>();
        for( Figure figure : board.getFigures() ) {
            movesHistory.add( new ArrayList<Integer>());
        }
    }
	
	public void move() {
        System.out.println("Depth:" + level );
        FigureAndPosition result = calcFigureAndPositionOfBestMove( board.getFigures() , level );
		board.setAiTurnShowField(board.getFigures().get(result.figureIndex).getPosition(), result.position);
        board.buildTips(result.figureIndex, result.position.getX(), result.position.getY());
//        System.out.println("---------------------");
//        System.out.println( "x:" + result.position.getX() + " y:" + result.position.getY() + " w:" + result.weight );
        System.out.println("С веса:" + positionToFieldWeight( board.getFigures().get(result.figureIndex).getPosition() ));
        System.out.println("На вес:" + positionToFieldWeight( result.position ));

        board.move(result.figureIndex, result.position);
        movesHistory.get( result.figureIndex ).add( result.position.getY() * 8 + result.position.getX()  );
        //Если уровень сложности больше 0, то прощитываем сначала на n шагов вперед при первом ходе
        //при втором на n-1, пока не достингнем 0 уровня
//        if( --level < 0 ) {
//            level = board.getSettings().getGameLevel();
//        }
//        System.out.println("Операций: " + operations );
        System.out.println("Вес:"+result.weight);
        operations = 0;
        aiTurns++;
        if( aiTurns % 10 == 0 ) {
            clearMoveHistory();
        }
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
        /*
         Переменная для легкого уровня. Если уровень легкий - обрываем рандомно поиск
         хода, если он положительный
         */
        int random = 99;

        if( board.getGameLevel() == 0 ) {
            Random r = new Random();
            random   = r.nextInt( figures.size() / 2 );
            // Если АИ - черные - то добавим к рандому количество белых
            // шашек, чтоб мы пропускали именно на черных
            if( color == Figure.BLACK ) {
                random += figures.size() / 2 ;
            }
        }
		List<Position> positions;
        FigureAndPosition result    = new FigureAndPosition();
        FigureAndPosition resultTmp;

        LABEL: for( int i = 0 ; i < figures.size() ; i++ ) {
            if( figures.get(i).getColor() != color ) continue;
            Position tmpPosition1 = figures.get(i).getPosition();
            positions  = figures.get(i).getAviableMoves();
            currWeight = positionToFieldWeight( figures.get(i).getPosition() );
            for ( Position position : positions ) {
                tmp = positionToFieldWeight( position ) - currWeight;
                // Если эта фигура уже ходила в это поле - пропускаем поле
                if( movesHistory.get(i).contains( position.getY() * 8 + position.getX() ) )  {
                    continue;
                }
                //Если уровень максимальный уровень сложности - рекурсивно побегаем по фигуркам
                if( depth > 1 ) {
                    Position tmpPosition2 = figures.get(i).getPosition();
                    figures.get(i).setPosition( position );
                    resultTmp = calcFigureAndPositionOfBestMove( figures , depth - 1 );
                    tmp   += resultTmp.weight / 2;
                    figures.get(i).setPosition( tmpPosition2 );
                }
                if (tmp > bestResult ) {
                    bestResult         = tmp;
                    result.weight      = bestResult;
                    result.figureIndex = i;
                    result.position    = position;
                    // Для легкого уровня вносим хаотичность ходов
                    if( i > random && tmp > 0 ) {
                        System.out.println("Breaked on " + i);
                        break LABEL;
                    }
                }
            }
            figures.get(i).setPosition( tmpPosition1 );
//            System.out.println( "x:" + result.position.getX() + " y:" + result.position.getY() + " w:" + result.weight );
        }
//        System.out.println("Size: " + figures.size() );
//        System.out.println("Random: " + random );
        operations++;
		return result;
	}
	
	public int positionToFieldWeight( Position position ) {
		return priorities[ position.x ][ position.y ];
    }

}
