import java.util.Date;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;
import simple3d.gameentity.Scene1;
import simple3d.gameentity.Tank;
import simple3d.gameentity.Tree;
import simple3d.shape.Box;
import simple3d.shape.Cone;
import simple3d.shape.Rectangle;
import simple3d.shape.Shape;
import simple3d.shape.Sphere;

/**
 * 
 * A simple Canvas class to the drawing 3d primitives.
 * 
 * @author Alexander Lisovets
 * 
 */
public class Simple3dDemoCanvas extends GameCanvas {
	private Simple3dDemoMidlet midlet;
	private Display myDisplay;
	private Graphics g;
	boolean inited = false;

	private int w2, h2;
	Draw3dHelper drw;

	public Simple3dDemoCanvas(Simple3dDemoMidlet midlet) {
		super(false);
		this.midlet = midlet;
		myDisplay = Display.getDisplay(midlet);
		myDisplay.setCurrent(this);
		setFullScreenMode(true);
		g = getGraphics();
		w2 = getWidth();
		h2 = getHeight();
		drw = new Draw3dHelper(w2, h2);
		inited = true;
	}

	private void pause(long msec) {
		try {
			Thread.sleep(msec);
		} catch (Exception e) {
		}
	}

	public void stop() {
		pause(100);
		midlet.stop();
	}

	public void drawPxArr(int[] c) {
		g.drawRGB(c, 0, w2, 0, 0, w2, h2, false);
	}

	public void start() {
		while (inited == false) {
			pause(50);
		}

		testPrimitives();
		drw = new Draw3dHelper(w2, h2);
		testSnowman();
		testTank2();
		testTank();
		pause(3000);
		stop();
	}

	public void testTank() {
		Date dt = new Date();
		drw.cleanDrawBuffer(0x20);

		Shape shapes[] = new Shape[7];
		shapes[0] = new Tank(0x10c820, 8);
		shapes[0].move(new Vector3D(0, -80, 800));

		shapes[1] = new Tank((150 << 16) + (200 << 8) + 30, 8);
		shapes[1].move(new Vector3D(0, -80, 750));

		shapes[2] = new Tank((100 << 16) + (200 << 8) + 60, 8);
		shapes[2].move(new Vector3D(0, -80, 500));

		Matrix3D mty1 = Transform3D.getTurnMatrixY(3.14f);
		shapes[3] = new Tank((100 << 16) + (200 << 8) + 90, 8);
		shapes[3].turn0(mty1);
		shapes[3].move(new Vector3D(0, -80, 350));

		shapes[4] = new Tank((100 << 16) + (200 << 8) + 150, 8);
		shapes[4].turn0(mty1);
		shapes[4].move(new Vector3D(0, -80, 200));

		mty1 = Transform3D.getTurnMatrixY(1.57f);
		shapes[5] = new Tank((100 << 16) + (200 << 8) + 200, 8);
		shapes[5].turn0(mty1);
		shapes[5].move(new Vector3D(350, -80, 500));

		shapes[6] = new Tank((120 << 16) + (200 << 8) + 255, 8);
		shapes[6].turn0(mty1);
		shapes[6].move(new Vector3D(-350, -80, 500));

		Tree tree1 = new Tree(8);
		tree1.move(new Vector3D(-80, 0, 500));

		Tree tree2 = new Tree(8);
		tree2.move(new Vector3D(210, 0, 700));
		Tree tree3 = new Tree(8);
		tree3.move(new Vector3D(-100, 0, 1500));
		Tree tree4 = new Tree(8);
		tree4.move(new Vector3D(-400, 0, 1000));
		Tree tree5 = new Tree(8);
		tree5.move(new Vector3D(-350, 0, 1100));
		Tree tree6 = new Tree(8);
		tree6.move(new Vector3D(-370, 0, 1300));

		Vector3D v0 = new Vector3D(0, -30, 500);
		mty1 = Transform3D.getTurnMatrixY(0.157f);
		Matrix3D mty2 = Transform3D.getTurnMatrixY(-0.157f);
		for (int i = 0; i < 200; i++) {
			drw.cleanDrawBuffer(0x30);
			tree1.draw(drw);
			tree2.draw(drw);
			tree3.draw(drw);
			tree4.draw(drw);
			tree5.draw(drw);
			tree6.draw(drw);

			for (int j = 0; j < shapes.length; j++) {
				if (j % 2 == 0)
					((Tank) shapes[j]).turnTurret(0.4f);
				else
					((Tank) shapes[j]).turnTurret(-0.3f);
				if ((j == 1) || (j == 3))
					shapes[j].turn(mty1, v0);
				else
					shapes[j].turn(mty2, v0);
				shapes[j].draw(drw);
			}

			drawPxArr(drw.getColorBuffer());
			float fps = (float) ((i + 1) * 1000)
					/ ((new Date()).getTime() - dt.getTime());
			g.setColor(0xffff00);
			g.drawString("fps = " + fps + "  " + i, 10, 10, Graphics.TOP
					| Graphics.LEFT);
			flushGraphics();
		}

		pause(1000);
	}

