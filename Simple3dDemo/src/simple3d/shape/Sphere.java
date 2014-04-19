package simple3d.shape;


import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.PixelZ;
import simple3d.Transform3D;
import simple3d.Vector3D;




/**
 * 3D Ball 
 * To draw a sphere or a sphere sector 
 * 
 * @author Alexander Lisovets
 *
 */
public class Sphere implements Shape {
	public Vector3D center;
	public float radius;
	public float segmentHeight;
	public int color;
	public float facetAngle;
	public int facetsNum;
	public Matrix3D turnMatrix;
	public int viewType;
	
	
	 /**
	  *  Creates an empty ball
	  */
	protected  Sphere(){		
		center = new Vector3D();		
		turnMatrix = new Matrix3D();
	
	}
	
	/** 
	 * creats the ball or segment
	 * sets parameters of the ball
	 * @param radius
	 * @param segmentHeight - the height of a sphere sector 
	 * if segmentHeight < 2 * radius  then a sphere sector is created, else - a whole sphere.
	 * @param facetsNum - the number of a facets on the circle. 
	 * @param color
	 */
	public Sphere( float radius, float segmentHeight, int facetsNum, int color){
		center = new Vector3D();				
		turnMatrix = new Matrix3D();						
		set(radius, segmentHeight, facetsNum, color);
	}	
	
	/** 
	 * creates the ball or segment
	 * @param radius
	 * @param segmentHeight - the height of a sphere sector 
	 * if segmentHeight < 2 * radius  then a sphere sector is created, else - a whole sphere.
	 * @param facetsNum - the number of a facets on the circle. 
	 * @param color
	 * @param viewType a visibility and lighting type
	 */
	public Sphere( float radius, float segmentHeight, int facetsNum, int color, int view){
		this( radius, segmentHeight, facetsNum, color);
		this.viewType = view;
	}
	
	/**
	 * Create ball as copy of the passed parameter
	 * @param ball
	 */
	public Sphere(Sphere ball){
		center = new Vector3D();		
		turnMatrix = new Matrix3D();
		set(ball);	
	}
	
	/** 
	 * sets parameters of the ball
	 * @param radius
	 * @param segmentHeight - the height of a sphere sector 
	 * if segmentHeight < 2 * radius  then a sphere sector is created, else - a whole sphere.
	 * @param facetsNum - the number of a facets on the circle. 
	 * @param color
	 */
	public void set(float radius, float segmentHeight, int facetsNum, int color){
		center.set();				
		this.facetsNum = facetsNum;
		this.radius = radius;	
		if(segmentHeight == 0)
			this.segmentHeight = 2 * radius;
		else
			this.segmentHeight = segmentHeight; 
		if(segmentHeight > radius * 2){
			this.segmentHeight = 2 * radius;
		}
		turnMatrix.setMatrix(1,0,0, 0,1,0, 0,0,1);						
		facetAngle = (float)(Math.PI * 2 / facetsNum);								
		this.color = color;
		viewType = 0;
	}
	
	/** 
	 * sets parameters of the ball
	 * @param radius
	 * @param segmentHeight - the height of a sphere sector 
	 * if segmentHeight < 2 * radius  then a sphere sector is created, else - a whole sphere.
	 * @param facetsNum - the number of a facets on the circle. 
	 * @param color
	 * @param viewType a visibility and lighting type
	 */
	public void set( float radius, float segmentHeight, int facetsNum, int color, int viewType){
		set( radius, segmentHeight, facetsNum, color);
		this.viewType = viewType;
	}
	
	public void set(Sphere ball){
		center.set(ball.center);		
		turnMatrix.setMatrix(ball.turnMatrix);
		facetAngle = ball.facetAngle;
		facetsNum = ball.facetsNum;
		color = ball.color;
		radius = ball.radius;		
		segmentHeight = ball.segmentHeight;
		viewType = ball.viewType;
	}
	
	public boolean isOnScreen(Draw3dHelper drwr) {
		//TODO
		return true;
	}

	public void move(Vector3D sh) {
		center.add(sh);
	}

	
	public void rmove(Vector3D sh) {
		center.sub(sh);
	}

	public void turn0(Matrix3D mt) {
		turnMatrix = Matrix3D.mult(mt, turnMatrix);
		center = mt.mult(center);

	}

	public void turn(Matrix3D mt, Vector3D centr) {
		turnMatrix = Matrix3D.mult(mt, turnMatrix);
		center.sub(centr);
		center = mt.mult(center);		
		center.add(centr);

	}
	
