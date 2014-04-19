package simple3d.shape;
import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.PixelZ;
import simple3d.Transform3D;
import simple3d.Vector3D;


public class Cone implements Shape {
	public Vector3D center;
	public float radius0;
	public float radius1;
	public float height;
	public int color;
	public float startAngle;
	public float facetAngle;
	public int facetsNum;
	Matrix3D mTurn;
	public int viewType;
	
	
	public Cone(float height, float radius0, float radius1, float segmentAngle, int facetsNum, int color){
		this(height, radius0, radius1, segmentAngle, facetsNum, color, 0);
	}
	
	public Cone(float height, float radius0, float radius1, float segmentAngle, int facetsNum, int color, int viewType){
		center = new Vector3D();				
		this.facetsNum = facetsNum;
		this.radius0 = radius0;
		this.radius1 = radius1;
		this.height = height; 
		mTurn = new Matrix3D(1,0,0, 0,1,0, 0,0,1);		
		if(segmentAngle == 0f)			
			facetAngle = (float)(Math.PI * 2 / facetsNum);		
		else
			facetAngle = segmentAngle / facetsNum;					
		this.color = color;
		startAngle = 0;
		this.viewType = viewType;
	}	
	
	protected  Cone(){		
		center = new Vector3D();		
		mTurn = new Matrix3D();
	}
	
	public Cone(Cone cone){
		center = new Vector3D(cone.center);		
		mTurn = new Matrix3D(cone.mTurn);
		startAngle = cone.startAngle;
		facetAngle = cone.facetAngle;
		facetsNum = cone.facetsNum;
		color = cone.color;
		radius0 = cone.radius0;
		radius1 = cone.radius1;
		height = cone.height;
		viewType = cone.viewType; 
	}
	
	
	public boolean isOnScreen(Draw3dHelper drwr) {
		return true;
	}

	public void move(Vector3D sh) {
		center.add(sh);

	}

	public void rmove(Vector3D sh) {
		center.sub(sh);

	}

	public void turn0(Matrix3D matrix) {
		mTurn = Matrix3D.mult(matrix, mTurn);
		center.set(matrix.mult(center));

	}

	public void turn(Matrix3D matrix, Vector3D centr) {
		mTurn = Matrix3D.mult(matrix, mTurn);
		center.sub(centr);
		center = matrix.mult(center);		
		center.add(centr);

	}
	
	public void turn(Matrix3D matrix) {
		mTurn = Matrix3D.mult(matrix, mTurn);
	}


	public void turn(Vector3D axle, float alfa, Vector3D centr) {
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		turn(mt, centr);		

	}
	

	public void draw(Draw3dHelper drwr) {
		int col;
		PixelZ p[] = new PixelZ[4];		
		Vector3D v0 = new Vector3D(0, height / 2, -radius0);
		Vector3D v1 = new Vector3D(0, -height / 2, -radius1);	
		float delta = radius1 - radius0;
		float len = (float)Math.sqrt(delta*delta + height*height);				
		Vector3D norml = new Vector3D(0, -delta/len, height/len);		
		
		Matrix3D mt1 = Transform3D.getTurnMatrixY(startAngle);
		v0 = mt1.mult(v0);
		v1 = mt1.mult(v1);
		norml = mt1.mult(norml);
		
		
		Vector3D v2 = new Vector3D(v0);
		Vector3D v3 = new Vector3D(v1);
		
		for(int i = 0; i < 3; i++){
			p[i] = new PixelZ();
		}
		v0 = mTurn.mult(v0);
		v0.add(center);
		v1 = mTurn.mult(v1);
		v1.add(center);	
		p[0].set((int)drwr.getScreenX(v1), (int)drwr.getScreenY(v1), 1/v1.z);
		p[2].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
		
		mt1 = Transform3D.getTurnMatrixY(facetAngle);
		for(int i = 0; i < facetsNum; i++ ){
			
			p[1].set(p[0]);
			p[0].set(p[2]);
			v0 = v2;
			v1 = v3;
			v2 = mt1.mult(v2);
			v3 = mt1.mult(v3);	
			
			v0 = mTurn.mult(v2);
			v0.add(center);
			p[2].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);			
			v0 = mTurn.mult(norml);
			col = drwr.getScreenColor(color, v0, center, viewType);
			if(col != Draw3dHelper.EMPTY_COLOR)
				drwr.dr3angl(p, col);
			v0 = mTurn.mult(v3);
			v0.add(center);
			p[0].set((int)drwr.getScreenX(v0), (int)drwr.getScreenY(v0), 1/v0.z);
			if(col != Draw3dHelper.EMPTY_COLOR)
				drwr.dr3angl(p, col);
			norml = mt1.mult(norml);
		}

	}

	

}
