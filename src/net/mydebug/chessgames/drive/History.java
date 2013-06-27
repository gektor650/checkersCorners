package net.mydebug.chessgames.drive;

import java.util.ArrayList;
import java.util.List;



import com.badlogic.androidgames.framework.Game;
import net.mydebug.chessgames.drive.figures.Figure;
import net.mydebug.chessgames.drive.figures.FigureData;
import net.mydebug.chessgames.drive.db.HistoryDb;

public class History implements HistoryInterface {
	List<Figure> figures = new ArrayList<Figure>();
	int 		  turnId = 0;
	int 	      gameId = 7;
	Game 		   game ;
	HistoryDb historyDb;
	
	public History( Game game ) {
		this.game = game;
		historyDb = new HistoryDb( game.getActivity() );
	}
	


	@Override
	public void save( ChessBoard board ) {

		figures = board.getFigures();
		ArrayList<byte[]> data = new ArrayList<byte[]>();
		for( int i = 0 ; i < figures.size() ; i++ ) {
			data.add( figures.get(i).getSerialized() );
		}

		byte[] savedData = Serialize.serialize( data );

		historyDb.addTurn( gameId, ++turnId, savedData ); 
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList<FigureData> back( ChessBoard board ) {
		if( turnId <= 1 ) return null; 
		historyDb.removeLastTurn();
		@SuppressWarnings("unchecked")
		ArrayList<byte[]>  data = (ArrayList)Serialize.deserialize( historyDb.getTurn( gameId , --turnId ) );
		ArrayList<FigureData>   figures         = new ArrayList<FigureData>();
		for( int i = 0 ; i < data.size() ; i++) {
			FigureData figureData = (FigureData)Serialize.deserialize( data.get(i) );
			figures.add( figureData );
		}
		return figures;
	}



	@Override
	public int getTurn() {
		return turnId;
	}
	

}
