package net.mydebug.chessgames.drive;

import net.mydebug.chessgames.drive.figures.MoveLine;

import java.util.ArrayList;
import java.util.List;


public class Graph {


    public int[][] graph;
    List<MoveLine> lines;

    public Graph( List<MoveLine> lines ) {
        if( true ) return;
        this.lines = lines;
        if( lines.size() < 2 ) {
            return;
        }

        int i,j,k,l;

        List<Integer> graphCnt = new ArrayList<Integer>();

        //Определяем для каждой связи номер поля на шахматной доске ( считаем что на доске 64 поля)
        int[] iToFieldNumberBeg = new int[ lines.size() ];
        int[] iToFieldNumberEnd = new int[ lines.size() ];
        for( i = 0 ; i < lines.size() ; i++ ) {

            iToFieldNumberBeg[i] = lines.get(i).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition1().getX();
            iToFieldNumberEnd[i] = lines.get(i).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition2().getX();
            if( ! graphCnt.contains( iToFieldNumberBeg[i] )) {
                graphCnt.add( iToFieldNumberBeg[i] );
            }
            if( ! graphCnt.contains( iToFieldNumberEnd[i] )) {
                graphCnt.add( iToFieldNumberBeg[i] );
            }

        }

        //  Определяем размер графа
        graph = new int[ graphCnt.size() ][ graphCnt.size() ];

        //Заполняем наш граф нулевыми значениями
        for( i = 0 ; i < graph.length ; i++ ) {
            for( j = 0 ; j < graph.length ; j++ ) {
                graph[i][j] = 0;
            }
        }


        for( i = 0 ; i < lines.size() ; i++ ) {
            System.out.println( lines.get(i).getPosition1().getX() + ";" + lines.get(i).getPosition1().getY() + "   " + lines.get(i).getPosition2().getX() + ";" + lines.get(i).getPosition2().getY() );
            System.out.println("");
        }

            // Эти переменные используем для добавления единички в матрицу графа
        int a = 0,b =0;
        // Теперь добавляем в наш граф информацию о отношениях вершин ( т.к. у нас растояние между вершинами одинаковое, то значение 1 - длинна )
        int tmp1,tmp2,tmp3,tmp4;
        int commonVertex = -1, vertex1 = -1, vertex2 = -1;
        for( i = 0 ; i < lines.size() ; i++ ) {
            tmp1 = lines.get(i).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition1().getX();
            tmp2 = lines.get(i).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition2().getX();
            for( j = 0 ; j < lines.size() ; j++ ) {
                commonVertex = -1;
                vertex1 = -1;
                vertex2 = -1;
                tmp3 = lines.get(j).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(j).getPosition1().getX();
                tmp4 = lines.get(j).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(j).getPosition2().getX();
                if( tmp1 == tmp3 ) {
                    commonVertex = tmp1;
                    vertex1      = tmp2;
                    vertex2      = tmp4;
                }
                if( tmp2 == tmp3 ) {
                    commonVertex = tmp2;
                    vertex1      = tmp1;
                    vertex2      = tmp4;
                }
                if( tmp1 == tmp4 ) {
                    commonVertex = tmp1;
                    vertex1      = tmp2;
                    vertex2      = tmp3;
                }
                if( tmp2 == tmp4 ) {
                    commonVertex = tmp2;
                    vertex1      = tmp1;
                    vertex2      = tmp3;
                }
                int commonVertexIndex = -1;
                int vertex1Index      = -1;
                int vertex2Index      = -1;
                if( commonVertex >= 0 ) {
                    for( k = 0 ; k < graphCnt.size() ; k++ ) {
                        if( graphCnt.get(k) == commonVertex ) {
                            commonVertexIndex = k;
                        }
                        if( graphCnt.get(k) == vertex1 ) {
                            vertex1Index = k;
                        }
                        if( graphCnt.get(k) == vertex2 ) {
                            vertex2Index = k;
                        }
                    }
                    if( commonVertexIndex >= 0 ) {
                        graph[commonVertexIndex][vertex1Index] = 1;
                        graph[commonVertexIndex][vertex2Index] = 1;
                    }
                }
            }
        }

        for( i = 0 ; i < graph.length ; i++ ) {
            for( j = 0 ; j < graph.length ; j++ ) {
                System.out.print( graph[i][j] );
            }
            System.out.println();
        }



    }

    public void getShortestRoute() {
        if( true ) return;
        if( lines.size() == 0 ) return;
        // Алгорит Дейкстры по поиску кратчайшего пути по вершинам графа
        int i,j,v,u,l;
        int[] H = new int[graph.length];

        int[] T = new int[graph.length];
        int[] X = new int[graph.length];
        for( i = 0 ; i < lines.size() ; i++ ) {
            T[i] = 999999;
            X[i] = 0;
        }
        H[0] = 0;
        T[0] = 0;
        X[0] = 1;
        v    = 0;

        int t = graph.length - 1;
        while ( true ) {
            for ( i = 0 ; i < graph.length ; i++ ) {
                if( X[i] == 0 && T[i] > ( T[v] + graph[v][i]) ) {
                    T[i] = T[v] + graph[v][i];
                    H[i] = v;
                }
            }
            l = 9999999;
            v = 0;
            for( i = 0 ; i < graph.length ; i++ ){
                if( X[i] == 0 && T[i] < l ) {
                    v = i;
                    l = T[i];
                }
            }
            if( v == 0 ) {
                System.out.println("НЕТ ПУТИ");
                break;
            } else if( v == t ) {
                System.out.println( "Путь найден" );
                break;
            } else {
                X[v] = 1;
            }
        }

        for ( i = 0 ; i < H.length ; i++ ) {
            System.out.println( H[i] );
        }

    }




}
