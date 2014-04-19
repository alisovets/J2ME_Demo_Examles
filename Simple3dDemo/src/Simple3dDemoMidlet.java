import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
//import javax.microedition.midlet.*;

public class Simple3dDemoMidlet extends MIDlet{ 
	
	Simple3dDemoCanvas myCanvas;
	public Simple3dDemoMidlet() {
	 	//myCanvas = new TestCanvas(Display.getDisplay(this));
		myCanvas = new Simple3dDemoCanvas(this);	    	   
	}
	  
	public void startApp() throws MIDletStateChangeException {		
		myCanvas.start();	    
	}  
	  
	public void destroyApp(boolean unconditional) throws MIDletStateChangeException {}  
  
	public void pauseApp() {}
	
	public void stop(){
		try {
			destroyApp(false);
			notifyDestroyed();
		} catch (MIDletStateChangeException ex) {  }
	}
  
}
