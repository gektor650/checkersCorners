package net.mydebug.chessgames.drive;

import java.util.ArrayList;
import java.util.List;



import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import net.mydebug.chessgames.drive.figures.Figure;
import net.mydebug.chessgames.drive.figures.FigureData;
import net.mydebug.chessgames.drive.db.HistoryDb;

/**
 * @author gektor650
 *  Класс используется для сохранения позиций фигур на шахматном поле и восстановления предыдущего хода
 */
public class History implements HistoryInterface {
	List<Figure> figures = new ArrayList<Figure>();
	int 		  turnId = 0;
	int 	      gameId = 1;
	Game 		   game ;
	HistoryDb historyDb;
	
	public History( Game game , boolean isNew ) {
		this.game = game;
		historyDb = new HistoryDb( game.getActivity() );
		if( isNew )
			historyDb.clearAll();
		else
			turnId = historyDb.getLastTurnId( gameId );
	}
	

	@Override
	public void save( ChessBoard board ) {
		figures = board.getFigures();
		ArrayList<FigureData> data = new ArrayList<FigureData>();
		for( int i = 0 ; i < figures.size() ; i++ ) {
			data.add( new FigureData( figures.get(i).getX(), figures.get(i).getY(), figures.get(i).getColor(), figures.get(i).getType() ) );
		}

		byte[] savedData = Serialize.serialize( data );

		historyDb.addTurn( gameId, ++turnId, savedData , board.getTurn() ); 
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList<FigureData> back( ) {
		if( turnId <= 1 ) return null; 
		historyDb.removeLastTurn();
		@SuppressWarnings("unchecked")
		ArrayList<FigureData>  data = (ArrayList)Serialize.deserialize( historyDb.getTurn( gameId , --turnId ) );
		return data;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList<FigureData> loadLastTurn( ) {
		if( turnId <= 1 ) return null; 
		@SuppressWarnings("unchecked")
		ArrayList<FigureData>  data = (ArrayList)Serialize.deserialize( historyDb.getTurn( gameId , turnId ) );
		return data;
	}

	@Override
	public int lastWhosTurn( ) {
		int whosTurn = 0;
		whosTurn = historyDb.getLastWhosTurn( gameId , turnId );
		return whosTurn;
	}



	@Override
	public int getTurn() {
		return turnId;
	}
	

}
