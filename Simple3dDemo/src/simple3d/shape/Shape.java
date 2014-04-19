package simple3d.shape;
import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Vector3D;

/**
 * 3D shape
 * 
 * @author Alexander Lisovets 
 *
 */
public interface Shape {		
	/**
	 * Performs the rotation around the zero point (0, 0, 0)
	 * @param matrix rotation matrix
	 */
	public void turn0(Matrix3D matrix); 
	
	
	/**
	 * Performs the rotation around the center object point 
	 * @param matrix rotation matrix
	 */
	public void turn(Matrix3D matrix);
	
	
	/**
	 * Performs the rotation around the specified center point
	 * @param matrix rotation matrix
	 * @param centr
	 */
	public void turn(Matrix3D matrix, Vector3D centr);	//turning around center
	
	/**
	 * Performs the rotation on the specified angle around the specified center point and axis 
	 * @param axle 
	 * @param angle
	 * @param centr
	 */
	public void turn(Vector3D axle, float angle, Vector3D centr);
	
	/**
	 * Moves the object on the shift value specified by the vector   
	 * @param shift
	 */
	public void move(Vector3D shift); 
	
	/**
	 * 
	 * Moves the object on the shift value specified by the vector in the reverse direction    
	 * @param shift
	 * @param shift
	 */
	public void rmove(Vector3D shift); 
	
	/**
	 * checks if the object is inside on the screen 
	 * @param drawHelper  DrawHelper by which it is drawn
	 * @return false if the whole object is out of the screen borders
	 */
	public boolean isOnScreen(Draw3dHelper drawHelper);
	
	/**
	 * Prepares to draw this object
	 * @param drawHelper DrawHelper by which it is drawn
	 */
	public void draw(Draw3dHelper drawHelper); 
}
