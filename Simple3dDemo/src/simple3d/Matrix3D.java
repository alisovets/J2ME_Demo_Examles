package simple3d;

/**
 * 3d matrix with methods of the matrix mathematics 
 * 
 * @author ALexander Lisovets
 *
 */
public class Matrix3D {
	float m[][] = new float[3][3];
	
	/**
	 * Default constructor
	 */
	public Matrix3D(){
		setMatrix();
	}
	
	/**
	 * Constructor 
	 * @param mt
	 */
	public Matrix3D(Matrix3D mt){		
		setMatrix(mt);	
	}
	
	
	/**
	 * Cnstructor
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param y0
	 * @param y1
	 * @param y2
	 * @param z0
	 * @param z1
	 * @param z2
	 */
	public Matrix3D(float x0, float x1, float x2, float y0, float y1, float y2, float z0, float z1, float z2){	
		setMatrix(x0, x1, x2, y0, y1, y2, z0, z1, z2);		
	}		
	
	/**
	 * sets all values
	 * @param x0
	 * @param x1
	 * @param x2
	 * @param y0
	 * @param y1
	 * @param y2
	 * @param z0
	 * @param z1
	 * @param z2
	 */
	public void setMatrix(float x0, float x1, float x2, float y0, float y1, float y2, float z0, float z1, float z2){		
		m[0][0] = x0;
		m[0][1] = x1;
		m[0][2] = x2;		
		m[1][0] = y0;
		m[1][1] = y1;
		m[1][2] = y2;		
		m[2][0] = z0;
		m[2][1] = z1;
		m[2][2] = z2;	
	}
	
	/**
	 * sets all values in zero
	 */
	public void setMatrix(){		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				m[i][j]= 0;
	}
	
	/**
	 * sets all fields to the values taken from the parameter matrix
	 * @param mt
	 */
	public void setMatrix(Matrix3D mt){		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				m[i][j]= mt.m[i][j];
	}	
	
	/**
	 * calculates and returns the result of the multiplication of two matrices
	 * @param m1 
	 * @param m2
	 * @return matrix is the result of the multiplication of two matrices
	 */
	public static Matrix3D mult(Matrix3D m1, Matrix3D m2){
		Matrix3D m3 = new Matrix3D();
		for(int i = 0; i < 3; i++)
			for(int j= 0; j < 3; j++)
				m3.m[i][j] = m1.m[i][0] * m2.m[0][j]+ m1.m[i][1] * m2.m[1][j]+ m1.m[i][2] * m2.m[2][j];
		return m3;
	}

	/**
	 * 
	 * Performs multiplication the current matrix to the matrix passed in the parameter and 
	 * puts the result in current matrix   
	 * @param mt
	 */
	public void mult(Matrix3D mt){
		m = mult(mt, this).m;																	
	}
	
	
	/**
	 * Sums two matrices 
	 * @param m1
	 * @param m2
	 * @return the matrix that is the result of the result of adding two matrices
	 */
	public static Matrix3D add(Matrix3D m1, Matrix3D m2){				
		Matrix3D m3 = new Matrix3D();
		for(int i = 0; i < 3; i++)
			for(int j= 0; j < 3; j++)
				m3.m[i][j] = m1.m[i][j]+ m2.m[i][j];
		return m3;
	}
	
	
	/**
	 * Performs adding the matrix passed in the parameter to the current matrix 
	 * puts the result in current matrix   
	 * @param mt the matrix to add to the current one
	 */
	public void add(Matrix3D mt){
		for(int i = 0; i < 3; i++)
			for(int j= 0; j < 3; j++)
				m[i][j] = m[i][j]+ mt.m[i][j];
	}
	
	/**
	 * calculates and returns the result of the multiplication the current matrix of the vector
	 * @param mt matrix
	 * @param v  vector
	 * @return the vector that is the result of the multiplication
	 */
	public static Vector3D mult(Matrix3D mt, Vector3D v){
		Vector3D  v2 = new Vector3D();
		v2.x =  mt.m[0][0] * v.x + mt.m[0][1] * v.y + mt.m[0][2] * v.z;
		v2.y =  mt.m[1][0] * v.x + mt.m[1][1] * v.y + mt.m[1][2] * v.z;
		v2.z =  mt.m[2][0] * v.x + mt.m[2][1] * v.y + mt.m[2][2] * v.z;		
		return v2;
	} 
	
	
	/**
	 * Performs multiplication the current matrix to the vector passed in the parameter and 
	 * return the result vector   
	 * @param v - vector
	 * @return the vector that is result of the multiplication
	 */
	public Vector3D mult(Vector3D v){
		Vector3D  v2 = new Vector3D();
		v2.x =  m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z;
		v2.y =  m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z;
		v2.z =  m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z;		
		return v2;
	}
	
	
	public String toString() {
		String s = "Matrix: \n";
			for(int i = 0; i < 3; i++ ){
				for(int j = 0; j < 3; j++ ){
					s += m[i][j] + ", \t";
				}	
				s +="\n";
		}
		return s;
	}
}