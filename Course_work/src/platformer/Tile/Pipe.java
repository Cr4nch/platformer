package platformer.Tile;

import java.awt.Color;
import java.awt.Graphics;

import platformer.Handler;
import platformer.Id;
import platformer.Entity.Plant;


public class Pipe extends Tile{
  public Pipe(int x,int y,int width,int height,boolean soild,Id id,Handler handler,int facing,boolean plant){
    super(x,y,width,height,soild,id,handler);
    this.facing = facing;
    
    if(plant){
      handler.addEntity(new Plant(x,y-64,width,width,soild,Id.plant,handler));
    }
  }
  
  @Override
  public void render(Graphics g){
    g.setColor(new Color(0,128,0));
    g.fillRect(x,y,width,height);
  }
  
  @Override
  public void tick(){
  }
  
  public byte getType(){
    return 1;
  }
  
}
