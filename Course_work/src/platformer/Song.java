package platformer;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;

public class Song{
  
  private Clip clip;
  
  public Song(String path){
    try{
      AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(path));
      AudioFormat baseFormat = ais.getFormat();
      AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),
                                                 16,baseFormat.getChannels(),baseFormat.getChannels()*2,
                                                 baseFormat.getSampleRate(),false);
      AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat,ais);
      clip = AudioSystem.getClip();  
      clip.open(dais);
      
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
