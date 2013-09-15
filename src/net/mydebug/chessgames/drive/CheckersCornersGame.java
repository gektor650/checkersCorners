package net.mydebug.chessgames.drive;



import java.util.ArrayList;
import net.mydebug.chessgames.drive.figures.CheckerCorners;
import net.mydebug.chessgames.drive.figures.CheckersCornersAi;
import net.mydebug.chessgames.drive.figures.Figure;
import net.mydebug.chessgames.drive.figures.FigureData;
import net.mydebug.chessgames.drive.figures.MoveLine;
import net.mydebug.chessgames.drive.figures.Position;


import com.badlogic.androidgames.framework.Game;

public class CheckersCornersGame extends ChessBoard {

    protected int time;

	public CheckersCornersGame( Game game , boolean newGame ) {
		super(game , newGame );
        if( gameMode == ONE_PLAYER ) {
            AiModel = new CheckersCornersAi( aiColor , (ChessBoard) this );
            if( playerColor == Figure.BLACK )
                AiModel.move();
        }

    }

	
	
	@Override
	protected void press(int x, int y) {
		int index = getFigureIndexByField(x,y);
		if( activeFigure > -1 ) {
			if( this.checkFieldIsEmpty( new Position( x , y )  ) == 1
					&& getTurn() == figures.get( activeFigure ).getColor() ) {
				for(int i = 0 ; i < tips.size(); i ++ )
					if( tips.get(i).x == x && tips.get(i).y == y ) {
						move( activeFigure  , new Position( x , y ) );
					}
			}
			tips      = new ArrayList<Position>();
			tipsLines = new ArrayList<MoveLine>();
			activeFigure = -1;
		} 
		if( index > -1 && activeFigure == -1) {
			buildTips( index , x , y );
			activeFigure = index;
		}
	}

	@Override
	protected void initializeFigures() {
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
	protected void buildTips( int figureIndex , int x , int y ) {
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
		for( int i = 0 ; i < figureData.size() ; i++ ) {
			figures.add( new CheckerCorners( figureData.get(i).color, figureData.get(i).x , figureData.get(i).y , this ) );	
		}
	
	}


	
}
