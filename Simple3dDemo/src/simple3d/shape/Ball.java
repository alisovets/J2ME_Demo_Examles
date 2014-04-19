package simple3d.shape;

import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.PixelZ;
import simple3d.Transform3D;
import simple3d.Vector3D;

public class Ball implements Shape{
	
	public Vector3D center;
	public float radius;
	public int color;
	
	public Ball(){
		center = new Vector3D(0, 0, 0);
	}
	
	public Ball(float radius, int color){
		this();
		this.radius = radius;
		this.color = color;
	}
	
	

	public void turn0(Matrix3D matrix) {
		center.set(matrix.mult(center));
	}

	public void turn(Matrix3D matrix) {
		//nothing
	}

	public void turn(Matrix3D matrix, Vector3D point) {
		center.sub(point);
		center.set(matrix.mult(center));
		center.add(point);
		
	}

	public void turn(Vector3D axle, float angle, Vector3D point) {
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, angle);
		center.sub(point);
		center = mt.mult(center);		
		center.add(point);	
	}

	public void move(Vector3D shift) {
		center.add(shift);
	}

	public void rmove(Vector3D shift) {
		center.sub(shift);
		
	}

	public boolean isOnScreen(Draw3dHelper drawHelper) {
		// TODO stub
		return false;
	}

	public void draw(Draw3dHelper drawHelper) {
		float centerX = drawHelper.getScreenX(center);
		float centerY = drawHelper.getScreenY(center);
		float centerZ = center.z;
		Vector3D endRealPoint = new Vector3D(0, radius, 0);
		endRealPoint.add(center);
		float endY = drawHelper.getScreenY(endRealPoint);
		float radius = centerY - endY;
		
		float rFactor = 1f / radius;
		float squareR = radius * radius;
		float r; 
		float z; 
		float screenX1, screenX2, screenY1, screenY2;
		PixelZ pixelZ = new PixelZ();
		Vector3D normal = new Vector3D();
		Vector3D point = new Vector3D();
		int w = drawHelper.getWidth();
		int h = drawHelper.getHeight();
		for(float y = 0; y <= radius; y++){
			r = (float)Math.sqrt(squareR  - y * y);
			screenY1 = centerY - y;
			if(screenY1 > h){
				continue;
			}
			screenY2 = centerY + y;
			if(screenY2 < 0){
				continue;
			}
			
			for(float x = 0; x <= r; x++){
				screenX1 = x  + centerX;
				if(screenX1 < 0){
					continue;
				}
				
				screenX2 = centerX - x;
				if(screenX2 > w){
					continue;
				}
				
				z = (float) Math.sqrt(squareR - x * x - y * y);
				normal.set(-x * rFactor, -y * rFactor, z * rFactor);
				
				point.set(centerX + x, centerY + y, centerZ - z);
				int screenColor = drawHelper.getScreenColor(color, normal, point, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
				pixelZ.set((int)screenX1, (int)screenY1, 1f/(centerZ - z), screenColor);
				if(screenColor != Draw3dHelper.EMPTY_COLOR){
					drawHelper.drawPoint(pixelZ);
				}
				
				normal.y = -normal.y;
				point.y = centerY - y;
				pixelZ.y = (int)screenY2;
				if((screenY2 < h) && (screenX1 < w)){
					screenColor = drawHelper.getScreenColor(color, normal, point, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
					if(screenColor != Draw3dHelper.EMPTY_COLOR ){
						pixelZ.col = screenColor;
						drawHelper.drawPoint(pixelZ);
					}
				}
				
				if(screenX2 < 0){
					continue;
				}
				normal.x = -normal.x;
				point.x = centerX - x;
				pixelZ.x = (int)screenX2;
				if(screenY2 < h){
					screenColor = drawHelper.getScreenColor(color, normal, point, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
					if(screenColor != Draw3dHelper.EMPTY_COLOR ){
						pixelZ.col = screenColor;
						drawHelper.drawPoint(pixelZ);
					}
				}
				if(screenY1 < 0){
					continue;
				}
				
				normal.y = -normal.y;
				point.y = centerY + y;
				pixelZ.y = (int)screenY1;
				screenColor = drawHelper.getScreenColor(color, normal, point, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
				if(screenColor != Draw3dHelper.EMPTY_COLOR ){
					pixelZ.col = screenColor;
					drawHelper.drawPoint(pixelZ);
				}
				
			}
		}
		
	}
}
