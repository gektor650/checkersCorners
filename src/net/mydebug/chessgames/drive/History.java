package net.mydebug.chessgames.drive;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.mydebug.chessgames.drive.figures.Figure;
import net.mydebug.chessgames.drive.figures.Position;

public class History implements HistoryInterface {
	List<Figure> figures = new ArrayList<Figure>();
	int 		  turnId = 0;
	int 	      gameId = 0;
	
	@Override
	public void add(  List<Figure> figures  ) {
		// TODO Auto-generated method stub
		this.figures = figures;
		save();
		turnId++;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		for( int i = 0 ; i < figures.size() ; i++ ) {

		}

	}

	@Override
	public List<Figure> load() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Figure> back() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected class HistoryRow {
		Position positionFrom ;
		Position positionTo   ;
		Figure   figure ;
	}
	
	public static byte[] getBytes(Object obj) throws java.io.IOException {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(bos);
	    oos.writeObject(obj);
	    oos.flush();
	    oos.close();
	    bos.close();
	    byte[] data = bos.toByteArray();
	    return data;
	}

}