	public void turn(Matrix3D matrix) {
		turnMatrix = Matrix3D.mult(matrix, turnMatrix);
	}
	

	public void turn(Vector3D axle, float alfa, Vector3D centr) {
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		turnMatrix = Matrix3D.mult(mt, turnMatrix);
		center.sub(centr);
		center = mt.mult(center);		
		center.add(centr);		

	}

	public void draw(Draw3dHelper drwr) {
		PixelZ p[] = new PixelZ[4];		
		for(int i = 0; i < 3; i++){
			p[i] = new PixelZ();		
		}
		boolean fullBall = true;
		int facNum2 = facetsNum/2;
		Vector3D meridian[] = new Vector3D[facNum2 + 1];
		Vector3D normals[] = new Vector3D[facNum2 + 1];
		
		
		meridian[0] = turnMatrix.mult(new Vector3D(0, radius, 0));
		meridian[facNum2] = turnMatrix.mult(new Vector3D(0, -radius, 0));
		normals[0] = turnMatrix.mult(new Vector3D(0, -1, 0));
		normals[facNum2] = turnMatrix.mult(new Vector3D(0, 1, 0));
		
		float alSin = (float)Math.sin(facetAngle);
		float alCos = (float)Math.cos(facetAngle);
		float merZ = 0;
		float merY = radius;
		float norZ = 0;
		float norY = -1f;
		
		
		for(int i = 1; i < facNum2; i++){
			float tmpZ = merZ;
			float tmpY = merY;
			merZ = merZ * alCos - merY * alSin;
			merY = merY * alCos + tmpZ * alSin;
			
			
			if(radius- merY >= segmentHeight){
				float k = (radius - merY - segmentHeight)/(tmpY - merY);
				merZ += k * (tmpZ - merZ);
				merY = radius - segmentHeight;

				meridian[i] = turnMatrix.mult(new Vector3D(0, merY, merZ));								
				normals[i] = turnMatrix.mult(new Vector3D(0, norY, norZ));
				facNum2  = i; //+1;		
				fullBall =false;
				break;
			}
			
			meridian[i] = turnMatrix.mult(new Vector3D(0, merY, merZ));
			tmpZ = norZ;
			norZ = norZ * alCos - norY * alSin;
			norY = norY * alCos + tmpZ * alSin;
			normals[i] = turnMatrix.mult(new Vector3D(0, norY, norZ));			
		}	
		
		
		//the points of the meridian are turned in the meridian[] and normals[] arrays. 
		
		int col;				
		Vector3D v0;		
	
		PixelZ tmpP;		
		
		Vector3D mainAxis = turnMatrix.mult(new Vector3D(0, 1, 0));
		Matrix3D facetMatrix = Transform3D.getTurnMatrix(mainAxis, facetAngle);
	
		
		for(int i = 0; i < facetsNum ; i++){
			
			v0 = new Vector3D(meridian[0]);
			v0.add(center);
			p[0].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
			col = drwr.getScreenColor(color, normals[0], v0, viewType);
						
			v0.set(meridian[1]);
			v0.add(center);
			p[1].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
			
			v0.set(facetMatrix.mult(meridian[1]));
			meridian[1].set(v0);
			v0.add(center);
			p[2].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
			if(col != Draw3dHelper.EMPTY_COLOR)
				drwr.dr3angl(p, col);		

			for(int j = 2; j < facNum2; j++){
				tmpP = p[0];
				p[0] = p[1];
				p[1] = tmpP;
				normals[j].set(facetMatrix.mult(normals[j]));
			
				col = drwr.getScreenColor(color, normals[j], v0, viewType);
				v0.set(meridian[j]);
				v0.add(center);
				p[1].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
				if(col != Draw3dHelper.EMPTY_COLOR)					
					drwr.dr3angl(p, col);
				
				tmpP = p[0];
				p[0] = p[2];
				p[2] = tmpP;
				
				v0.set(facetMatrix.mult(meridian[j]));
			
				meridian[j].set(v0);
				v0.add(center);
				p[2].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
				if(col != Draw3dHelper.EMPTY_COLOR)
					drwr.dr3angl(p, col);		
			}
		
		
			if(fullBall){
				v0 = new Vector3D(meridian[facNum2]);
				v0.add(center);
				p[0].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
				col = drwr.getScreenColor(color, normals[0], v0, viewType);
				if(col != Draw3dHelper.EMPTY_COLOR)
					drwr.dr3angl(p, col);
			}
		}	

	}

	
}
