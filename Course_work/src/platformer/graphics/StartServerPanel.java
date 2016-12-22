package platformer.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import platformer.Game;
import platformer.GameGUI;
import platformer.Handler;
import platformer.net.Client;
import platformer.net.Server;

public class StartServerPanel extends JPanel{
  public StartServerPanel(final GameGUI parent){
    JButton breakButton = new JButton("BREAK");
    JButton startButton = new JButton("START");
    JLabel textLabel = new JLabel("SELECT MAP");
    JLabel nameLabel = new JLabel("ENTER NAME");
    final JTextField nameField = new JTextField(20);
    
    Font labelFont = new Font("Jokerman",Font.BOLD | Font.ITALIC,40);
    nameLabel.setFont(labelFont);
    nameLabel.setForeground(Color.GREEN);
    textLabel.setFont(labelFont);
    textLabel.setForeground(Color.GREEN);
    
    nameField.setBorder(BorderFactory.createEmptyBorder());
    nameField.setBackground(new Color(0,0,0,70)); 
    nameField.setForeground(Color.GREEN);
    nameField.setPreferredSize(new Dimension(300,30));
    
    String[] items = new String[0];
    final HashMap<String,String> levels= new HashMap<String,String>();
    try{
      BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/platformer/res/levels.txt")));
      String line = br.readLine();
      int n=Integer.parseInt(line);
      items=new String[n];
      for(int i=0;i<n;i++){
        line=br.readLine();
        if(line==null)throw new IOException();
        String[] parts =line.split(" ");
        items[i]=parts[0];
        levels.put(parts[0], parts[1]);
      }
      br.close();
    }catch(IOException e){
      e.printStackTrace();
    }
    
    final JComboBox mapBox = new JComboBox(items);
    mapBox.setBackground(new Color(0,0,0,70));
    mapBox.setForeground(Color.GREEN);
   
    setLayout(new GridBagLayout());
    
    setStyle(breakButton);
    setStyle(startButton);
    
    addComponent(breakButton,0,0,1,1,GridBagConstraints.CENTER);
    addComponent(textLabel,0,1,1,1,GridBagConstraints.CENTER);
    addComponent(mapBox,0,2,1,1,GridBagConstraints.CENTER);
    addComponent(startButton,0,5,1,1,GridBagConstraints.CENTER);
    addComponent(nameLabel,0,3,1,1,GridBagConstraints.CENTER);
    addComponent(nameField,0,4,1,1,GridBagConstraints.CENTER);
    
    breakButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        parent.changeLayout("MULTIPLAYER");
      }
    });
    

    
    startButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        String str=nameField.getText();
        
        Game.username="";
        for(int i=0;i<str.length();i++){
        	char c = str.charAt(i);
        	if((c>='A'&&c<='Z')||(c>='a'&&c<='z')||(c>='1'&&c<='9'))
        		Game.username+=c;
        }
        if(Game.username.length()==0)return;
        //Start server and game
     // 	Game.switchLevel();
       // if(Game.server!=null)Game.server.stop();
        Game.server=new Server(Game.handler,7777);
    		Game.server.start();
        Game.multi=true;
        Game.localServer=true;

        String level=levels.get((String)mapBox.getSelectedItem());
        System.out.println("LEVEL: "+level);
        try {
					Game.handler.createLevel(ImageIO.read(getClass().getResource(level)));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        Game.client = new Client("localhost",7777,Game.handler);
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
