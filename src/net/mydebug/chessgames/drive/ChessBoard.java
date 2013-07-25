package net.mydebug.chessgames.drive;


import java.util.ArrayList;
import java.util.List;


import android.graphics.Paint;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

import net.mydebug.chessgames.MainMenu;
import net.mydebug.chessgames.drive.figures.Ai;
import net.mydebug.chessgames.drive.figures.Figure;
import net.mydebug.chessgames.drive.figures.FigureData;
import net.mydebug.chessgames.drive.figures.MoveDirection;
import net.mydebug.chessgames.drive.figures.MoveLine;
import net.mydebug.chessgames.drive.figures.Position;

public abstract class ChessBoard  {
	private Pixmap chessBoardImage;
	protected Game game;
	
	protected List<Figure>   figures         = new ArrayList<Figure>();
	protected List<Position> tips            = new ArrayList<Position>();
	protected List<MoveLine> tipsLines 		 = new ArrayList<MoveLine>();
    protected Ai AiModel;
    protected History history;
	private float[] pixelToPositionX = new float[9];
	private float[] pixelToPositionY = new float[9];
	private final float RAW_BODY_SIZE     = 840;
	private final float RAW_BODY_PADDING  = 20;
	private final float RAW_BODY_FIELD    = 100;
	public final static int CHESSBOARD_FIELDS_COUNT = 8;
	public static int BOARD_SHOW_TIPS = 1;
	private int WHOSE_TURN = Figure.WHITE;
	protected final int ONE_PLAYER = 0;
	protected final int PLAYER_COLOR = Figure.WHITE;
	protected final int AI_COLOR     = Figure.BLACK;
	protected final int TWO_PLAYERS = 1;
	
	public Position aiTurnFrom = null;
	public Position aiTurnTo   = null;
	
	private int boardWidth;
	private int boardHeight;
	private float paddingX = 0;
	private float paddingY = 0;
	private float coefficient;
    
	private float fieldWidth;
	private float fieldHeight;
	private float figureWidth;	
	private float figureHeight;	
	private float figurePaddin;	
	
	protected int gameMode = ONE_PLAYER;
	
	protected int[][] figuresOnBoard = new int[CHESSBOARD_FIELDS_COUNT][CHESSBOARD_FIELDS_COUNT];
	protected int activeFigure = -1;
	
	public ChessBoard(Game game , boolean isNew) {
    	chessBoardImage = game.getGraphics().newPixmap("chessboard.png", PixmapFormat.RGB565 );
        this.game = game;

        activeFigure = -1;
      	boardWidth = game.getGraphics().getWidth();
    	paddingY   = ( game.getGraphics().getHeight() - boardWidth ) / 2; 
        
        coefficient = boardWidth / RAW_BODY_SIZE ;
        fieldWidth  = RAW_BODY_FIELD * coefficient;
        float startPointX  = RAW_BODY_PADDING * coefficient;
        float startPointY  = RAW_BODY_PADDING * coefficient + paddingY;
        for(int i = 0 ; i < pixelToPositionX.length ; i++) {
        	pixelToPositionX[i] = startPointX;
        	startPointX += fieldWidth;
        	pixelToPositionY[i] = startPointY;
        	startPointY += fieldWidth;
         }
        fieldHeight = fieldWidth;
        boardHeight = boardWidth;
        figureWidth = figureHeight = fieldWidth * 0.8f;
        figurePaddin = fieldWidth * 0.1f;
    	history = new History( game , isNew );
        if( isNew ) {
            this.initializeFigures();
            this.setFiguresOnBoard();
    		history.save(this);
        } else {
        	this.loadGameFromHistory();
			if( isGameOver() != -1 ) {
				gameOver();
			}
        }

    }
	
