package simple3d.gameentity;

import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;
import simple3d.shape.Ball;
import simple3d.shape.Cone;
import simple3d.shape.Shape;

/**
 * To draw a 3d snowman
 */
public class Snowman extends Entity{
	
	public Snowman(float s) {
		shapes = new Shape[20];
		center = new Vector3D(0,  20 * s, 0);
		
		shapes[0] = new Ball(10 * s, 0xf0f0ff);
		shapes[0].move(new Vector3D(0, 6 * s, 0));
			
		shapes[1] = new Ball(8 * s, 0xf0f0ff);
		shapes[1].move(new Vector3D(0,  20 * s, 0));
		
		shapes[11] = new Ball(4 * s, 0xf0f0ff);
		shapes[11].move(new Vector3D(-11 * s,  22 * s, -1* s));
		
		shapes[12] = new Ball(4 * s, 0xf0f0ff);
		shapes[12].move(new Vector3D(11 * s,  22 * s, -1* s));
		
		
		//head
		shapes[2] = new Ball(6 * s, 0xf0f0ff);
		shapes[2].move(new Vector3D(0,  32 * s, 0));
		
		shapes[3] = new Cone(6f * s, 0f, 1f * s, 6.28f, 10, 0xff4040, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		Matrix3D matrixX90 = Transform3D.getTurnMatrixX(1.6f);
		shapes[3].turn(matrixX90);
		shapes[3].move(new Vector3D( 0, 32f * s, - 8f * s ));
		
		shapes[4] = new Ball(0.5f * s, 0x000010);
		shapes[4].turn(matrixX90);
		shapes[4].move(new Vector3D( -2 * s, 33f * s, - 4.0f * s ));
	
		shapes[5] = new Ball(0.5f * s, 0x000010);
		shapes[5].turn(matrixX90);
		shapes[5].move(new Vector3D( 2 * s, 33f * s, -3.0f * s ));
		
		shapes[6] = new Ball(0.5f * s, 0x000010);
		shapes[6].turn(matrixX90);
		shapes[6].move(new Vector3D( 0 , 29.9f * s, - 4.15f * s ));
		
		shapes[7] = new Ball(0.5f * s, 0x000010);
		shapes[7].turn(matrixX90);
		shapes[7].move(new Vector3D( -1.2f * s, 30f * s, - 4.05f * s ));
		
		shapes[8] = new Ball(0.5f * s, 0x000010);
		shapes[8].turn(matrixX90);
		shapes[8].move(new Vector3D( 1.2f * s, 30f * s, - 4.05f * s ));
		
		shapes[9] = new Ball(0.5f * s, 0x000010);
		shapes[9].turn(matrixX90);
		shapes[9].move(new Vector3D( 2.4f * s, 30.2f * s, - 3.9f * s ));
		
		shapes[10] = new Ball(0.5f * s, 0x000010);
		shapes[10].turn(matrixX90);
		shapes[10].move(new Vector3D( -2.4f * s, 30.2f * s, - 3.9f * s ));
		//end head
		
		Matrix3D mtz = Transform3D.getTurnMatrixZ(0.15f);
			
		shapes[13] = new Cone(6f* s, 3.4f * s, 5.2f * s, 6.28f, 20, 0x20ff30, Draw3dHelper.FRONT_SIDE_IS_LIT );
		shapes[13].move(new Vector3D(0.75f * s , 38.6f * s, 0));
		shapes[13].turn(mtz, new Vector3D(0.7f * s, 39f * s, 0));
		
		shapes[14] = new Cone(1f * s, 5.22f * s, 5.25f * s, 6.28f, 20, 0x20ff30, Draw3dHelper.FRONT_SIDE_IS_LIT );
		shapes[14].move(new Vector3D(0.75f* s , 36.1f * s, 0));
		shapes[14].turn(mtz, new Vector3D(0.7f * s, 39 * s, 0));
		
		shapes[15] = new Cone(1f, 0f, 3.4f * s, 6.28f, 20, 0x20ff30, Draw3dHelper.FRONT_SIDE_IS_VISIBLE );
		shapes[15].move(new Vector3D(0.75f * s , 41.5f * s, 0));
		shapes[15].turn(mtz, new Vector3D(0.7f * s, 39f * s, 0));
		
	}

}
