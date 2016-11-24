package platformer.graphics;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import platformer.Game;
import platformer.GameGUI;

public class PausePanel extends JPanel{
  public PausePanel(final GameGUI parent){
    JButton continueButton = new JButton("CONTINUE");
    JButton backButton = new JButton("BACK");
    JButton exitButton = new JButton("EXIT");
    
    add(continueButton);
    add(backButton);
    add(exitButton);
    
    continueButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        Game.playing=true;
        parent.changeLayout("GAME");
      }
    });
    backButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        parent.changeLayout("MENU");
      }
    });
    exitButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        System.exit(0);
      }
    });
  }
   
@Override
 protected void paintComponent(Graphics g) {
   super.paintComponent(g);
   g.drawImage(Game.menuBackground, 0, 0, null);
 } 
  
  
}
