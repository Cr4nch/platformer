package platformer.graphics;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import platformer.Game;
import platformer.GameGUI;

public class MultiplayerPanel extends JPanel{
  public MultiplayerPanel(final GameGUI parent){
    JButton breakButton = new JButton("BREAK");
    JButton startServerButton = new JButton("START SERVER");
    JButton connectButton = new JButton("CONNECT TO SERVER");
    
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    
    setStyle(breakButton);
    setStyle(startServerButton);
    setStyle(connectButton);
    
    add(breakButton);
    add(startServerButton);
    add(connectButton);
    
    breakButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        parent.changeLayout("MENU");
      }
    });
    
    startServerButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        parent.changeLayout("START_SERVER");
      }
    });
    
    connectButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        parent.changeLayout("CONNECT");
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



