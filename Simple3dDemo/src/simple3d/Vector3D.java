package simple3d;
/**
 * 3D Vector with a couple of vector mathematics method. 
 * @author Alexander Lisovets
 *
 */
public class Vector3D {
	public float x, y, z;
	
	public Vector3D(){
	}
	
	public Vector3D(Vector3D p){
		x = p.x;  
		y = p.y;
		z = p.z;		
	}
	
	public Vector3D(float px, float py, float pz){
		x = px;  
		y = py;
		z = pz;		
	}
	
	/**
	 * sets all the fields zero
	 */
	public void set(){
		x = y = z = 0;
	}
	
	/**
	 * sets all the fields with the parameter values 
	 * @param px
	 * @param py
	 * @param pz
	 */
	public void set(float px, float py, float pz){
		x = px;  
		y = py;
		z = pz;
	}
	
	/**
	 * sets all fields to the values taken from the parameter vector
	 * @param p
	 */
	public void set(Vector3D p){
		x = p.x;  
		y = p.y;
		z = p.z;
	}			
	
	/**
	 * adds values of the parameter vector to the current vector 
	 * @param p
	 */
	public void add(Vector3D p){
		x += p.x;
		y += p.y;
		z += p.z;					
	}
	
	/**
	 * subtracts the parameter vector from the current 
	 * @param p
	 */
	public void sub(Vector3D p){
		x -= p.x;
		y -= p.y;
		z -= p.z;
	}
	
	public String toString() {
		return "Vector3D("+  x + ", " + y + ", " + z +  ")";
	}
}


