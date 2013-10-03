package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.mydebug.chessgames.drive.CheckersCornersGame;
import net.mydebug.chessgames.drive.ChessBoard;

public class CheckersCornersAi implements Ai {

	int [][] priorities;

    private CheckersCornersGame board;
    private int color;
    private int level      = 0;
    private int aiTurns    = 0;
    private int operations = 0;
    private Figure nearWhite = null;
    private Figure nearBlack = null;
    private boolean prioritiesRebuilded = false;

    // Сюда мы будем складировать ходы фигур, дабы они не зацикливались
    // Хранить будем в варианте номера на поле ( x * 8 + y = 64 наших поля ), для более быстрого подсчета
    // Массив массивов, в котором первый индекс - индекс фигуры, а в содержащамсе массиве - номера полей, в который уже фигура ходила
    List<List> movesHistory = new ArrayList<List>();
	
	public CheckersCornersAi( int color , CheckersCornersGame board ) {
		this.board = board;
		this.color = color;
        resetAi();
	}

    public void resetAi() {
        //Заполняем лист пустыми листами
        clearMoveHistory();
        level      = board.getSettings().getGameLevel();
        priorities = new int[board.getBoardLength()][board.getBoardLength()];
        // Массив приоритетов полей для АИ черных фигур
        priorities[0] = new int[]{46, 45, 44, 33, 31, 30, 28, 26 };

        priorities[1] = new int[]{45, 44, 43, 33, 31, 29, 28, 25 };

        priorities[2] = new int[]{44, 43, 42, 32, 30, 29, 27, 24 };

        priorities[3] = new int[]{43, 42, 41, 31, 29, 27, 25, 22 };

        priorities[4] = new int[]{31, 31, 30, 29, 27, 26, 24, 21 };

        priorities[5] = new int[]{30, 29, 29, 27, 26, 25, 23, 20 };

        priorities[6] = new int[]{28, 28, 27, 25, 24, 23, 21, 19 };

        priorities[7] = new int[]{26, 25, 24, 22, 21, 20, 19, 16 };

        // Если цвет АИ белый - инвертинуем наши приоритеты полей
        if( color == Figure.WHITE ) {
            reversePriorities();
        }
    }

    private void  reversePriorities() {
        int[][] prioritiesReverse = new int[board.getBoardLength()][board.getBoardLength()];
        for( int i = 0 ; i < priorities.length ; i++ ) {
            for( int j = 0 ; j < priorities.length ; j++) {
                prioritiesReverse[i][j] = priorities[ priorities.length - i - 1 ][ priorities.length - j - 1 ];
            }
        }
        priorities = prioritiesReverse;
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
        //Если все фигуры пришли на место - увеличиваем глубину просчета количества ходов и изменяем приоритеты
        //полей, дабы не тупили фигуры
        if( checkAlmostWin() ) {
            rebuildPriorities();
        }
        for( int i = 0 ; i < priorities.length ; i++ ) {
            for( int j = 0 ; j < priorities[i].length ; j++ ) {
                System.out.print( priorities[j][i] + " ");
            }
            System.out.println("");
        }
    }

    private boolean checkAlmostWin() {
        int whiteOnBase = 0;
        int blackOnBase = 0;
        boolean isNearWhite  = false;
        boolean isNearBlack  = false;
        for( Figure figure: board.getFigures() ) {
            if( figure.getColor() == Figure.WHITE && figure.getColor() == color ) {
                if( figure.getPosition().getX() > 3 && figure.getPosition().getY() > 4 ) {
                    whiteOnBase++;
                } else if(  figure.getPosition().getX() > 4 && figure.getPosition().getY() > 5  ) {
                    isNearWhite = true;
                    nearWhite   = figure;
                }
            }
            if( figure.getColor() == Figure.BLACK && figure.getColor() == color ) {
                if( figure.getPosition().getX() < 4 && figure.getPosition().getY() < 3 ) {
                    blackOnBase++;
                } else if(  figure.getPosition().getX() < 5 && figure.getPosition().getY() < 4  ) {
                    isNearBlack = true;
                    nearBlack   = figure;
                }
            }
        }
        if( color == Figure.WHITE )
            return ( whiteOnBase == 11 && isNearWhite);
        else
            return ( blackOnBase == 11 && isNearBlack );
    }

    private Position getEmptyOnBase() {
        if( color == Figure.BLACK ) {
            for( int i = 0 ; i < 4 ; i++ ) {
                for( int j = 0 ; j < 3 ; j++ ) {
                    Position tmpPosition = new Position( i , j );
                    if( board.checkFieldIsEmpty( tmpPosition ) == 1 ) {
                        return tmpPosition;
                    }
                }
            }
        } else if( color == Figure.WHITE ) {
            for( int i = ChessBoard.CHESSBOARD_FIELDS_COUNT - 1 ; i > ChessBoard.CHESSBOARD_FIELDS_COUNT - 5 ; i-- ) {
                for( int j = ChessBoard.CHESSBOARD_FIELDS_COUNT - 1 ; j > ChessBoard.CHESSBOARD_FIELDS_COUNT - 4 ; j-- ) {
                    Position tmpPosition = new Position( i , j );
                    if( board.checkFieldIsEmpty( tmpPosition ) == 1 ) {
                        return tmpPosition;
                    }
                }
            }
        }
        return null;
    }

