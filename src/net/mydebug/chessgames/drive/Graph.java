package net.mydebug.chessgames.drive;

import net.mydebug.chessgames.drive.figures.MoveLine;

import java.util.ArrayList;
import java.util.List;

/**
 *  Его Величество Граф
 *  С его помощью мы вычленим из отрезков всех возможных ходов фигуры кратчайший маршрут фигуры
 *  @author gektor650
 * */

 public class Graph {


    public int[][] graph      = null;
    List<Integer> graphVertex = new ArrayList<Integer>();

    /**
     * Инициализируем - строим наш граф и заполняем его связи по одиночным отрезкам
     * @param lines - Массив одиночных отрезков
     */
    public Graph( List<MoveLine> lines ) {
        if( lines.size() < 1 ) {
            return;
        }
        int i,j,k;

        //Определяем для каждой связи номер поля на шахматной доске ( считаем что на доске 64 поля(ChessBoard.CHESSBOARD_FIELDS_COUNT*ChessBoard.CHESSBOARD_FIELDS_COUNT))
        int[] iToFieldNumberBeg = new int[ lines.size() ];
        int[] iToFieldNumberEnd = new int[ lines.size() ];
        for( i = 0 ; i < lines.size() ; i++ ) {
            iToFieldNumberBeg[i] = lines.get(i).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition1().getX();
            iToFieldNumberEnd[i] = lines.get(i).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition2().getX();
            if( ! graphVertex.contains( iToFieldNumberBeg[i] )) {
                graphVertex.add( iToFieldNumberBeg[i] );
            }
            if( ! graphVertex.contains( iToFieldNumberEnd[i] )) {
                graphVertex.add( iToFieldNumberEnd[i] );
            }

        }

        //Определяем размер графа - двумерный массив величиною с количество вершин
        graph = new int[ graphVertex.size() ][ graphVertex.size() ];

        //Заполняем наш граф большими значениями - считаем что растояние между точек изначально максимальное
        for( i = 0 ; i < graph.length ; i++ ) {
            for( j = 0 ; j < graph.length ; j++ ) {
                graph[i][j] = 9999999;
            }
        }

        // Теперь добавляем в наш граф информацию о отношениях вершин ( т.к. у нас растояние между вершинами одинаковое, то значение 1 - длинна )
        int tmp1,tmp2,tmp3,tmp4;
        int commonVertex = -1, vertex1 = -1, vertex2 = -1;
        for( i = 0 ; i < lines.size() ; i++ ) {
            //Переводим координаты поля в номер фигуры (0 - 63 для стандартной шахматной доски)
            tmp1 = lines.get(i).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition1().getX();
            tmp2 = lines.get(i).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(i).getPosition2().getX();
            for( j = 0 ; j < lines.size() ; j++ ) {
                if( i == j ) continue;
                commonVertex = -1;
                vertex1 = -1;
                vertex2 = -1;
                //Переводим координаты поля в номер фигуры (0 - 63 для стандартной шахматной доски)
                tmp3 = lines.get(j).getPosition1().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(j).getPosition1().getX();
                tmp4 = lines.get(j).getPosition2().getY() * ChessBoard.CHESSBOARD_FIELDS_COUNT + lines.get(j).getPosition2().getX();
//               Ищем пересечения линий
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
                //По дефолту - общих вершин нет
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
                    // Если есть общая вершина - отмечаем отрезки на графе с растоянием в одну клетку (все линии у нас длинною в 1)
                    if( commonVertexIndex >= 0 ) {
                        graph[commonVertexIndex][vertex1Index] = 1;
                        graph[commonVertexIndex][vertex2Index] = 1;
                        graph[vertex1Index][commonVertexIndex] = 1;
                        graph[vertex2Index][commonVertexIndex] = 1;
                    }
                }
            }
        }

    }


    public List<MoveLine> getShortestRoute( int x1 , int y1 , int x2 , int y2  ) {
        List<Integer> path   = new ArrayList<Integer>();
        List<MoveLine> lines = new ArrayList<MoveLine>();

        if( graph == null || graph.length < 1 ) return lines;

        //Переводим координаты поля в номер фигуры (0 - 63 для стандартной шахматной доски)
        int position1 = y1 * ChessBoard.CHESSBOARD_FIELDS_COUNT + x1;
        int position2 = y2 * ChessBoard.CHESSBOARD_FIELDS_COUNT + x2;

        int v,i,l;
        int p = graph.length;
        int s = -1;
        int t = -1;

        // Находим индекс вешины для начальной и конечной точки пути
        for( i = 0 ; i < graphVertex.size() ; i++ ) {
            if( position1 == graphVertex.get(i) ) {
                s = i;
            }
            if( position2 == graphVertex.get(i) ) {
                t = i;
            }
        }

        // Алгорит Дейкстры по поиску кратчайшего пути по вершинам графа
        // честно спер его с http://dtf.ru/articles/read.php?id=57085
        int[] H = new int[p];
        int[] T = new int[p];
        int[] X = new int[p];
        for( i = 0 ; i < p ; i++ ) {
            T[i] = 999999;
            X[i] = 0;
        }
        H[s] = -1;
        T[s] = 0;
        X[s] = 1;
        v    = s;
        while ( true ) {
            for ( i = 0 ; i < p ; i++ ) {
                if( X[i] == 0 && T[i] > ( T[v] + graph[v][i]) ) {
                    T[i] = T[v] + graph[v][i];
                    H[i] = v;
                }
            }
            l = 9999999;
            v = 0;
            for( i = 0 ; i < p ; i++ ){
                if( X[i] == 0 && T[i] < l ) {
                    v = i;
                    l = T[i];
                }
            }
            if( v == 0 ) {
                //Путь не найден
                break;
            } else if( v == t ) {
                //Путь найден
                break;
            } else {
                X[v] = 1;
            }
        }

        // Обрабатываем результат вершин H и находим искомый путь
        i = 0;
        while( t != s && t > 0 ) {
            path.add( graphVertex.get(t) );
            t = H[t];
            // если вдруг что-то пойдет не так и путь задан ошибочно - прерываем цикл
            if( i++ > graphVertex.size() ) break;
        }
        path.add( graphVertex.get(s) );
        // Переделываем вершины графа в отрезки с началькой точкой и конечной
        for( i = 1 ; i < path.size() ; i++ ) {
            lines.add( new MoveLine(
                    path.get(i-1) % ChessBoard.CHESSBOARD_FIELDS_COUNT ,
                    (int) path.get(i-1) / ChessBoard.CHESSBOARD_FIELDS_COUNT ,
                    path.get(i) % ChessBoard.CHESSBOARD_FIELDS_COUNT ,
                    (int) path.get(i) / ChessBoard.CHESSBOARD_FIELDS_COUNT
            ) );
        }

        return lines;
    }




}