    /** Обрабатываем нажатие на экран
     * @param x - позиция по горизонтали
     * @param y - позиция по вертикали
     */
    public void touch( int x , int y ) {
    	// Если нажали на шахматную доску
    	if( y > paddingY && y < game.getGraphics().getHeight() - paddingY ) {
        	// устанавливаем значение поля выходящее за пределы, потом будем проверять если поле от 0 до 7 - значит кликнули на клетку
        	int fieldX = 99;
        	int fieldY = 99;
        	int i;
        	int fieldsCnt = pixelToPositionX.length - 1 ;
        	for( i = 0 ; i < fieldsCnt ; i++ ) {
        		if( pixelToPositionX[i] < x && pixelToPositionX[i+1] > x) {
        			fieldX = i;
        			break;
        		}
        	}
        	for( i = 0 ; i < fieldsCnt ; i++ ) {
        		if( pixelToPositionY[i] < y && pixelToPositionY[i+1] > y) {
        			fieldY = i;
        			break;
        		}
        	}
        	if( fieldX < CHESSBOARD_FIELDS_COUNT && fieldY < CHESSBOARD_FIELDS_COUNT ) {
    			press( fieldX , fieldY );
    			setFiguresOnBoard();
    			draw();
    			if( isGameOver() != -1 ) {
    				gameOver();
    			}
    			
        	}
        //если по вертикали мы попали в зону нижнего меню
    	} else if( y > game.getGraphics().getHeight() - 32 ) {
    		// Если нажали в левый нижний угол (иконка "ход назад")
    		if( x < 32 ) {
    			this.loadPrevFromHistory();
    		// Если нажали в нижний правый угол (иконка выйти в меню)
    		} else if( x > game.getGraphics().getWidth() - 32) {
				game.setScreen( new MainMenu( game ) );
    		}
    	}
 


    }
    
    public void gameOver() {
		String text = "";
		if( isGameOver() == Figure.WHITE ) {
			text = "White wins!";
		} else if( isGameOver() == Figure.BLACK ){
			text = "Black wins!";
		}
		game.getGraphics().drawRect( 0 , 0 , game.getGraphics().getHeight() - 32 , game.getGraphics().getWidth(), 0x77cccccc);
		game.getGraphics().drawText( "Game over! " , game.getGraphics().getWidth() /2 , game.getGraphics().getHeight() /2 - 10 , 20, 0xffff0000);
		game.getGraphics().drawText( text , game.getGraphics().getWidth() /2 , game.getGraphics().getHeight() /2 + 10 , 20, 0xffff0000);
    }
    
    
    public void loadPrevFromHistory() {
		ArrayList<FigureData> figureDatas = history.back( );
		if( figureDatas != null ) {
			clearTips();
    		setFiguresByFiguresData( figureDatas );
    		setFiguresOnBoard();
    		this.WHOSE_TURN = history.lastWhosTurn();
		}
		draw();	
    }

    public void clearTips() {
    	tipsLines  = new ArrayList<MoveLine>();
    	tips       = new ArrayList<Position>();
    	aiTurnFrom = null;
    	aiTurnTo   = null;
    }
    
	public void loadGameFromHistory() {
		ArrayList<FigureData> figureDatas = history.loadLastTurn( );
		if( figureDatas != null ) {
			setFiguresByFiguresData( figureDatas );
			setFiguresOnBoard();
    		this.WHOSE_TURN = history.lastWhosTurn();
		}	
	}
    
    public int getTurn() {
    	return WHOSE_TURN;
    }
    
    public void nextTurn() {
    	if( WHOSE_TURN == Figure.WHITE ) {
    		WHOSE_TURN = Figure.BLACK;
    	} else {
    		WHOSE_TURN = Figure.WHITE;
    	}
    }
    
    protected int getFigureIndexByField( int x , int y ) {
		return figuresOnBoard[x][y];
    }
    
    private int[][] getFiguresOnBoard() {
    	return figuresOnBoard;
    }
    
    public void move( int figureIndex , Position position) {
		figures.get( figureIndex ).setPosition( position.x, position.y);
		setFiguresOnBoard();
		tips      = new ArrayList<Position>();
		tipsLines = new ArrayList<MoveLine>();
		activeFigure = -1;
		nextTurn();
		if( this.gameMode == this.ONE_PLAYER ) {
    		if( WHOSE_TURN != PLAYER_COLOR ) {
    			AiModel.move();
    		} else {
	    		history.save( this );    		
    		}
		} else {
    		history.save( this );    		
    	}
    }

    public void setFigures( List<Figure> figures ) {
    	this.figures = figures;
    }
    