    private void rebuildPriorities() {
//        System.out.println("rebuilded");
        priorities[0] = new int[]{250, 250, 200,  0, -99, -99, -99, -99 };

        priorities[1] = new int[]{250, 250, 200,  0, -99, -99, -99, -99 };

        priorities[2] = new int[]{250, 250, 200,  0, -99, -99, -99, -99 };

        priorities[3] = new int[]{200, 200, 200,  0, -99, -99, -99, -99 };

        priorities[4] = new int[]{  0,   0,   0,  0, -99, -99, -99, -99 };

        priorities[5] = new int[]{ -99,  -99,  -99,  -99, -99, -99, -99, -99 };

        priorities[6] = new int[]{ -99,  -99,  -99,  -99, -99, -99, -99, -99 };

        priorities[7] = new int[]{ -99,  -99,  -99,  -99, -99, -99, -99, -99 };

        // Если цвет АИ белый - инвертинуем наши приоритеты полей
        if( color == Figure.WHITE ) {
            reversePriorities();
        }
        // Находим пустое поле на "базе"
        Position emptyOnBase = getEmptyOnBase();
        if( emptyOnBase != null ) {
            // Расставляем вокруг пустого поля
            priorities[ emptyOnBase.getX() ][ emptyOnBase.getY()] = 400;
            if( emptyOnBase.getX()+1 <  ChessBoard.CHESSBOARD_FIELDS_COUNT )
                priorities[ emptyOnBase.getX() + 1 ][ emptyOnBase.getY() ] = 300;
            if( emptyOnBase.getY()+1 <  ChessBoard.CHESSBOARD_FIELDS_COUNT )
                priorities[ emptyOnBase.getX() ][ emptyOnBase.getY() + 1 ] = 300;
            if( emptyOnBase.getY()-1 > 0 )
                priorities[ emptyOnBase.getX() ][ emptyOnBase.getY() - 1 ] = 300;
            if( emptyOnBase.getX()-1 > 0 )
                priorities[ emptyOnBase.getX() - 1 ][ emptyOnBase.getY() ] = 300;
        }
        // Расстывляем приоритеты вокруг фигуры, недошедгей до базы
        Figure nearest;
        if( color == Figure.WHITE ) {
            nearest = nearWhite;
        } else {
            nearest = nearBlack;
        }
        priorities[ nearest.getX() ][ nearest.getY() ] = 0;
        if( nearest.getX()+1 <  ChessBoard.CHESSBOARD_FIELDS_COUNT )
            priorities[ nearest.getX() + 1 ][ nearest.getY() ] = 100;
        if( nearest.getY()+1 <  ChessBoard.CHESSBOARD_FIELDS_COUNT )
            priorities[ nearest.getX() ][ nearest.getY() + 1 ] = 100;
        if( nearest.getY()-1 > 0 )
            priorities[ nearest.getX() ][ nearest.getY() - 1 ] = 100;
        if( nearest.getX()-1 > 0 )
            priorities[ nearest.getX() - 1 ][ nearest.getY() ] = 100;
        level = 2;
        prioritiesRebuilded = true;
    }

    /**
     * Рекурсивная функция просчета весов возможных ходов фигур.
     * @param figures - фигуры на шахматной доске
     * @param depth   - сколько раз проверять рекурсивно ходы фигур (для каждой фигуры depth раз остальные фигуры)
     * @return результат из индекс фигуры с максимальным весом хода , позицию максимального хода, вес хода
     */
	public FigureAndPosition calcFigureAndPositionOfBestMove( List<Figure> figures , int depth ) {
        float bestResult = -99;
		int currWeight   = 0;
		float tmp;
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
                    // Делаем вес хода второго уровня вдвое меньше
                    // третьего - в трое и т.д.
                    float coef = ( level == depth ) ?  depth : 1 ;
                    tmp   += resultTmp.weight / coef ;
                    figures.get(i).setPosition( tmpPosition2 );
                }
                // Если вес хода для фигур одинаковый - то ходить должны
                // для черных аи последняя фигура ( >=  (поскольку i должен быть максимальным(нижний правый угол)))
                // для белых аи первая фигура ( строго >  ( первый i из фигур ( верхний левый угол)) )
                boolean bestResultByColor;
                if( color == Figure.BLACK ) {
                    bestResultByColor = tmp >= bestResult;
                } else {
                    bestResultByColor = tmp >  bestResult;
                }

                if ( bestResultByColor ) {
                    bestResult         = tmp;
                    result.weight      = bestResult;
                    result.figureIndex = i;
                    result.position    = position;
                    // Для легкого уровня вносим хаотичность ходов
                    if( i > random && tmp > 0 ) {
//                        System.out.println("Breaked on " + i);
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
