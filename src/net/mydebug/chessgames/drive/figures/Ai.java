package net.mydebug.chessgames.drive.figures;

import java.util.ArrayList;
import java.util.List;

public interface Ai extends Runnable {
	public void move();
	
	class FigureAndPosition {
        public FigureAndPosition() {}

        public FigureAndPosition( Position position , int figureIndex , int weight ) {

        }
		public Position position;
		public int figureIndex;
        protected List<Integer> weight = new ArrayList<Integer>();
        protected int weightInt = -99;

        private void buildWeight( int depth ) {
            if( this.weight.size() <= depth ) {
                for ( int i = this.weight.size() ; i <= depth ; i++ ) {
                    this.weight.add(-999);
                }
            }
        }

        public void setWeight( int weight , int checkDepth ) {
            buildWeight( checkDepth );
            this.weight.set( checkDepth , weight );
        }

        public int getWeight(  int checkDepth ) {
            buildWeight( checkDepth );
            return this.weight.get( checkDepth );
        }
	}
}
