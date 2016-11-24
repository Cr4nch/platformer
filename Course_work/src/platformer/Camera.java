package platformer;

import platformer.Entity.Entity;

public class Camera{
	private int x,y;
	  
	public void tick(Entity player){
		setX(Game.WIDTH/2*Game.SCALE-player.getX());
	    setY(+Game.HEIGHT/2*Game.SCALE-player.getY());
	  // System.out.println("TICK CAM");
	}
	  
	public int getX(){
	    return x;
	}
	  
	public int getY(){
		return y;
	}
	  
	public void setX(int x){
		this.x=x;
	}
	  
	public void setY(int y){
	    this.y=y;
	}
	  
}
