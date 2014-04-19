package simple3d;

/**
 * Utility class to create rotation matrixes
 * 
 * @author Alexander Lisovets
 *
 */


public class Transform3D {
	
	/**
	 * creates a rotation matrix
	 * @param axis a vector of the rotation axis 
	 * @param angle a rotation angle  
	 * @return the rotation matrix
	 */
	public static Matrix3D getTurnMatrix(Vector3D axis, float angle){				
		float len = (float)Math.sqrt(axis.z * axis.z + axis.x * axis.x + axis.y * axis.y );
		
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float x = axis.x / len;
		float y = axis.y / len;
		float z = axis.z / len;
		
		
		float x0 = cos + (1 - cos)* x * x;
		float x1 = (1 - cos)* x * y - sin * z;
		float x2 = (1 - cos)* x * z  + sin * y;
		
		float y0 = (1 - cos) * y * x + sin * z;
		float y1 = cos + (1 - cos) * y * y;
		float y2 = (1 - cos) * y * z - sin * x;
		
		float z0 = (1 - cos) * z * x - sin * y;
		float z1 = (1 - cos) * z * y + sin * x;
		float z2 = cos + (1 - cos) * z * z;
		
		Matrix3D matrix =  new Matrix3D(x0, x1, x2, y0, y1, y2, z0, z1, z2);
		return matrix ;
	}
	
	
	
	
	public static Matrix3D getTurnMatrix2(Vector3D axis, float angle){				
		float lenZ = (float)Math.sqrt(axis.z * axis.z + axis.x * axis.x);
		float len = (float)Math.sqrt(axis.z * axis.z + axis.x * axis.x + axis.y * axis.y );
		
		float btSin;
		float btCos;
		
		float alSin = axis.y / len;
		float alCos = lenZ /len;			
		if(lenZ > 0.00001){				
			btSin = -axis.x /lenZ;
			btCos = axis.z / lenZ;					
		}			
		else{
			btSin = 0;
			btCos = len;
		}
													
		float gmSin = (float)-Math.sin(angle);
		float gmCos = (float)Math.cos(angle);		
		// -bt
		Matrix3D matrix = new Matrix3D (btCos, 0, -btSin, 0, 1, 0, btSin, 0, btCos);
		//  -al
		Matrix3D matrix2 = new Matrix3D(1, 0, 0,  0, alCos, alSin,  0, -alSin, alCos);
		matrix.mult(matrix2);
		
		//  gm
		matrix2.setMatrix(gmCos, -gmSin, 0,  gmSin, gmCos, 0,  0, 0, 1);
		matrix.mult(matrix2);
		
		//  al
		matrix2.setMatrix(1, 0, 0,  0, alCos, -alSin,  0, alSin, alCos);
		matrix.mult(matrix2);
		
		//* bt
		matrix2.setMatrix(btCos, 0, btSin, 0, 1, 0, -btSin, 0, btCos);
		matrix.mult(matrix2);
		
		return matrix;
	}		
	
	
	/**
	 * creates a rotation matrix to rotate around the X axis 
	 * @param angle a rotation angle  
	 * @return the rotation matrix
	 */
	public static Matrix3D getTurnMatrixX(float angle){			
			float sin = (float)-Math.sin(angle);
			float cos = (float)Math.cos(angle);
			Matrix3D matrix = new Matrix3D(1, 0, 0,  0, cos, -sin,  0, sin, cos);		
			return matrix;
	}
	
	/**
	 * creates a rotation matrix to rotate around the Y axis 
	 * @param angle a rotation angle  
	 * @return the rotation matrix
	 */
	public static Matrix3D getTurnMatrixY(float angle){		
		float sin = (float)-Math.sin(angle);
		float cos = (float)Math.cos(angle);	
		Matrix3D matrix = new Matrix3D(cos, 0, sin, 0, 1, 0, -sin, 0, cos);	
		return matrix;
	}
	
	/**
	 * creates a rotation matrix to rotate around the Z axis 
	 * @param angle a rotation angle  
	 * @return the rotation matrix
	 */
	public static Matrix3D getTurnMatrixZ(float angle){		
		float sin = (float)-Math.sin(angle);
		float cos = (float)Math.cos(angle);		
		Matrix3D matrix = new Matrix3D(cos, -sin, 0,  sin, cos, 0,  0, 0, 1);	
		return matrix;
	}	
	
}
