package simple3d.shape;

import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;

/**
 * Plane 3D Triangle
 * 
 * @author Alexander Lisovets
 * 
 */
public class Triangle extends Polygon { // implements Shape {

	public Triangle(Vector3D v0, Vector3D v1, Vector3D v2, Vector3D normal,
			int color, int vt) {
		super(3);
		set(v0, v1, v2, normal, color, vt);
	}

	public Triangle(Triangle tr) {
		super(tr);
	}

	/**
	 * sets all the fields
	 * 
	 * @param v0
	 *            the vertex
	 * @param v1
	 *            the vertex
	 * @param v2
	 *            the vertex
	 * @param normal
	 *            the normal vector
	 * @param color
	 * @param vt
	 *            the view type
	 */
	public void set(Vector3D v0, Vector3D v1, Vector3D v2, Vector3D normal,
			int color, int vt) {
		vertexes[0].set(v0);
		vertexes[1].set(v1);
		vertexes[2].set(v2);
		this.normal.set(normal);
		this.color = color;
		viewType = vt;
	}

	/**
	 * sets all fields with the values taken from the passed parameter triangle
	 * 
	 * @param tr
	 */
	public void set(Triangle tr) {
		super.set(tr);
	}

	public void turn(Vector3D axle, float alfa, Vector3D centr) {
		Matrix3D mt = Transform3D.getTurnMatrix(axle, alfa);
		turn(mt, centr);
	}

}
