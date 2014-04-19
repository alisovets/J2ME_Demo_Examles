package simple3d.shape;

import simple3d.Vector3D;

/**
 * A plane 3d rectangle
 * 
 * @author Alexander Lisovets
 * 
 */
public class Rectangle extends Polygon {

	public Rectangle() {
		super(4);
	}

	public Rectangle(Vector3D[] vs, Vector3D normal, int color, int vt) {
		super(vs, normal, color, vt);
	}

	/**
	 * Creates a rectangle with the specified left top vertex and the specified
	 * width, height, color
	 * 
	 * @param vertex
	 * @param width
	 * @param height
	 * @param color
	 * @param vt
	 */
	public Rectangle(Vector3D vertex, float width, float height, int color,
			int vt) {
		super(4);
		set(vertex, width, height, color, vt);
	}

	public Rectangle(Rectangle rc) {
		super(rc);
	}

	/**
	 * sets the rectangle fields
	 * 
	 * @param vertex
	 * @param width
	 * @param height
	 * @param color
	 * @param vt
	 */
	public void set(Vector3D vertex, float width, float height, int color,
			int vt) {
		vertexes[0].set(vertex);
		vertexes[1].set(vertex.x + width, vertex.y, vertex.z);
		vertexes[2].set(vertex.x + width, vertex.y - height, vertex.z);
		vertexes[3].set(vertex.x, vertex.y - height, vertex.z);
		this.normal.set(0, 0, 1);
		this.color = color;
		viewType = vt;
	}

}