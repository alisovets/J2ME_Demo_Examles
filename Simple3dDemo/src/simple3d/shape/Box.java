package simple3d.shape;

import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;

/**
 * 
 * @author Alexander Liovets
 * 
 */
public class Box implements Shape {

	public int viewType;
	public Vector3D center; // central point
	private Rectangle[] faces;

	public Box() {
		center = new Vector3D(); // central point
		faces = new Rectangle[6];
		for (int i = 0; i < 6; i++) {
			faces[i] = new Rectangle();
		}
	}

	public Box(Box b) {
		this();
		set(b);
	}

	public Box(float width, float height, float depth, int[] colors, int vt) {
		this();
		set(width, height, depth, colors, vt);
	}

	/**
	 * creates a box
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 * @param col1
	 *            - the front and back face color
	 * @param col2
	 *            - the right and left face color
	 * @param col3
	 *            - the bottom and top face color
	 * @param vt
	 */
	public Box(float width, float height, float depth, int col1, int col2,
			int col3, int vt) {
		this();
		int[] colors = new int[6];
		colors[0] = col1;
		colors[1] = col1;
		colors[2] = col2;
		colors[3] = col2;
		colors[4] = col3;
		colors[5] = col3;
		set(width, height, depth, colors, vt);
	}

	/**
	 * creates cubic block
	 * 
	 * @param size
	 * @param col
	 * @param vt
	 */
	public Box(float size, int col, int vt) {
		this(size, size, size, col, col, col, vt);
	}

	public void set(Box b) {
		viewType = b.viewType;
		center.set(b.center);
		for (int i = 0; i < faces.length; i++) {
			faces[i].set(b.faces[i]);
		}
	}

	/**
	 * sets parameters of block
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 * @param color
	 *            an array of 6 colors
	 * @param vt
	 */
	public void set(float width, float height, float depth, int[] color, int vt) {
		viewType = vt;
		width /= 2;
		height /= 2;
		depth /= 2;
		Vector3D[] vs = new Vector3D[8];
		vs[0] = new Vector3D(-width, height, -depth);
		vs[1] = new Vector3D(width, height, -depth);
		vs[2] = new Vector3D(width, -height, -depth);
		vs[3] = new Vector3D(-width, -height, -depth);
		vs[4] = new Vector3D(-width, height, depth);
		vs[5] = new Vector3D(width, height, depth);
		vs[6] = new Vector3D(width, -height, depth);
		vs[7] = new Vector3D(-width, -height, depth);

		Vector3D normal = new Vector3D(0, 0, 1);
		faces[0].set(new Vector3D[] { vs[0], vs[1], vs[2], vs[3] }, normal,
				color[0], vt);
		normal.set(0, 0, -1);
		faces[1].set(new Vector3D[] { vs[4], vs[5], vs[6], vs[7] }, normal,
				color[1], vt);
		normal.set(1, 0, 0);
		faces[2].set(new Vector3D[] { vs[0], vs[4], vs[7], vs[3] }, normal,
				color[2], vt);
		normal.set(-1, 0, 0);
		faces[3].set(new Vector3D[] { vs[1], vs[5], vs[6], vs[2] }, normal,
				color[3], vt);
		normal.set(0, 1, 0);
		faces[4].set(new Vector3D[] { vs[3], vs[2], vs[6], vs[7] }, normal,
				color[4], vt);
		normal.set(0, -1, 0);
		faces[5].set(new Vector3D[] { vs[0], vs[1], vs[5], vs[4] }, normal,
				color[5], vt);
	}

	public void move(Vector3D shift) {
		for (int i = 0; i < faces.length; i++) {
			if (faces[i].color != Draw3dHelper.EMPTY_COLOR) {
				faces[i].move(shift);
			}
		}
		center.add(shift);
	}

	public void rmove(Vector3D shift) {
		for (int i = 0; i < faces.length; i++) {
			if (faces[i].color != Draw3dHelper.EMPTY_COLOR) {
				faces[i].rmove(shift);
			}
		}
		center.sub(shift);
	}

	public void turn0(Matrix3D mt) {
		for (int i = 0; i < faces.length; i++) {
			if (faces[i].color != Draw3dHelper.EMPTY_COLOR) {
				faces[i].turn0(mt);
			}
		}
		center.set(mt.mult(center));
	}

	public void turn(Matrix3D mt, Vector3D centr) {
		rmove(centr);
		turn0(mt);
		move(centr);
	}

	public void turn(Matrix3D mt) {
		for (int i = 0; i < faces.length; i++) {
			if (faces[i].color != Draw3dHelper.EMPTY_COLOR) {
				faces[i].turn(mt, center);
			}
		}
	}

	public void turn(Vector3D axle, float alfa, Vector3D centr) {
		Matrix3D mt = Transform3D.getTurnMatrix(axle, alfa);
		turn(mt, centr);
	}

	public void draw(Draw3dHelper drawHelper) {
		for (int i = 0; i < faces.length; i++) {
			if (faces[i].color != Draw3dHelper.EMPTY_COLOR) {
				faces[i].draw(drawHelper);
			}
		}
	}

	public boolean isOnScreen(Draw3dHelper drwr) {
		return true;
	}

}
