package platformer.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import platformer.Game;

public class MouseInput implements MouseListener,MouseMotionListener{
  public int x,y;
  
  @Override
  public void mouseDragged(MouseEvent e){
  }
  
  @Override 
  public void mouseMoved(MouseEvent e){
    this.x=e.getX();
    this.y=e.getY();
   // System.out.println(x+" "+y);
  }
  
  @Override
  public void mouseClicked(MouseEvent e){
  }
  
  @Override 
  public void mouseEntered(MouseEvent e){
  }
  
  @Override
  public void mouseExited(MouseEvent e){
  }
  
  @Override
  public void mousePressed(MouseEvent e){
   // System.out.println(x+" "+y);
    if(!Game.playing){
    /*  switch(Launcher.menuType){
        case 0:
        for(int i=0;i<Game.launcher.buttonsMain.length;i++){
          Button button = Game.launcher.buttonsMain[i];
          if(button.contain(this.x,this.y))button.trigerEvent();
        }
        break;
        case 1:
        for(int i=0;i<Game.launcher.buttonsLead.length;i++){
          Button button = Game.launcher.buttonsLead[i];
          if(button.contain(this.x,this.y))button.trigerEvent();
        }
        break;
        case 2:
        for(int i=0;i<Game.launcher.buttonsStart.length;i++){
          Button button = Game.launcher.buttonsStart[i];
          if(button.contain(this.x,this.y))button.trigerEvent();
        }
        break;
        case 3:
        for(int i=0;i<Game.launcher.buttonsPause.length;i++){
          Button button = Game.launcher.buttonsPause[i];
          if(button.contain(this.x,this.y))button.trigerEvent();
        }
        break;
      }*/
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e){
  }
  
}