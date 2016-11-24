package platformer.graphics;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    
    setLayout(new GridBagLayout());
    
    addComponent(breakButton,0,0,1,1,GridBagConstraints.EAST);
    addComponent(nameLabel,0,1,1,1,GridBagConstraints.EAST);
    addComponent(ipLabel,0,2,1,1,GridBagConstraints.EAST);
    addComponent(portLabel,0,3,1,1,GridBagConstraints.EAST);
    addComponent(connectButton,0,4,1,1,GridBagConstraints.EAST);
    addComponent(nameField,1,1,1,1,GridBagConstraints.WEST);
    addComponent(ipField,1,2,1,1,GridBagConstraints.WEST);
    addComponent(portField,1,3,1,1,GridBagConstraints.WEST);
    
    
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
  

  
  private void addComponent(JComponent c,int x,int y,int width,int height,int align){
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx=x;
    gc.gridy=y;
    gc.gridwidth=width;
    gc.gridheight=height;
    gc.weightx=100.0;
    gc.weighty=100.0;
    gc.insets=new Insets(5,5,5,5);
    gc.anchor=align;
    gc.fill=GridBagConstraints.NONE;
    add(c,gc);
  }
  
 @Override
 protected void paintComponent(Graphics g) {
   super.paintComponent(g);
   g.drawImage(Game.menuBackground, 0, 0, null);
 } 
  
}