    protected void setFiguresOnBoard() {
        int i,j;
    	for( i = 0 ; i < CHESSBOARD_FIELDS_COUNT ; i++ ) {
    		for( j = 0 ; j < CHESSBOARD_FIELDS_COUNT ; j++ ) {
    			figuresOnBoard[i][j] = -1;
    		}
    	}
    	for( i = 0 ; i < figures.size() ; i++ ) {
    		figuresOnBoard[figures.get(i).getX()][figures.get(i).getY()] = i;
    	}
    }
    
    public void setAiTurnShowField( Position p1 , Position p2 ) {
    	aiTurnFrom = p1;
    	aiTurnTo   = p2;
    }

    public void draw() {
//        game.getGraphics().clear(0xff000000);
//        Pixmap chessBoardImage = game.getGraphics().newPixmap("13410109-vector-realistic-wood-texture-background-grey-color.jpg", Graphics.PixmapFormat.ARGB8888 );
        Pixmap chessBoardImage = game.getGraphics().newPixmap("950890_72529114.jpg", Graphics.PixmapFormat.ARGB8888 );
        game.getGraphics().drawPixmap(chessBoardImage, 0, 0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
    	this.drawBoard();
    	this.drawFigures();
    	this.drawTips();
        this.drawAiTurns();
        this.drawInfo();
    	this.drawBottomMenu();

//    	this.drawGrid();
    }
    
    
    private int getXPixel( int i ) {
    	return (int)pixelToPositionX[i] + 1;
    }
    
    private int getYPixel( int i ) {
    	return (int)pixelToPositionY[i] + 1;
    }
    
    private void drawGrid() {
    	for( int i = 0 ; i < pixelToPositionX.length ; i++ ) {
    		game.getGraphics().drawLine( (int)pixelToPositionX[0],(int) pixelToPositionY[i], (int)pixelToPositionX[ pixelToPositionY.length - 1  ], (int)pixelToPositionY[ i ], 0xcc0000ff );
    		game.getGraphics().drawLine( (int)pixelToPositionX[i],(int) pixelToPositionY[0], (int)pixelToPositionX[i], (int)pixelToPositionY[pixelToPositionY.length - 1], 0xcc0000ff );
    	}
    }


	private void drawTips() {
		if( BOARD_SHOW_TIPS == 1 ) {
			// Подсвечиваем поля возможного хода

			for( int i = 0 ; i < tips.size() ; i++ ) {
				highlightField( tips.get(i) , 0xcc00cc00 );	
				highlightFieldBorder( tips.get(i) ,0xff00ff00);
			}
            Graph graph = new Graph( tipsLines );
			// Рисуем линии, которые подсказывают траэкторию возможного хода
			for( int i = 0 ; i < tipsLines.size() ; i++ ) {		
//				System.out.print( String.valueOf( tipsLines.get(i).position1.x ) + ";" + String.valueOf( tipsLines.get(i).position1.y ) + " - " + String.valueOf( tipsLines.get(i).position2.x ) + ";" + String.valueOf( tipsLines.get(i).position2.y ) + "   ");
				game.getGraphics().drawLine( (int) (getXPixel( tipsLines.get(i).position1.x) + fieldHeight / 2  ), (int) (getYPixel( tipsLines.get(i).position1.y )  + fieldHeight / 2), (int)( getXPixel( tipsLines.get(i).position2.x ) + fieldHeight / 2 ), (int) (getYPixel( tipsLines.get(i).position2.y ) + fieldHeight / 2 ), 0xff00ff00 , 2 );
			}
//			System.out.println();

		}
	}
    
	private void highlightFieldBorder( Position position , int color ) {
		int x1 = getXPixel( position.x )  ;
		int x2 = (int)( getXPixel( position.x ) + fieldHeight )  ;
		int y1 = getYPixel( position.y ) ;
		int y2 = (int)( getYPixel( position.y ) + fieldHeight ) ;
		game.getGraphics().drawLine( x1 , y1 , x1 , y2 , color );
		game.getGraphics().drawLine( x2 , y1 , x2 , y2 , color );
		game.getGraphics().drawLine( x1 , y1 , x2 , y1 , color );
		game.getGraphics().drawLine( x1 , y2 , x2 , y2 , color );
	}


	private void highlightField( Position position , int color ) {
		game.getGraphics().drawRect( getXPixel( position.x )  , getYPixel( position.y ) , (int) fieldHeight,(int) fieldHeight, color );
	}
	
    private void drawBoard() {
    	game.getGraphics().drawPixmap( chessBoardImage , paddingX , paddingY , boardWidth , boardHeight );
    }
    
    private void drawFigures() {
    	int len = figures.size();
    	for( int i = 0 ; i < len ; i++ ) {
    		Figure figure = figures.get(i);
    		Pixmap pixmap;
    		if( i == activeFigure && getTurn() == figures.get(activeFigure).getColor() ) {
				highlightField( figure.getPosition() ,0x33cccccc);
				highlightFieldBorder( figure.getPosition() ,0xffcccccc);
				pixmap = game.getGraphics().newPixmap( figure.getActiveImage() , PixmapFormat.ARGB8888 );
    		} else {
        		pixmap = game.getGraphics().newPixmap( figure.getImage() , PixmapFormat.ARGB8888 );
    		}
    		game.getGraphics().drawPixmap( pixmap  , getXPixel(figure.getX()) + figurePaddin , getYPixel(figure.getY()) + figurePaddin , figureWidth , figureHeight );
    	}
    }
    
    private void drawAiTurns() {
    	if( aiTurnFrom != null ) {
    		highlightField( aiTurnFrom ,0x11cccccc);
    		highlightFieldBorder( aiTurnFrom ,0xffcccccc);    		
    	}

		if( aiTurnTo != null ) {
			highlightField( aiTurnTo ,0x33ee0000);
			highlightFieldBorder( aiTurnTo ,0xffff0000);    		
		}

    }
    
    public int getBoardLength() {
    	return CHESSBOARD_FIELDS_COUNT;
    }
    
    protected void drawInfo() {
		game.getGraphics().drawText( "Moves сount: " + String.valueOf( history.getTurn() )  ,  32 , game.getGraphics().getHeight() - 10 , 20 , 0xff000000 , Paint.Align.LEFT );
		game.getGraphics().drawText( "Move "  ,  game.getGraphics().getWidth() / 2 , 20 , 20 , 0xff000000 , Paint.Align.RIGHT );
		Pixmap pixmap;
		if( WHOSE_TURN == 0 ) 
			pixmap = game.getGraphics().newPixmap( "checkerBlack.png" , PixmapFormat.RGB565 );
		else
			pixmap = game.getGraphics().newPixmap( "checkerWhite.png" , PixmapFormat.RGB565 );	
		game.getGraphics().drawPixmap( pixmap  , game.getGraphics().getWidth() / 2 , 0 , 32 , 32 );
    }
    
    protected void drawBottomMenu() {
    	Pixmap pixmap;
    	if( history.getTurn() > 1) {
        	pixmap = game.getGraphics().newPixmap( "go-back-icon-32.png" , PixmapFormat.RGB565 );
    		game.getGraphics().drawPixmap( pixmap  , 0 , game.getGraphics().getHeight() - 32 );
    	}
    	

    	pixmap = game.getGraphics().newPixmap( "Close-2-icon-32.png" , PixmapFormat.RGB565 );
		game.getGraphics().drawPixmap( pixmap  , game.getGraphics().getWidth() - 32 , game.getGraphics().getHeight() - 32 );
		
    }
    
    public List<Figure> getFigures() {
    	return this.figures;
    }
	// -1 - за предлелами поля, 0 - пусто, 1 - занято
	public int checkFieldIsEmpty(Position position ) {
		if( position.x < 0 || position.x > ChessBoard.CHESSBOARD_FIELDS_COUNT - 1 || position.y < 0 || position.y > ChessBoard.CHESSBOARD_FIELDS_COUNT - 1 ) 
			return -1;
		if( figuresOnBoard[position.x ][position.y] == -1 ) 
			return 1;
		return 0;
	}

	public void setTips(List<Position> aviableMoves) {
		this.tips = aviableMoves;
	} 

	public void setTipsLines(List<MoveLine> aviableDirectionsLines) {
		this.tipsLines = aviableDirectionsLines;
	}
	
    protected abstract void initializeFigures();
    protected abstract void buildTips( int figureIndex , int x , int y );
    protected abstract void press( int x , int y );
    protected abstract void setFiguresByFiguresData( ArrayList<FigureData> figureData );
    // return color fins figures or -1 when game not over
    protected abstract int isGameOver();


    



}
