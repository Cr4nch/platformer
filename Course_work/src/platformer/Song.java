package platformer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Song{
  
  private Clip clip;
  
  public Song(String path){
    try{

      DataLine.Info daInfo = new DataLine.Info(Clip.class, null);
      try {
          URL sounURL = Song.class.getResource(path);

          AudioInputStream inputStream = AudioSystem.getAudioInputStream(sounURL);
          DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
          clip = (Clip) AudioSystem.getLine(info);
          clip.open(inputStream);

      } catch (LineUnavailableException e) {
          e.printStackTrace();
      } catch (UnsupportedAudioFileException ex) {
          ex.printStackTrace();
      } catch (IOException ex) {
          ex.printStackTrace();
      }
      
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public void play(){
    if(clip==null)return;
    stop();
    clip.setFramePosition(0);
    clip.start();
  }
  
  public void close(){
    stop();
    clip.close();
  }
  
  public void stop(){
    if(clip.isRunning())clip.stop();
  }
  
  
}
