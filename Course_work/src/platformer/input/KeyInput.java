package platformer.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import platformer.Game;
import platformer.Id;
import platformer.Entity.Entity;
import platformer.Entity.FireBall;
import platformer.Entity.Player;
import platformer.Tile.Tile;

public class KeyInput implements KeyListener{
  
  private boolean fire = true;
  
  @Override 
  public void keyPressed(KeyEvent event){
    int key = event.getKeyCode();
    
    if(!Game.playing/*&&Launcher.menuType==2*/)   { 
      if(key>=KeyEvent.VK_A && key<=KeyEvent.VK_Z){
        Game.username+=(char)key;
        return; 
      }else if(key==KeyEvent.VK_BACK_SPACE){
        if(Game.username.length()>2){
          Game.username=Game.username.substring(0,Game.username.length()-2);
        }else Game.username="";
      }
    }
    if(!Game.playing)return;
    
    
    for(int i=0;i<Game.handler.entity.size();i++){
      Entity en = Game.handler.entity.get(i);
      if(en.id == Id.player){
      	
        	if(en instanceof Player){
        		if(((Player)(en)).name.compareTo(Game.username)!=0)
        			continue;
        	}
      	
        if(en.goingDownPipe)return;
    switch(key){
      case KeyEvent.VK_W:
        for(int j =0; j<Game.handler.tile.size();j++){
         Tile t = Game.handler.tile.get(j);
         if(t.getId()==Id.pipe){
           if(en.getBoundsTop().intersects(t.getBounds())){
             if(!en.goingDownPipe){
             //  en.setVelX(0);
               en.goingDownPipe=true;
             }
           }
         }
       }
        if(!en.jumping) {
          en.jumping=true;
         en.gravity=9.0;
         Game.jump.play();
        }
        //en.setVelY(-5);
        break;
     case KeyEvent.VK_S:
       for(int j =0; j<Game.handler.tile.size();j++){
         Tile t = Game.handler.tile.get(j);
         if(t.getId()==Id.pipe){
           if(en.getBoundsBottom().intersects(t.getBounds())){
             if(!en.goingDownPipe){
             //  en.setVelX(0);
               en.goingDownPipe=true;
             }
           }
         }
       }
        break;
      case KeyEvent.VK_A:
        en.facing=0;
        en.setVelX(-5);
        break;
      case KeyEvent.VK_D:
        en.facing=1;
        en.setVelX(5);
        break;
      case KeyEvent.VK_Q:
        en.die();
        break;
      case KeyEvent.VK_SPACE:
        
        if(en.facing==0&&fire)
          Game.handler.addEntity(new FireBall(en.getX()-24,en.getY()+12,24,24,true,Id.fireball,en.handler,en.facing));
        if(en.facing==1&&fire)
          Game.handler.addEntity(new FireBall(en.getX()+en.getWidth(),en.getY()+12,24,24,true,Id.fireball,en.handler,en.facing));
        fire = false;
        break;
      case KeyEvent.VK_ESCAPE:
        if(Game.playing){
          Game.playing=false;
          Game.guiParent.changeLayout("PAUSE");
        }
        break;
    }
    	if(Game.multi){
    		Game.client.sendUpdate((Player)en);
    	}
      }
    }
  }
  
  @Override
  public void keyReleased(KeyEvent event){
  	
  	
    int key = event.getKeyCode(); 
    for(Entity en:Game.handler.entity){
      if(en.id == Id.player){
      	if(en instanceof Player){
      		if(((Player)(en)).name.compareTo(Game.username)!=0)
      			continue;
      	}
      	
      	
      	
    switch(key){
      case KeyEvent.VK_W:
        en.setVelY(0);
        break;
   /*   case KeyEvent.VK_S:
        en.setVelY(0);
        break;*/
      case KeyEvent.VK_A:
        en.setVelX(0);
        break;
      case KeyEvent.VK_D:
        en.setVelX(0);
        break;
      case KeyEvent.VK_SPACE:
        fire = true;
        break;
    
    }
    if(Game.multi){
    	Game.client.sendUpdate((Player)en);
    }
      }
   }
  }
  
  @Override
  public void keyTyped(KeyEvent event){
    
  }
  
}
