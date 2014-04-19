package simple3d.gameentity;

import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;
import simple3d.shape.Cone;
import simple3d.shape.Shape;

public class Scene1 extends Entity {
	private static final int BASE_COLOR = 0xffcf2f;
	private static final int COLONE_COLOR = 0x1010ff;
	private static final int BASE_FACET_NUMBER = 8;
	private static final float PI = (float) Math.PI;
	private Shape[] rotates;

	public Scene1(float s) {
		shapes = new Shape[20];
		rotates = new Shape[4];
		center = new Vector3D(0, 0 * s, 0);

		Matrix3D matrixPiY = Transform3D.getTurnMatrixY(PI);
		Matrix3D matrixPi2Y = Transform3D.getTurnMatrixY(PI / 2);
		Matrix3D matrixPi4Y = Transform3D.getTurnMatrixY(PI / 4);

		int i = 0;
		shapes[i] = new Cone(1 * s, 3 * s, 30 * s, 2 * PI, BASE_FACET_NUMBER,
				BASE_COLOR, Draw3dHelper.BOTH_OF_SIDES_ARE_LIT);
		shapes[++i] = new Cone(5 * s, 30 * s, 30 * s, 2 * PI,
				BASE_FACET_NUMBER, BASE_COLOR, Draw3dHelper.FRONT_SIDE_IS_LIT);
		shapes[i].move(new Vector3D(0, -3f * s, 0));

		shapes[++i] = new Cone(46 * s, 2 * s, 4 * s, 2 * PI, BASE_FACET_NUMBER,
				COLONE_COLOR, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		shapes[i].move(new Vector3D(0, 23f * s, 0));

		shapes[++i] = new Snowman(s / 2.5f);
		shapes[i].move(new Vector3D(0, 0, -25 * s));
		rotates[0] = shapes[i];

		shapes[++i] = new Snowman(s / 2.5f);
		shapes[i].move(new Vector3D(0, 0, -25 * s));
		shapes[i].turn0(matrixPi2Y);
		rotates[1] = shapes[i];

		shapes[++i] = new Snowman(s / 2.5f);
		shapes[i].move(new Vector3D(0, 0, -25 * s));
		shapes[i].turn0(matrixPiY);
		rotates[2] = shapes[i];

		shapes[++i] = new Snowman(s / 2.5f);
		shapes[i].move(new Vector3D(0, 0, -25 * s));
		shapes[i].turn0(matrixPiY);
		shapes[i].turn0(matrixPi2Y);
		rotates[3] = shapes[i];

		shapes[++i] = new Tree(s * 1.5f);
		shapes[i].move(new Vector3D(0, 14 * s, -22 * s));
		shapes[i].turn0(matrixPi4Y);

		shapes[++i] = new Tree(s * 1.5f);
		shapes[i].move(new Vector3D(0, 14 * s, -22 * s));
		shapes[i].turn0(matrixPi4Y);
		shapes[i].turn0(matrixPi2Y);

		shapes[++i] = new Tree(s * 1.5f);
		shapes[i].move(new Vector3D(0, 14 * s, -22 * s));
		shapes[i].turn0(matrixPi4Y);
		shapes[i].turn0(matrixPiY);

		shapes[++i] = new Tree(s * 1.5f);
		shapes[i].move(new Vector3D(0, 14 * s, -22 * s));
		shapes[i].turn0(matrixPi4Y);
		shapes[i].turn0(matrixPiY);
		shapes[i].turn0(matrixPi2Y);

		shapes[++i] = new Cone(16 * s, 0, 30 * s, 2 * PI,
				2 * BASE_FACET_NUMBER, BASE_COLOR,
				Draw3dHelper.BOTH_OF_SIDES_ARE_LIT);
		shapes[i].move(new Vector3D(0, 39f * s, 0));
	}

	public void turnRotats(float angle) {
		Vector3D axis = turnMatrix.mult(new Vector3D(0, 1, 0));

		Matrix3D matrix = Transform3D.getTurnMatrix(axis, angle);
		for (int i = 0; i < rotates.length; i++) {
			rotates[i].turn(matrix, new Vector3D(((Entity) rotates[i]).center));
		}
	}

	public void turnMainAxis(float angle) {
		Vector3D axis = turnMatrix.mult(new Vector3D(0, 1, 0));
		turn(axis, angle, new Vector3D(center));
	}

}
