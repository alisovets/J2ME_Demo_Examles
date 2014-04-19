package simple3d.gameentity;

import simple3d.Draw3dHelper;
import simple3d.Vector3D;
import simple3d.shape.Cone;
import simple3d.shape.Shape;

/**
 * to create a simple 3D model of a tree.
 *  
 * @author Alexander Lisovets
 *
 */
public class Tree extends Entity{
	
	public Tree(float size){		
		shapes = new Shape[4];	
		center = new Vector3D(0,  0, 0);

		
		shapes[0] = new Cone(5f * size, 0f, 2.5f * size, 6.28f, 12, 0xff00, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		shapes[0].move(new Vector3D(0, -0.385f * size, 0));
		shapes[1] = new Cone(5f * size, 0f, 4.05f * size, 6.28f, 12, 0xff00, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		shapes[1].move(new Vector3D(0.1f * size, -3.05f * size, 0));
		shapes[2] = new Cone(5f * size, 0f, 5f * size, 6.28f, 12, 0xff00, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		shapes[2].move(new Vector3D(0, -5.57f * size, 0));
		shapes[3] = new Cone(2.5f * size, 0.62f * size, 0.5f * size, 6.28f, 12, 0xefef00, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		shapes[3].move(new Vector3D(0, -8.75f * size , 0));
		
		

	}

}
