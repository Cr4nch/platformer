package platformer.Tile;

import java.awt.Graphics;

import platformer.Game;
import platformer.Handler;
import platformer.Id;

public class Flag extends Tile{
  public Flag(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
    super(x,y,width,height,soild,id,handler);
  } 
  
  @Override
  public void render(Graphics g){
    g.drawImage(Game.flag[1].getBufferedImage(),x,y,width,height/5,null);
    g.drawImage(Game.flag[0].getBufferedImage(),x,y+height/5,width,height/5,null);
    g.drawImage(Game.flag[0].getBufferedImage(),x,y+height*2/5,width,height/5,null);
    g.drawImage(Game.flag[0].getBufferedImage(),x,y+height*3/5,width,height/5,null);
    g.drawImage(Game.flag[2].getBufferedImage(),x,y+height*4/5,width,height/5,null);
    
  }
  
  @Override
  public void tick(){
  }
}