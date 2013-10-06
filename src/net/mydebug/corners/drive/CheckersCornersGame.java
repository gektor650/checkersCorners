package net.mydebug.corners.drive;



import java.util.ArrayList;
import net.mydebug.corners.drive.figures.CheckerCorners;
import net.mydebug.corners.drive.figures.CheckersCornersAi;
import net.mydebug.corners.drive.figures.Figure;
import net.mydebug.corners.drive.figures.FigureData;
import net.mydebug.corners.drive.figures.MoveLine;
import net.mydebug.corners.drive.figures.Position;
import com.badlogic.androidgames.framework.Game;

/**
 * Собственно говоря наша игра в Уголки
 */

public class CheckersCornersGame extends ChessGame {

	public CheckersCornersGame( Game game , boolean newGame ) {
		super(game , newGame );
        if( gameMode == ONE_PLAYER ) {
            AiModel = new CheckersCornersAi( aiColor , this );
            if( playerColor == Figure.BLACK && newGame ) {
                AiModel.move();
            }
        }

    }

	
	
	@Override
	protected void press(int x, int y) {
        //определяем есть ли в данном поле фигура
		int index = getFigureIndexByField(x,y);
        // если это второе нажатие, а до этого мы нажимали на какую-то фигуру проверим может ли эта
        // фигура ходить в место x y и перемещаем ее туда
		if( activeFigure > -1 ) {
			if( this.checkFieldIsEmpty( new Position( x , y )  ) == 1
					&& getTurn() == figures.get( activeFigure ).getColor() ) {
                // В прошлом нажатии в переменную tips были помещены возможные ходы текущей фигуры
                // проверим, если эта фигура может ходить в данную клетку - перемещаем ее туда
                for (Position tip : tips)
                    if (tip.x == x && tip.y == y) {
                        move(activeFigure, new Position(x, y));
                    }
			}
            // сбрасываем подсказки возможных ходов и активность фигуры
			tips      = new ArrayList<Position>();
			tipsLines = new ArrayList<MoveLine>();
			activeFigure = -1;
		}
        // если мы нажали на фигуру и у нас нет активной - заполняем переменную tips
        // возможными ходами фигуры figures.get(index);
		if( index > -1 && activeFigure == -1) {
			buildTips( index , x , y );
			activeFigure = index;
		}
	}

	@Override
	protected void initializeFigures() {
        figures = new ArrayList<Figure>();
		int i,j;
		for( i = 0 ; i < getBoardLength() / 2; i++  ) {
			for( j = 0 ; j < 3 ; j++ ) {
				figures.add( new CheckerCorners( Figure.WHITE , i , j , this ) );
			}
		}

		for( i = getBoardLength() / 2 ; i < getBoardLength() ; i++  ) {
			for( j = 5 ; j < getBoardLength() ; j++ ) {
				figures.add( new CheckerCorners( Figure.BLACK , i , j , this ) );				
			}
		}

	}

	@Override
	public void buildTips( int figureIndex , int x , int y ) {
		if( figures.get(figureIndex).getColor() == getTurn() ) {
			tips      = figures.get(figureIndex).getAviableMoves();
			tipsLines = figures.get(figureIndex).getAviableDirectionsLines();
		}
	}
	
	@Override
	protected int isGameOver() {
		int i;
		int whiteOnBase = 0;
		int blackOnBase = 0;
		for( i = 0 ; i < figures.size(); i++ ) {
			if( figures.get(i).getColor() == Figure.WHITE ) {
				if( figures.get(i).getPosition().x > 3 && figures.get(i).getPosition().y > 4 ) {
					whiteOnBase++;
				}
			}
			if( figures.get(i).getColor() == Figure.BLACK ) {
				if( figures.get(i).getPosition().x < 4 && figures.get(i).getPosition().y < 3 ) {
					blackOnBase++;
				}
			}
		}
		if( whiteOnBase == 12 ) {
			return Figure.WHITE;
		}
		if( blackOnBase == 12 ) {
			return Figure.BLACK;
		}
		return -1;
	}

	@Override
	public void setFiguresByFiguresData( ArrayList<FigureData> figureData ) {
		figures = new ArrayList<Figure>();
        for (FigureData aFigureData : figureData) {
            figures.add(new CheckerCorners(aFigureData.color, aFigureData.x, aFigureData.y, this));
        }
	
	}


	
}
