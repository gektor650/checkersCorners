package net.mydebug.chessgames.drive;

import net.mydebug.chessgames.drive.figures.MoveLine;

import java.util.List;


public class Graph {


    public int[][] graph;

    public Graph( List<MoveLine> lines ) {
        if( lines.size() < 2 ) {
            return;
        }

        int i,j;
        //  Определяем размер графа
        graph = new int[ lines.size() ][ lines.size() ];

        //Заполняем наш граф нулевыми значениями
        for( i = 0 ; i < graph.length ; i++ ) {
            for( j = 0 ; j < graph.length ; j++ ) {
                graph[i][j] = 0;
            }
        }

        //Определяем для каждой связи номер поля на шахматной доске ( считаем что на доске 64 поля)
        int[] iToFieldNumberBeg = new int[ lines.size() ];
        int[] iToFieldNumberEnd = new int[ lines.size() ];
        for( i = 0 ; i < lines.size() ; i++ ) {
            iToFieldNumberBeg[i] = lines.get(i).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition1().getX();
            iToFieldNumberEnd[i] = lines.get(i).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition2().getX();
        }


            // Эти переменные используем для добавления единички в матрицу графа
        int a = 0,b =0;
        // Теперь добавляем в наш граф информацию о отношениях вершин ( т.к. у нас растояние между вершинами одинаковое, то значение 1 - длинна )
        for( i = 0 ; i < lines.size() ; i++ ) {
            for( j = 0 ; j < lines.size() ; j++ ) {
                if( i == j ) continue;
                if(
                        lines.get(i).getPosition1().getX() == lines.get(j).getPosition1().getX() &&
                        lines.get(i).getPosition1().getY() == lines.get(j).getPosition1().getY() ||
                        lines.get(i).getPosition2().getX() == lines.get(j).getPosition2().getX() &&
                        lines.get(i).getPosition2().getY() == lines.get(j).getPosition2().getY() ||
                        lines.get(i).getPosition1().getX() == lines.get(j).getPosition2().getX() &&
                        lines.get(i).getPosition1().getY() == lines.get(j).getPosition2().getY() ||
                        lines.get(i).getPosition2().getX() == lines.get(j).getPosition1().getX() &&
                        lines.get(i).getPosition2().getY() == lines.get(j).getPosition1().getY()

                        )
                graph[i][j] = 1;
            }
        }

        for( i = 0 ; i < graph.length ; i++ ) {
            for( j = 0 ; j < graph.length ; j++ ) {
                System.out.print( graph[i][j] );
            }
            System.out.println();
        }

    }




}
