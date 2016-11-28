package platformer.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import platformer.Game;
import platformer.GameGUI;
import platformer.net.Client;

public class ConnectPanel extends JPanel{
  public ConnectPanel(final GameGUI parent){
    JButton breakButton = new JButton("BREAK");
    JButton connectButton = new JButton("CONNECT");
    JLabel nameLabel = new JLabel("NAME");
    JLabel ipLabel = new JLabel("IP  ");
    JLabel portLabel = new JLabel("PORT");
    final JTextField nameField = new JTextField(10);
    final JTextField ipField = new JTextField(15);
    final JTextField portField = new JTextField(6);
    
    Font labelFont = new Font("Jokerman",Font.BOLD | Font.ITALIC,30);
    nameLabel.setFont(labelFont);
    nameLabel.setForeground(Color.GREEN);
    ipLabel.setFont(labelFont);
    ipLabel.setForeground(Color.GREEN);
    portLabel.setFont(labelFont);
    portLabel.setForeground(Color.GREEN);
    
    nameField.setBorder(BorderFactory.createEmptyBorder());
    nameField.setBackground(new Color(0,0,0,50)); 
    nameField.setForeground(Color.GREEN);
    
    ipField.setBorder(BorderFactory.createEmptyBorder());
    ipField.setBackground(new Color(0,0,0,50)); 
    ipField.setForeground(Color.GREEN);
    
    portField.setBorder(BorderFactory.createEmptyBorder());
    portField.setBackground(new Color(0,0,0,50)); 
    portField.setForeground(Color.GREEN);
    
    
    GridBagLayout lay = new GridBagLayout();
    lay.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
    lay.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
    setLayout(new GridBagLayout());
    
    setStyle(breakButton);
    setStyle(connectButton);
    
    addComponent(breakButton,0,0,1,1,GridBagConstraints.EAST,0,0,GridBagConstraints.NONE);
    addComponent(nameLabel,0,1,1,1,GridBagConstraints.WEST,0,0,GridBagConstraints.NONE);
    addComponent(ipLabel,0,2,1,1,GridBagConstraints.WEST,0,0,GridBagConstraints.NONE);
    addComponent(portLabel,0,3,1,1,GridBagConstraints.WEST,0,0,GridBagConstraints.NONE);
    addComponent(connectButton,0,4,1,1,GridBagConstraints.EAST,0,0,GridBagConstraints.NONE);
    addComponent(nameField,1,1,1,1,GridBagConstraints.WEST,0,0,GridBagConstraints.NONE );
    addComponent(ipField,1,2,1,1,GridBagConstraints.WEST,0,0,GridBagConstraints.NONE);
    addComponent(portField,1,3,1,1,GridBagConstraints.WEST,0,0,GridBagConstraints.NONE);
    
    
    breakButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        parent.changeLayout("MULTIPLAYER");
      }
    });
    
    connectButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
      	
      	Game.username=nameField.getText();
      	Game.multi=true;
      	Game.client=new Client(ipField.getText(),Integer.parseInt(portField.getText()),Game.handler);
      	Game.client.start();
      	Game.playing=true;
      	
        parent.changeLayout("GAME");
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
  

  
  private void addComponent(JComponent c,int x,int y,int width,int height,int align,double weightx,double weighty,int fill){
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx=x;
    gc.gridy=y;
    gc.gridwidth=width;
    gc.gridheight=height;
    gc.weightx=weightx;
    gc.weighty=weighty;
    gc.insets=new Insets(5,5,5,5);
    gc.anchor=align;

    //gc.fill=GridBagConstraints.NONE;
    gc.fill=fill;
    add(c,gc);
  }
  
 @Override
 protected void paintComponent(Graphics g) {
   super.paintComponent(g);
   g.drawImage(Game.menuBackground, 0, 0, null);
 } 
  
}
