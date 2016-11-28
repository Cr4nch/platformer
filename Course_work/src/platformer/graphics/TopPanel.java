package platformer.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import platformer.Game;
import platformer.GameGUI;

public class TopPanel extends JPanel{
  public TopPanel(final GameGUI parent){
    JButton backButton = new JButton("BACK");
    
    setLayout(new GridBagLayout());

    
    addComponent(backButton,0,0,1,1,GridBagConstraints.CENTER);
    Insets margin = new Insets(20,150,20,150);
    backButton.setMargin(margin);
    
    setStyle(backButton);
    
    BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/platformer/res/leadboard.txt")));
    String line=null;
    int i=1;
    try {
			while((line=br.readLine())!=null){
				JLabel label = new JLabel(line);
				addComponent(label,0,++i,1,1,GridBagConstraints.CENTER);
				Font labelFont = new Font("Jokerman",Font.BOLD | Font.ITALIC,30);
				label.setForeground(Color.GREEN);
				label.setFont(labelFont);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    
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
