package platformer.Tile;

import java.awt.Graphics;

import platformer.Game;
import platformer.Handler;
import platformer.Id;

public class Wall extends Tile{
  
  public Wall(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    super(x,y,width,height,soild,id,handler);
  }
  
  @Override
  public void render(Graphics g){
    g.drawImage(Game.grass.getBufferedImage(),x,y,width,height,null);
  }
  
  @Override
  public void tick(){
    
  }
  
}
