package com.badlogic.androidgames.framework;

import android.graphics.Paint.Align;

public interface Graphics {
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);

    public void clear(int color);

    public void drawPixel(int x, int y, int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);
    
    public void drawCircle(int x, int y, int radius,  int color);

    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);
    
    public void drawPixmap(Pixmap pixmap, float x, float y , float scaleWidth , float scaleHeight );

    public void drawPixmap(Pixmap pixmap, int x, int y);
    
    public void drawText(String text , int x, int y , int size , int color);

    
    public void drawText(String text , int x, int y , int size , int color , Align align );
    
    public int getWidth();

    public int getHeight();

    public void drawLine(int x, int y, int x2, int y2, int color, int width);

    public void drawColor( int color );
}
