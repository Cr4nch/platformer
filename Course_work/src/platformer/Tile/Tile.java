package platformer.Tile;

import java.awt.Graphics;
import java.awt.Rectangle;

import platformer.Handler;
import platformer.Id;

public abstract class Tile{
  public int x,y;
 public int width,height;
 public boolean soild;
 public int velX,velY;
 public boolean activated = false;
 public Id id;
 public int position;
 
 public int facing;
 
 public Handler handler;
 
 public Tile(int x,int y,int width,int height,boolean soild,Id id,Handler handler){
   this.x=x;
   this.y=y;
   this.width=width;
   this.height=height;
   this.soild=soild;
   this.handler=handler;
   this.id=id;
 }
 
 public abstract void render(Graphics g);
 public  abstract void tick();
 
 public void die(){
   handler.removeTile(this);
 }
 

 public Id getId(){
   return id;
 }
 
 public int getVelX(){
   return velX;
 }
 
 public int getVelY(){
   return velY;
 }
 
 public int getX(){
   return x;
 }
 
 public int getY(){
   return y;
 }
 
 public int getWidth(){
   return width;
 }
 
 public int getHeight(){
   return height;
 }
 
 public boolean getSoild(){
   return soild;
 }
 
 public void setId(Id id){
   this.id=id;
 }
 
 public void setVelX(int velX){
   this.velX=velX;
 }
 
 public void setVelY(int velY){
   this.velY=velY;
 }
 
 public void setX(int x){
   this.x=x;
 }
 
 public void setY(int y){
   this.y=y;
 }
 
 public void setWidth(int width){
   this.width=width;
 }
 
 public void setHeight(int height){
   this.height=height;
 }
 
 public void setSoild(boolean soild){
   this.soild=soild;
 }
 
 public Rectangle getBounds(){
   return new Rectangle(x,y,width,height);
 }
 
 public Rectangle getBoundsTop(){
   return new Rectangle(x+10,y,width-20,5);
 }
 
 public Rectangle getBoundsBottom(){
   return new Rectangle(x+10,y+height-5,width-20,5);
 }
 
 public Rectangle getBoundsLeft(){
   return new Rectangle(x,y+10,5,height-20);
 }
 
 public Rectangle getBoundsRight(){
   return new Rectangle(x+width-5,y+10,5,height-20);
 }
 
 public void setPosition(int position){
   this.position=position;
 }
 
}
