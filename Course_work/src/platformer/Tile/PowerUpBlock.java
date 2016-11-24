package platformer.Tile;

import java.awt.Graphics;


import platformer.Game;
import platformer.Handler;
import platformer.Id;
import platformer.Entity.Mushroom;
import platformer.graphics.sprites.Sprite;

public class PowerUpBlock extends Tile{
  
  private Sprite powerUp;
  private boolean poppedUp = false;
  
  private int spriteY = getY();
  private byte  type;
  
  public PowerUpBlock(int x,int y,int width,int height,boolean soild,Id id,Handler handler,Sprite powerUp,byte type){
    super(x,y,width,height,soild,id,handler);
    this.powerUp = powerUp;
    this.type = type;
    spriteY=getY();
  }
  
  
  @Override
  public void render(Graphics g){
    if(!poppedUp)g.drawImage(powerUp.getBufferedImage(),x,spriteY,width,height,null);
    if(!activated)g.drawImage(Game.powerUp.getBufferedImage(),x,y,width,height,null);
    else g.drawImage(Game.usedPowerUp.getBufferedImage(),x,y,width,height,null);
  }
  
  
  @Override
  public void tick(){
    if(activated&&!poppedUp){
      spriteY--;
    if(spriteY<y-height){
      handler.addEntity(new Mushroom(x,spriteY,width,height,true,Id.mushroom,handler,type));
      poppedUp=true;
    }
    }
  }
  
  public void setState(byte state){
    if(state==0)this.poppedUp=false;
    else this.poppedUp=true;
  }
  
  public byte getState(){
    return (byte)(this.poppedUp?1:0);
  }
    
  public byte getType(){
    return this.type;
  }
  
}
