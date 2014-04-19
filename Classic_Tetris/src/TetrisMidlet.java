import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Game Tetris Midlet
 * @author Alexander Lisovets
 * 2007
 */
public class TetrisMidlet extends MIDlet {

	TetrisCanvas myCanvas;

	public TetrisMidlet() {
		myCanvas = new TetrisCanvas(this);
	}

	public void startApp() throws MIDletStateChangeException {
		myCanvas.startInit();
		myCanvas.start();
	}

	public void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
	}

	public void pauseApp() {
	}

	public void stop() {
		try {
			destroyApp(false);
			notifyDestroyed();
		} catch (MIDletStateChangeException ex) {
		}
	}

}
