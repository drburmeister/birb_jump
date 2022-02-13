package BirbSpike;

//Class for simulating gravity on the player and handling wall collisions
class Sprite
{
  double x,y;
  double dx,dy;
  public static final double GRAVITY = 2;
  public static final double JUMP = -13;
 
  boolean active=false, visible=false;
  public static boolean l_bound = false, r_bound = false;

  //method for moving the player with their velocity and gravity
  void updatePosition()
  {
	//move player horizontally with their set x velocity and flip the sign if player hits a vertical wall
  	x += dx;
  	if ((x - Birb.RADIUS*2) <= birb_Spike.LEFTEDGE) {
  	    dx*=-1;
  	    l_bound = true; //turn on wall collision flag
  	}
  	else if ((x + Birb.RADIUS*2) >= birb_Spike.RIGHTEDGE) {
  	    dx*=-1;
  	    r_bound = true;
  	}
  	//move player vertically with their set y velocity and change it every update with accordance to GRAVITY
  	//also handles if player hits the top or bottom of the play area
  	y += dy;
  	if ((y + Birb.RADIUS) >= birb_Spike.HEIGHT) dy = JUMP;
  	else if ((y - Birb.RADIUS) <= 0 ) dy = -JUMP/3;     

  	dy += GRAVITY;
  }
  //Set y velocity when jumping is activated
  void birbBounce() {
	  dy = JUMP;
  }
  void setPosition(double x2, double y2)
  {
  	x = x2; y = y2;
  }

  void setVelocity(double a, double b)
  {
    dx = a; dy = b;
  }

  boolean isCloserThan(Sprite s, double dist)
  {
  	// Return true if Sprite s is closer to
  	// the current Sprite (this) than specified
  	// distance dist.
    double dx = x-s.x;
    double dy = y-s.y;
    return dx*dx+dy*dy < dist*dist;
  }

  boolean isActive()
  {
  	return active;
  }

  void suspend()
  {
    active = false; visible = false;
  }

  void resume()
  {
    active = true; visible = true;
    setPosition(x, y);
    setVelocity(dx, dy);
  }
}