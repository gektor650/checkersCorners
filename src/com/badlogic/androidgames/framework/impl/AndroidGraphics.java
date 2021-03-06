package com.badlogic.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;


import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class AndroidGraphics implements Graphics {
    private AssetManager assets;
    private Bitmap frameBuffer;
    private Canvas canvas;
    private Paint paint;
    private Rect srcRect = new Rect();
    private Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inScaled = false;
        options.inPreferredConfig = Config.ARGB_8888;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in , null , options );
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }
    
	
	@Override
	public void drawLine(int x, int y, int x2, int y2, int color , int width ) {
		paint.setColor(color);
		paint.setStrokeWidth(width);
		canvas.drawLine(x, y, x2, y2, paint);
	}
	
    @Override
    public void drawCircle(int x, int y, int radius,  int color) {
        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(2);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,
                null);
    }
    
    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
    }
    

    
    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y , float scaleWidth , float scaleHeight ) {
    	scaleWidth  =  scaleWidth  / pixmap.getWidth();
    	scaleHeight =  scaleHeight / pixmap.getHeight() ;
    	Matrix matrix = new Matrix();
    	matrix.postScale(scaleWidth, scaleHeight );
    	matrix.postTranslate(x, y );
    	Paint paint = new Paint();
    	paint.setAntiAlias(true);
    	paint.setFilterBitmap(true);
    	paint.setDither(true);
    	canvas.drawBitmap( ((AndroidPixmap)pixmap).bitmap , matrix, paint);

    }
    
   @Override 
   public void drawText( String text , int x , int y , int size , int color ) {
	   Typeface font = Typeface.createFromAsset( assets , "ubuntu-font-family/Ubuntu-BI.ttf");
       Paint paint = new Paint();
	   paint.setTypeface( font );
//	   paint.setTypeface( Typeface.MONOSPACE );
	   paint.setTextSize( size );
       paint.setColor(color);
       paint.setAntiAlias( true );
       paint.setTextAlign( Paint.Align.CENTER );
       paint.setShadowLayer(5.0f, 0, 0, 0xffffffff );
       canvas.drawText( text , x , y, paint );
   }
   
   @Override 
   public void drawText( String text , int x , int y , int size , int color , Align align ) {
	   Typeface font = Typeface.createFromAsset( assets , "ubuntu-font-family/Ubuntu-BI.ttf");
       Paint paint = new Paint();
       paint.setTypeface( font );
//       paint.setTypeface( Typeface.MONOSPACE );
       paint.setTextSize( size );
	   paint.setColor(color);
	   paint.setAntiAlias( true );
	   paint.setTextAlign( align );
       paint.setShadowLayer(5.0f, 0, 0, 0xffffffff );
	   canvas.drawText( text , x , y, paint );
   }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void drawColor( int color ) {
        canvas.drawColor( color , PorterDuff.Mode.CLEAR );
    }
}
