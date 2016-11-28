package platformer.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
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
    
    setStyle(continueButton);
    setStyle(backButton);
    setStyle(exitButton);
    
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
  
  
  public void setStyle(JButton button){
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setContentAreaFilled(false);
    
    Font buttonFont = new Font("Jokerman",Font.BOLD | Font.ITALIC,20);
    button.setFont(buttonFont);
    button.setForeground(Color.GREEN);
    
    Insets margin = new Insets(20,150,20,150);
    button.setMargin(margin);
 } 
   
@Override
 protected void paintComponent(Graphics g) {
   super.paintComponent(g);
   g.drawImage(Game.menuBackground, 0, 0, null);
 } 
  
  
}
