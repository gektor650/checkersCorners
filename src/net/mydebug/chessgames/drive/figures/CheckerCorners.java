package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;

import net.mydebug.chessgames.drive.ChessBoard;


public class CheckerCorners extends Checker {

	private List<Position>turns;
	ArrayList<Position> posiblePositions = new ArrayList<Position>();
	ArrayList<MoveDirection> posibleDirections = new ArrayList<MoveDirection>();


	public CheckerCorners(int color , int x , int y , ChessBoard board ) {
		super(color , x , y , board );
	}
	
	// Генерируем turns - куда может ходить шашка (позиции)
	private void generateTurnsByPosition( Position position ) {
		turns = new ArrayList<Position>();
		turns.add( new Position( position.x , position.y + 1 ) );
		turns.add( new Position( position.x , position.y - 1 ) );
		turns.add( new Position( position.x - 1 , position.y ) );
		turns.add( new Position( position.x + 1 , position.y ) );
	}
	
	//рекурсивно проверяем возможные ходы фигуры по заданному направлению
	public void checkTurnNextLevel( Position position  , MoveDirection direction  ) {
//		изменяем проверочную позицию по направлению, получаем поле через занятую клетку
		if( direction.direction == DIRECTION_X ) {
			position.x = position.x + direction.value ;
		} 
		if( direction.direction == DIRECTION_Y ) {
			position.y = position.y + direction.value ;	
		}
		int empty = ChessBoard.checkFieldIsEmpty( position );
		// Если поле свободно - добавляем его в возможные ходы и проверяем 3 направления (на возможность шагнуть еще через какую-то фигуру
		if( empty == 1 ) {
			posiblePositions.add( position );
			posibleDirections.add( direction );
			List<Position> directPos = new ArrayList<Position>();
			List<MoveDirection> directions = new ArrayList<MoveDirection>();
			
			// Определяем какие 3 клетки нам еще стоит проверить (соседние клетки, но не ту, откуда пришли)
			
			if( direction.direction == DIRECTION_Y ) {
				if( direction.value == 1 ) {
					directPos.add(new Position( position.x , position.y  + 1));
					directions.add(new MoveDirection( DIRECTION_Y , 1 ) );
				}
				else {
					directPos.add(new Position( position.x , position.y - 1));	
					directions.add(new MoveDirection( DIRECTION_Y , -1 ) );
				}
				directPos.add(new Position( position.x + 1 , position.y ));
				directions.add(new MoveDirection( DIRECTION_X , 1 ) );
				directPos.add(new Position( position.x - 1, position.y  ));
				directions.add(new MoveDirection( DIRECTION_X , -1 ) );
			} 
			if( direction.direction == DIRECTION_X) {
				if( direction.value == 1 ) {
					directPos.add(new Position( position.x + 1, position.y  ));
					directions.add(new MoveDirection( DIRECTION_X , 1 ) );
				}
				else {
					directPos.add(new Position( position.x - 1, position.y ));
					directions.add(new MoveDirection( DIRECTION_X , -1 ) );
				}
				directPos.add(new Position( position.x , position.y  + 1));
				directions.add(new MoveDirection( DIRECTION_Y , 1 ) );
				directPos.add(new Position( position.x , position.y - 1 ));
				directions.add(new MoveDirection( DIRECTION_Y , -1 ) );
			}
			// запускаем проверку для этих 3 клеток
			for( int i = 0 ; i < directions.size() ; i++ ) {
				if( ChessBoard.checkFieldIsEmpty( directPos.get(i) ) == 0 ) {
					checkTurnNextLevel( directPos.get(i)  , directions.get(i)  );
				}	
			}
		} 
	}

	

	@Override
	public List<Position> getAviableMoves() {
		posiblePositions = new ArrayList<Position>();
		posibleDirections = new ArrayList<MoveDirection>();
		generateTurnsByPosition( getPosition() );
		for( int i = 0 ; i < turns.size() ; i++ ) {
			int isEmpty = ChessBoard.checkFieldIsEmpty( turns.get(i) );
			//если в поле пусто добавляем возможность хода
			if( isEmpty == 1 ) {
				posiblePositions.add( turns.get(i) );
				posibleDirections.add( null );
			// проверяем можем ли мы перешагнуть через занятую клетку
			} else if( isEmpty == 0 ) {
				int direction;
				int value;
				//определяем направление, по которому надо проверить возможные ходы
				if( turns.get(i).x == this.x ) {
					value = -(this.y - turns.get(i).y); 
					direction = DIRECTION_Y;
				} else {
					value = -(this.x - turns.get(i).x) ;
					direction = DIRECTION_X;
				}
				// рекурсивно проверяем можем ли мы перешагнуть через занятую клетку
				checkTurnNextLevel(  turns.get(i) , new MoveDirection(direction, value) );
			}
		}
		return posiblePositions;
	}

	@Override
	public List<MoveDirection> getAviableDirections() {
		return posibleDirections;
	}

	


}

