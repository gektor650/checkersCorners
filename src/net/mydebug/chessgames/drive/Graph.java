package net.mydebug.chessgames.drive;

import net.mydebug.chessgames.drive.figures.MoveLine;

import java.util.ArrayList;
import java.util.List;


public class Graph {


    public int[][] graph;
    List<MoveLine> lines;
    List<Integer> graphVertex = new ArrayList<Integer>();

    public Graph( List<MoveLine> lines ) {
        this.lines = lines;
        if( lines.size() < 1 ) {
            return;
        }

        int i,j,k,l;


        //Определяем для каждой связи номер поля на шахматной доске ( считаем что на доске 64 поля)
        int[] iToFieldNumberBeg = new int[ lines.size() ];
        int[] iToFieldNumberEnd = new int[ lines.size() ];
        for( i = 0 ; i < lines.size() ; i++ ) {
            iToFieldNumberBeg[i] = lines.get(i).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition1().getX();
            iToFieldNumberEnd[i] = lines.get(i).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition2().getX();
            if( ! graphVertex.contains( iToFieldNumberBeg[i] )) {
                graphVertex.add( iToFieldNumberBeg[i] );
                System.out.println( "logBeg: " + iToFieldNumberBeg[i] );
            }
            if( ! graphVertex.contains( iToFieldNumberEnd[i] )) {
                graphVertex.add( iToFieldNumberEnd[i] );
                System.out.println( "logEnd: " + iToFieldNumberEnd[i] );
            }

        }

        //  Определяем размер графа
        graph = new int[ graphVertex.size() ][ graphVertex.size() ];

        //Заполняем наш граф нулевыми значениями
        for( i = 0 ; i < graph.length ; i++ ) {
            for( j = 0 ; j < graph.length ; j++ ) {
                graph[i][j] = 9999999;
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
                if( i == j ) continue;
                commonVertex = -1;
                vertex1 = -1;
                vertex2 = -1;
                tmp3 = lines.get(j).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(j).getPosition1().getX();
                tmp4 = lines.get(j).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(j).getPosition2().getX();
                System.out.println("tmp1:"+tmp1+" tmp2:"+tmp2+" tmp3:"+tmp3+" tmp4:"+tmp4);
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
                    for( k = 0 ; k < graphVertex.size() ; k++ ) {
                        if( graphVertex.get(k) == commonVertex ) {
                            commonVertexIndex = k;
                        }
                        if( graphVertex.get(k) == vertex1 ) {
                            vertex1Index = k;
                        }
                        if( graphVertex.get(k) == vertex2 ) {
                            vertex2Index = k;
                        }
                    }
                    if( commonVertexIndex >= 0 ) {
                        graph[commonVertexIndex][vertex1Index] = 1;
                        graph[commonVertexIndex][vertex2Index] = 1;
                        graph[vertex1Index][commonVertexIndex] = 1;
                        graph[vertex2Index][commonVertexIndex] = 1;
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


    public void getShortestRoute( int x1 , int y1 , int x2 , int y2  ) {
        if( lines.size() < 1 ) return;
        int position1 = y1 * ChessBoard.CHESSBOARD_FIELDS_COUNT + x1;
        int position2 = y2 * ChessBoard.CHESSBOARD_FIELDS_COUNT + x2;

        int v,u,l;
        int p = graph.length;
        int s = -1;
        int t = -1;

        for( u = 0 ; u < graphVertex.size() ; u++ ) {
            if( position1 == graphVertex.get(u) ) {
                s = u;
            }
            if( position2 == graphVertex.get(u) ) {
                t = u;
            }
        }

        System.out.println( "s:" + s + " t:" + t);
        System.out.println( "p1:" + position1 + " p2:" + position2 );
        // Алгорит Дейкстры по поиску кратчайшего пути по вершинам графа
        int[] H = new int[p];

        int[] T = new int[p];
        int[] X = new int[p];
        for( u = 0 ; u < p ; u++ ) {
            T[u] = 999999;
            X[u] = 0;
        }
        H[s] = 0;
        T[s] = 0;
        X[s] = 1;
        v    = s;

        while ( true ) {
            for ( u = 0 ; u < p ; u++ ) {
                if( X[u] == 0 && T[u] > ( T[v] + graph[v][u]) ) {
                    T[u] = T[v] + graph[v][u];
                    H[u] = v;
                }
            }
            l = 9999999;
            v = 0;
            for( u = 0 ; u < p ; u++ ){
                if( X[u] == 0 && T[u] < l ) {
                    v = u;
                    l = T[u];
                }
            }
            if( v == 0 ) {
                System.out.println("НЕТ ПУТИ");
                break;
            } else if( v == t ) {
                System.out.println("VERTEX:" + v);
                System.out.println( "Путь найден" );
                break;
            } else {
                X[v] = 1;
            }
        }

        for ( u = 0 ; u < H.length ; u++ ) {
            System.out.println( "H[" + u + "]: " + H[u] + " - gr:" + graphVertex.get(u) );
        }
        for ( u = 0 ; u < T.length ; u++ ) {
            System.out.println( "T[" + u + "]: " + T[u] + " - gr:" + graphVertex.get(u) );
        }

    }




}
