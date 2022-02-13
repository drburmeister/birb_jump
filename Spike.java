package BirbSpike;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Spike extends Sprite {
	  Color color = Color.BLACK;
	  
	  void render(GraphicsContext gc)
	  {
	    if (visible)
	    {
	      gc.setFill(color);
	    }
	  }
		public static void fillEqTriangle(int x, int y, int size, int angle, GraphicsContext gc) {
			if(angle == 0) gc.fillPolygon(new double[] {x, x, x+size}, new double[] {y,y+size,y+size/2}, 3);
			else if(angle == 90)  gc.fillPolygon(new double[] {x, x+size, x+size/2}, new double[] {y,y,y-size}, 3);
			if(angle == 180) gc.fillPolygon(new double[] {x, x, x-size}, new double[] {y,y+size,y+size/2}, 3);
			else if(angle == 270)  gc.fillPolygon(new double[] {x, x+size, x+size/2}, new double[] {y,y,y+size}, 3);
		}
}