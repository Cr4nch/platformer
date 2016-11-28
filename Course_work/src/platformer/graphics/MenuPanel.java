package platformer.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import platformer.Game;
import platformer.GameGUI;

public class MenuPanel extends JPanel{
  public MenuPanel(final GameGUI parent){
    JButton startButton = new JButton("START GAME");
    JButton multiplayerButton = new JButton("MULTIPLAYER");
    JButton topButton   = new JButton("TOP PLAYERS");
    JButton exitButton  = new JButton("EXIT GAME");
    
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    setStyle(startButton);
    setStyle(topButton);
    setStyle(exitButton);
    setStyle(multiplayerButton);
    
    add(startButton,gbc);
    add(multiplayerButton,gbc);
    add(topButton,gbc);
    add(exitButton,gbc);
    
    
    startButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        parent.changeLayout("COMPANY");
      }
    });
    
    multiplayerButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        parent.changeLayout("MULTIPLAYER");
      }
    });
    
    topButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        parent.changeLayout("TOP");
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
  
}
