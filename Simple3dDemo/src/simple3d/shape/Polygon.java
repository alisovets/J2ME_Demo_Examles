package simple3d.shape;
import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.PixelZ;
import simple3d.Transform3D;
import simple3d.Vector3D;

/**
 * Plane 3d polygon
 * 
 * @author Alexander Lisovets
 *
 */
public class Polygon implements Shape {
	
	public Vector3D vertexes[] ;  //vertex array
	public Vector3D normal;		//normal vector	
	public int color;			//color
	public int viewType; // defines how to show and light up the front and back sides

	
	
	
	public Polygon(int numOfVerts){
		vertexes = new Vector3D[numOfVerts];
		for(int i = 0; i < vertexes.length; i++)
			vertexes[i] = new Vector3D();
		normal = new Vector3D(1, 0, 0);	
	}
	
	public Polygon(Vector3D []pv, Vector3D normal, int color, int vt){
		this(pv.length);
		set(pv, normal, color, vt);
	}
	
	public Polygon(Polygon pl){
		this(pl.vertexes.length);
		set(pl);
	}
	
	
	/**
	 * sets the polygon is defined with the array of vertexes, the normal vector, color and view type 
	 * @param pv
	 * @param normal
	 * @param color
	 * @param vt defines how to show and light up the front and back sides
	 */
	public void set(Vector3D []pv, Vector3D normal, int color, int vt){
		if(pv.length != vertexes.length){
			if(pv.length >= 3){
				vertexes = new Vector3D[pv.length];
			}
			else if(vertexes.length != 3){
				vertexes = new Vector3D[3];
			}
			for(int i = 0; i < vertexes.length; i++ ){
				vertexes[i] = new Vector3D();	
			}
		}
		for(int i = 0; i < vertexes.length; i++){
			if(i < pv.length){
				vertexes[i].set(pv[i]);
			}
			else{
				vertexes[i].set(pv[pv.length -1]);
			}
		}
		this.normal.set(normal);
		this.color = color;	
		viewType = vt;
	}
	
	/**
	 * sets the fields with the values from the passed polygon    
	 * @param pl
	 */
	public void set(Polygon pl){
		if(pl.vertexes.length != vertexes.length){
			vertexes = new Vector3D[pl.vertexes.length];
			for(int i = 0; i < vertexes.length; i++ ){
				vertexes[i] = new Vector3D();	
			}
		}
		for(int i = 0; i < vertexes.length; i++){
				vertexes[i].set(pl.vertexes[i]);
		}
		normal.set(pl.normal);
		color = pl.color;
		viewType = pl.viewType;
	}
	
	
	public void turn0(Matrix3D mt) {
		for(int i = 0;  i < vertexes.length; i++)
			vertexes[i] = mt.mult(vertexes[i]);				
		normal = mt.mult(normal);
	}

	public void turn(Matrix3D mt, Vector3D centr) {
		for(int i = 0; i < vertexes.length; i++){
			vertexes[i].sub(centr);
			vertexes[i] = mt.mult(vertexes[i]);		
			vertexes[i].add(centr);
		}	
		normal = mt.mult(normal);
	}
	
	public void turn(Matrix3D mt) {
		Vector3D centr = new Vector3D(vertexes[0]);
		int length = vertexes.length;
		for(int i = 1; i < length; i++){
			centr.add(vertexes[i]);
		}
		centr.set(centr.x /length, centr.y / length , centr.z /length);
		
		for(int i = 0; i < length; i++){
			vertexes[i].sub(centr);
			vertexes[i] = mt.mult(vertexes[i]);		
			vertexes[i].add(centr);
		}	
		normal = mt.mult(normal);
	}
	
	public void turn(Vector3D axle, float alfa, Vector3D centr){
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		turn(mt, centr);	
	}
	
	public void move(Vector3D sh) {
		for(int i = 0; i < vertexes.length; i++)					
			vertexes[i].add(sh);
	}

	public void rmove(Vector3D sh) {
		for(int i = 0; i < vertexes.length; i++)					
			vertexes[i].sub(sh);
	}
	
	public boolean isOnScreen(Draw3dHelper drwr){			
		return true;		
	}
	
	public void draw(Draw3dHelper drwr){		
		PixelZ p[] = new PixelZ[3];
		PixelZ pTmp;
		
		int curCol = (255 << 24) | drwr.getScreenColor(color, normal, vertexes[0], viewType); 
		if(curCol != Draw3dHelper.EMPTY_COLOR){
			for(int i = 0; i < 3; i++){
				p[i] = new PixelZ((int)drwr.getScreenX(vertexes[i]), (int)drwr.getScreenY(vertexes[i]), 1/vertexes[i].z);		
			}				
			drwr.dr3angl(p, curCol);
			for(int i = 3; i < vertexes.length; i++){
				pTmp = p[1];
				p[1] = p[2];
				p[2] = pTmp;
				p[2].set((int)drwr.getScreenX(vertexes[i]), (int)drwr.getScreenY(vertexes[i]), 1/vertexes[i].z);
				drwr.dr3angl(p, curCol);
			}
		}
	}		
		
	public void normalInfo(){
		System.out.println("normal= " + normal.x+ "  " + normal.y +"  "+normal.z);
	}		

	
}
