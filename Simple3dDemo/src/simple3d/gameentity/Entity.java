package simple3d.gameentity;

import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;
import simple3d.shape.Shape;

/**
 * to create and handle complex 3D entity
 * 
 * @author Alexander Lisoves
 * 
 */
public abstract class Entity implements Shape {

	protected final Matrix3D turnMatrix = new Matrix3D(1, 0, 0, 0, 1, 0, 0, 0,
			1);

	protected Shape[] shapes;
	protected Vector3D center;

	public void turn0(Matrix3D matrix) {
		for (int i = 0; i < shapes.length; i++) {
			if (shapes[i] != null) {
				shapes[i].turn0(matrix);
			}
		}
		turnMatrix.mult(matrix);
		center.set(matrix.mult(center));
	}

	public void turn(Matrix3D matrix) {
		for (int i = 0; i < shapes.length; i++) {
			if (shapes[i] != null) {
				shapes[i].turn(matrix, center);
			}
		}
		turnMatrix.mult(matrix);
	}

	public void turn(Matrix3D matrix, Vector3D centr) {
		for (int i = 0; i < shapes.length; i++) {
			if (shapes[i] != null) {
				shapes[i].turn(matrix, centr);
			}
		}
		center.sub(centr);
		center = matrix.mult(center);
		center.add(centr);
		turnMatrix.mult(matrix);
	}

	public void turn(Vector3D axle, float angle, Vector3D centr) {
		Matrix3D matrix = Transform3D.getTurnMatrix(axle, angle);
		for (int i = 0; i < shapes.length; i++) {
			if (shapes[i] != null) {
				shapes[i].turn(matrix, centr);
			}
		}
		center.sub(centr);
		center = matrix.mult(center);
		center.add(centr);
		turnMatrix.mult(matrix);
	}

	public void move(Vector3D shift) {
		for (int i = 0; i < shapes.length; i++) {
			if (shapes[i] != null) {
				shapes[i].move(shift);
			}
		}
		center.add(shift);
	}

	public void rmove(Vector3D shift) {
		for (int i = 0; i < shapes.length; i++) {
			if (shapes[i] != null) {
				shapes[i].move(shift);
			}
		}
		center.sub(shift);
	}

	public boolean isOnScreen(Draw3dHelper drawHelper) {
		// TODO stub
		return true;
	}

	public void draw(Draw3dHelper drawHelper) {
		for (int i = 0; i < shapes.length; i++) {
			if (shapes[i] != null) {
				shapes[i].draw(drawHelper);
			}
		}

	}

}
