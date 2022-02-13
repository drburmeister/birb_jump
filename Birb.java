package BirbSpike;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Birb extends Sprite
{
  Color color = Color.RED;
  public static final double RADIUS = 8;
  
  void render(GraphicsContext gc)
  {
    if (visible)
    {
    	//draw the coin
    	gc.setFill(color);
    	gc.fillOval(x-RADIUS, y-RADIUS, 2*RADIUS, 2*RADIUS);
    }
  }
}
