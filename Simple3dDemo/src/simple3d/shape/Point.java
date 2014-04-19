package simple3d.shape;

import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;

/**
 * 3D point
 * 
 * @author Alexander Lisovets
 * 
 */
public class Point implements Shape {
	public Vector3D vertex;
	public Vector3D normal;
	public int col;// color
	public int viewType; // defines how to show and light up the front and back
							// sides

	public Point() {
		vertex = new Vector3D();
		normal = new Vector3D();
	}

	public Point(Point p) {
		vertex = new Vector3D(p.vertex);
		normal = new Vector3D(p.normal);
		col = p.col;
		viewType = p.viewType;
	}

	public Point(Vector3D p, Vector3D n, int c, int vt) {
		vertex = new Vector3D(p);
		normal = new Vector3D(n);
		col = c;
		viewType = vt;
	}

	public void set() {
		vertex.set();
		normal.set();
		col = 0;
		viewType = Draw3dHelper.BOTH_OF_SIDES_ARE_LIT;
	}

	public void set(Point p) {
		vertex.set(p.vertex);
		normal.set(p.normal);
		col = p.col;
		viewType = p.viewType;
	}

	public void set(Vector3D p, Vector3D n, int c, int vt) {
		vertex.set(p);
		n.set(n);
		col = c;
		viewType = vt;
	}

	public void setPoint(Point p) {
		vertex.x = p.vertex.x;
		vertex.y = p.vertex.y;
		vertex.z = p.vertex.z;
		normal.x = p.normal.x;
		normal.y = p.normal.y;
		normal.z = p.normal.z;
		col = p.col;
	}

	public void turn0(Matrix3D mt) {
		vertex = mt.mult(vertex);
		normal = mt.mult(normal);
	}
	
	public void turn(Matrix3D mt) {
		vertex = mt.mult(vertex);
		normal = mt.mult(normal);
	}

	public void turn(Matrix3D mt, Vector3D centr) {
		vertex.sub(centr);
		vertex = mt.mult(vertex);
		vertex.add(centr);
		normal = mt.mult(normal);
	}

	public void turn(Vector3D axle, float alfa, Vector3D centr) {
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		vertex.sub(centr);
		vertex = mt.mult(vertex);		
		vertex.add(centr);	
		normal = mt.mult(normal);
	}

	public void move(Vector3D sh) {
		vertex.add(sh);
	}

	public void rmove(Vector3D sh) {
		vertex.sub(sh);
	}

	public boolean isOnScreen(Draw3dHelper drawHelper) {
		drawHelper.isOnScreen(vertex);
		return true;
	}

	public void draw(Draw3dHelper drawHelper) {
		int screenColor = (255 << 24)
				| drawHelper.getScreenColor(col, normal, vertex, viewType);
		if (screenColor != Draw3dHelper.EMPTY_COLOR) {
			drawHelper.drawPoint(vertex, screenColor);
		}
	}

	

}