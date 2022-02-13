package BirbSpike;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Coin extends Sprite
{
  Color color = Color.GOLD;
  
  //The coin should always be a little bigger than the player
  public static final double RADIUS = Birb.RADIUS*1.5;


  void render(GraphicsContext gc)
  {
    if (visible)
    {
    	//draw the coin
		gc.setFill(Color.GOLDENROD);
		gc.fillOval(x-RADIUS-2, y-RADIUS-2, 2*RADIUS+4, 2*RADIUS+4);
		gc.setFill(color);
		gc.fillOval(x-RADIUS, y-RADIUS, 2*RADIUS, 2*RADIUS);

    }
  }
}
