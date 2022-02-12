package HW2_Burmeister;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Coin extends Sprite
{
  Color color = Color.GOLD;
  public static final double RADIUS = 8;

  void updateSprite()
  {
    if (active)
    {
      if (y > hw2_Burmeister.HEIGHT)
      	suspend();
    }
  }

  void render(GraphicsContext gc)
  {
    if (visible)
    {
      gc.setFill(color);
      gc.fillOval(x-RADIUS, y-RADIUS, 2*RADIUS, 2*RADIUS);
    }
  }
}
