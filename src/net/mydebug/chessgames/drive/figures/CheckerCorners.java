package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import net.mydebug.chessgames.drive.ChessBoard;


public class CheckerCorners extends Checker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Position>turns;
	List<Position> moves = new ArrayList<Position>();
	private final int DIRECTION_X  = 0;
	private final int DIRECTION_Y  = 1;

	public CheckerCorners(int color , int x , int y ) {
		super(color , x , y );
		// TODO Auto-generated constructor stub
	}
	
	private void generateTurnsByPosition( Position position ) {
		turns = new ArrayList<Position>();
		turns.add( new Position( position.x , position.y + 1 ) );
		turns.add( new Position( position.x , position.y - 1 ) );
		turns.add( new Position( position.x - 1 , position.y ) );
		turns.add( new Position( position.x + 1 , position.y ) );
	}

	@Override
	public List<Position> getAviableMoves( int[][] figuresOnBoard,
			List<Figure> figures) {
		moves = new ArrayList<Position>();
		generateTurnsByPosition( getPosition() );
		for( int i = 0 ; i < turns.size() ; i++ ) {
			int isEmpty = checkFieldIsEmpty( turns.get(i) , figuresOnBoard );
			if( isEmpty == 1 ) {
				moves.add( turns.get(i) );
			} else if( isEmpty == 0 ) {
				int direction;
				int value;
				if( turns.get(i).x == this.x ) {
					value = -(this.y - turns.get(i).y); 
					direction = DIRECTION_Y;
				} else {
					value = -(this.x - turns.get(i).x) ;
					direction = DIRECTION_X;
				}
				checkTurnNextLevel( new Position( turns.get(i).x, turns.get(i).y ) , new Direction(direction, value) , figuresOnBoard );
			}
		}
		return moves;
	}
	
	public void checkTurnNextLevel( Position position  , Direction direction , int[][] figuresOnBoard  ) {
//		Log.d( "1 Position x " + position.x + " y " + position.y , "" + checkFieldIsEmpty( position , figuresOnBoard )  );
		Position positionToCheck = new Position( position.x , position.y ); 
		if( direction.direction == DIRECTION_X ) {
			positionToCheck.x = position.x + direction.value ;
		} 
		if( direction.direction == DIRECTION_Y ) {
			positionToCheck.y = position.y + direction.value ;	
		}
//		Log.d( "2 Position x " + positionToCheck.x + " y " + positionToCheck.y , "" + checkFieldIsEmpty( positionToCheck , figuresOnBoard )  );
//		Log.d( "3 direction.direction " + direction.direction + "direction.value " + direction.value , "" + checkFieldIsEmpty( positionToCheck , figuresOnBoard )  );
		int empty = checkFieldIsEmpty( positionToCheck , figuresOnBoard );
		if( empty == 1 ) {
			moves.add( positionToCheck );
			List<Position> directPos = new ArrayList<Position>();
			List<Direction> directions = new ArrayList<Direction>();
			if( direction.direction == DIRECTION_Y ) {
				if( direction.value == 1 ) {
					directPos.add(new Position( positionToCheck.x , positionToCheck.y  + 1));
					directions.add(new Direction( DIRECTION_Y , 1 ) );
				}
				else {
					directPos.add(new Position( positionToCheck.x , positionToCheck.y - 1));	
					directions.add(new Direction( DIRECTION_Y , -1 ) );
				}
				directPos.add(new Position( positionToCheck.x + 1 , positionToCheck.y ));
				directions.add(new Direction( DIRECTION_X , 1 ) );
				directPos.add(new Position( positionToCheck.x - 1, positionToCheck.y  ));
				directions.add(new Direction( DIRECTION_X , -1 ) );
			} 
			if( direction.direction == DIRECTION_X) {
				if( direction.value == 1 ) {
					directPos.add(new Position( positionToCheck.x + 1, positionToCheck.y  ));
					directions.add(new Direction( DIRECTION_X , 1 ) );
				}
				else {
					directPos.add(new Position( positionToCheck.x - 1, positionToCheck.y ));
					directions.add(new Direction( DIRECTION_X , -1 ) );
				}
				directPos.add(new Position( positionToCheck.x , positionToCheck.y  + 1));
				directions.add(new Direction( DIRECTION_Y , 1 ) );
				directPos.add(new Position( positionToCheck.x , positionToCheck.y - 1 ));
				directions.add(new Direction( DIRECTION_Y , -1 ) );
			}
			for( int i = 0 ; i < directions.size() ; i++ ) {
				if( checkFieldIsEmpty( directPos.get(i) , figuresOnBoard ) == 0 ) {
//					Log.d( " Position x " + directPos.get(i).x + " y " + directPos.get(i).y , "" + checkFieldIsEmpty( directPos.get(i) , figuresOnBoard )  );
//					Log.d( " Direction " + directions.get(i).direction , " value " + directions.get(i).value );
////					directions.get(i).value = - directions.get(i).value;
					checkTurnNextLevel( directPos.get(i)  , directions.get(i) , figuresOnBoard  );
				}	
			}
		} 
	}



	public class Direction {
		public int direction;
		public int value ;
		public Direction( int direction , int value ) {
			this.direction = direction;
			this.value     = value;
		}
	}
	
	public int checkFieldIsEmpty( Position position , int[][] figuresOnBoard ) {
		if( position.x < 0 || position.x > ChessBoard.CHESSBOARD_FIELDS_COUNT - 1 || position.y < 0 || position.y > ChessBoard.CHESSBOARD_FIELDS_COUNT - 1 ) 
			return -1;
		if( figuresOnBoard[position.x ][position.y] == -1 ) 
			return 1;
		return 0;
	}
	


}

