package platformer.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import platformer.Game;
import platformer.GameGUI;

public class CompanyPanel extends JPanel{
  public CompanyPanel(final GameGUI parent){
    JButton playButton = new JButton("PLAY");
    JButton backButton = new JButton("BACK");
    JLabel nameLabel = new JLabel("Enter name");
    final JTextField nameField = new JTextField("",20);
    
    Font labelFont = new Font("Jokerman",Font.BOLD | Font.ITALIC,40);
    nameLabel.setFont(labelFont);
    nameLabel.setForeground(Color.GREEN);
    
    nameField.setBorder(BorderFactory.createEmptyBorder());
    nameField.setBackground(new Color(0,0,0,30)); 
    nameField.setForeground(Color.GREEN);
    
    
    add(playButton);
    add(backButton);
    
    add(nameLabel);
    add(nameField);
    
    setStyle(playButton);
    setStyle(backButton);
    
    
    
    playButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        Game.username=nameField.getText();
        Game.playing=true;
        parent.changeLayout("GAME");
      }
    });
    backButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        parent.changeLayout("MENU");
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