	void testTank2() {
		Date dt = new Date();
		Vector3D v1 = new Vector3D(0, -200, 800);
		Matrix3D mty2 = Transform3D.getTurnMatrixY(-0.09f);
		Matrix3D mtt = Transform3D.getTurnMatrixY(-0.019f);
		mtt.mult(Transform3D.getTurnMatrixX(-0.037f));
		Tank tank1 = new Tank(0xffff, 40);
		tank1.move(v1);
		for (int i = 0; i < 100; i++) {
			drw.cleanDrawBuffer(0x30);

			tank1.turn(mty2, v1);
			tank1.turnTurret(0.2f);
			tank1.turn(mtt);
			tank1.draw(drw);
			drawPxArr(drw.getColorBuffer());
			float fps = (float) ((i + 1) * 1000)
					/ ((new Date()).getTime() - dt.getTime());
			g.setColor(0xffff00);
			g.drawString("fps = " + fps + "  " + i, 10, 10, Graphics.TOP
					| Graphics.LEFT);
			flushGraphics();
		}
	}

	void testPrimitives() {
		Date dt = new Date();

		Sphere ball = new Sphere(80, 160, 40, 0xffffff,
				Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		ball.move(new Vector3D(0, -150, 1000));

		Sphere ball2 = new Sphere(150, 100, 100, 0xffbfa0,
				Draw3dHelper.BOTH_OF_SIDES_ARE_LIT);
		ball2.turn(Transform3D.getTurnMatrixX(3.14f));
		ball2.move(new Vector3D(0, -250, 1000));

		Box box1 = new Box(100f, 0xffff00, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		Matrix3D mtb1 = Matrix3D.mult(Transform3D.getTurnMatrixZ(0.9f),
				Transform3D.getTurnMatrixY(0.77f));

		box1.turn(mtb1);
		box1.move(new Vector3D(0, 60, 1000));
		Matrix3D mty1 = Transform3D.getTurnMatrixY(-0.1f);

		Vector3D v2 = new Vector3D(0, 220, 1000);
		Rectangle rect1 = new Rectangle(v2, 120, 60, 0xff0000,
				Draw3dHelper.BOTH_OF_SIDES_ARE_LIT);
		Matrix3D mty2 = Transform3D.getTurnMatrixY(0.1f);

		Cone cone1 = new Cone(200, 5, 80, 6.28f, 20, 0xffff,
				Draw3dHelper.BOTH_OF_SIDES_ARE_LIT);
		cone1.turn0(Transform3D.getTurnMatrixZ(1.2f));
		cone1.move(new Vector3D(-150, 0, 0));
		Vector3D v4 = new Vector3D(0, -200, 1000);
		Cone cone2 = new Cone(cone1);
		cone2.turn0(Transform3D.getTurnMatrixY(2.09f));
		Cone cone3 = new Cone(cone1);
		cone3.turn0(Transform3D.getTurnMatrixY(4.19f));
		cone1.move(v4);
		cone2.move(v4);
		cone3.move(v4);
		Matrix3D mty4 = Transform3D.getTurnMatrixY(0.09f);
		Matrix3D mty5 = Transform3D.getTurnMatrixY(0.03f);

		Box b2 = new Box(100f, 100f, 100f, 0xff0000, 0xff00, 0xff,
				Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		b2.move(new Vector3D(-500, 300, 1000));
		Vector3D v1 = new Vector3D(0, 2, 0);

		Matrix3D mt = Transform3D.getTurnMatrixY(-0.09f);
		mt.mult(Transform3D.getTurnMatrixX(-0.07f));
		mt.mult(Transform3D.getTurnMatrixZ(0.2f));

		Vector3D c1 = new Vector3D(0, 0, 900);

		Box b3 = new Box(100f, 100f, 100f, 0xff8080, 0x80ff80, 0x80ff,
				Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		b3.move(new Vector3D(-400, 100, 1000));
		Matrix3D mt2 = Transform3D.getTurnMatrixY(0.09f);
		mt.mult(Transform3D.getTurnMatrixX(-0.07f));

		for (int i = 0; i < 200; i++) {
			drw.cleanDrawBuffer(0x30);
			drw.setMainLight(mty5.mult(drw.getMainLight()));

			ball.draw(drw);
			ball2.draw(drw);
			rect1.turn(mty2, v2);
			rect1.draw(drw);

			box1.turn(mty1);
			box1.draw(drw);

			b2.turn(mt);
			b2.turn(mty1, c1);
			b2.rmove(v1);
			b2.draw(drw);

			b3.turn(mt2, c1);
			b3.draw(drw);

			cone1.turn(mty4, v4);
			cone1.draw(drw);
			cone2.turn(mty4, v4);
			cone2.draw(drw);
			cone3.turn(mty4, v4);
			cone3.draw(drw);

			drawPxArr(drw.getColorBuffer());
			float fps = (float) ((i + 1) * 1000)
					/ ((new Date()).getTime() - dt.getTime());
			g.setColor(0xffff00);
			g.drawString("fps = " + fps + "  " + i, 10, 10, Graphics.TOP
					| Graphics.LEFT);
			flushGraphics();
		}
	}

	void testSnowman() {

		Scene1 scene1 = new Scene1(14);
		scene1.move(new Vector3D(0, -250, 1000));

		Matrix3D matrix = Transform3D.getTurnMatrixX(-0.29f);
		scene1.turn(matrix);
		matrix = (Transform3D.getTurnMatrixY(-0.1f));
		Date dt = new Date();
		for (int i = 0; i < 100; i++) {

			drw.cleanDrawBuffer(0x101040);
			scene1.turn(matrix);

			scene1.turnRotats(0.4f);
			scene1.turnMainAxis(-0.08f);
			scene1.draw(drw);

			drawPxArr(drw.getColorBuffer());
			float fps = (float) ((i + 1) * 1000)
					/ ((new Date()).getTime() - dt.getTime());
			g.setColor(0xffff00);
			g.drawString("fps = " + fps + "  " + i, 10, 10, Graphics.TOP
					| Graphics.LEFT);
			flushGraphics();

		}

	}

}
