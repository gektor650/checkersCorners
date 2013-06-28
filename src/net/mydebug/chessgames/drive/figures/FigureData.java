package net.mydebug.chessgames.drive.figures;

import java.io.Serializable;

/**
 * @author gektor650
 * Класс используется для серилизации фигуры (цвет, позиция и тип), для дальнейшего хранения в базе данных
 */
public class FigureData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	public int color;
	public int type;
	public FigureData( int x , int y , int color , int type ) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.type  = type;
	}
}