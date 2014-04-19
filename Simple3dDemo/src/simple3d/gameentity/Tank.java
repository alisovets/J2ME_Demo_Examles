package simple3d.gameentity;
import simple3d.Draw3dHelper;
import simple3d.Matrix3D;
import simple3d.Transform3D;
import simple3d.Vector3D;
import simple3d.shape.Cone;
import simple3d.shape.Polygon;
import simple3d.shape.Shape;

/**
 * 
 * To create a 3D tank model  
 *  
 * @author Alexander Lisovets
 *
 */
public class Tank implements Shape{
	Turret turret;
	Base base;		
	Vector3D vertical;
	Vector3D centr;
	
	/**
	 * Creates a 3d tank model specified color and size  
	 * @param color
	 * @param size
	 */
	public Tank(int color, int size){							
		turret = new Turret(color, (int)(size*0.9f));	
		base = new Base(color, size);	
		turret.move(new Vector3D(0, 2.8f*size, 0));
		vertical = new Vector3D(0, 1, 0);
		centr = new Vector3D(-0.5f*size * 0.9f , 0, 0);
	}
	
	
	public void turn0(Matrix3D mt){
		turret.turn0(mt);
		base.turn0(mt);
		vertical.set(mt.mult(vertical));
		centr.set(mt.mult(centr));
	}
	
	public void turn(Matrix3D mt, Vector3D centr){		
		turret.turn(mt, centr);	
		base.turn(mt, centr);
		vertical.set( mt.mult(vertical));
		this.centr.sub(centr);
		this.centr.set( mt.mult(this.centr));
		this.centr.add(centr);		
	}
	
	public void turn(Matrix3D mt) {
		turret.turn(mt, centr);	
		base.turn(mt, centr);
		vertical.set( mt.mult(vertical));
	}
	
	public void turn(Vector3D axle, float alfa, Vector3D centr){
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		turret.turn(mt, centr);		
		base.turn(mt, centr);
		vertical.set( mt.mult(vertical));
		this.centr.sub(centr);
		this.centr.set( mt.mult(this.centr));
		this.centr.add(centr);
	}
	
	/**
	 * Turn a tank turret on specified angle  
	 * @param angle
	 */
	public void turnTurret(float angle){
		turret.turn(vertical, angle, centr);
	}
	
	public void move(Vector3D sh){		
		turret.move(sh);
		base.move(sh);
		centr.add(sh);
	}
	
	public void rmove(Vector3D sh){		
		turret.rmove(sh);
		base.rmove(sh);
		centr.sub(sh);
	}
	
	public void draw(Draw3dHelper drwr  ){		
		turret.draw(drwr);
		base.draw(drwr);	
	}
	
	public boolean isOnScreen(Draw3dHelper drwr) {
		// TODO stub
		return true;
	}

	
}


class Turret implements Shape{
	Shape turret[];
	int color;
	
	public Turret(int color, int size){
		this.color = color;
		
		turret = new Shape[15];
		
		
		
		Vector3D p0 = new Vector3D(-2*size, 3*size, 2*size);
		Vector3D p1 = new Vector3D(size, 3*size, 2*size);
		Vector3D p2 = new Vector3D(-2*size, 3*size, -2*size);
		Vector3D p3 = new Vector3D(size, 3*size, -2*size);
		
		Vector3D p4 = new Vector3D(-2.5f*size, 0.5f*size, 3.2f*size);
		Vector3D p5 = new Vector3D(1.5f*size, 0.5f*size, 3.2f*size);
		Vector3D p6 = new Vector3D(-2.5f*size, 0.5f*size, -3.2f*size);
		Vector3D p7 = new Vector3D(1.5f*size, 0.5f*size, -3.2f*size);
		
		Vector3D p8 = new Vector3D(4*size, 2*size, size);
		Vector3D p9 = new Vector3D(4*size, 2*size, -size);
		Vector3D p10= new Vector3D(4.2f*size, size, 1.2f*size);
		Vector3D p11= new Vector3D(4.2f*size, size, -1.2f*size);
		
		Vector3D p12 = new Vector3D(-3.4f*size, 2*size, 1.1f*size);
		Vector3D p13 = new Vector3D(-3.4f*size, 2*size, -1.1f*size);
		Vector3D p14 = new Vector3D(-3.6f*size, size, 1.7f*size);
		Vector3D p15 = new Vector3D(-3.6f*size, size, -1.7f*size);			
		
		
		Vector3D p[] = new Vector3D[4];
		
		p[0] = p0;
		p[1] = p4;
		p[2] = p14;
		p[3] = p12;		
		Vector3D n = new Vector3D(0.6f, -0.52f, -0.6f);		
		Polygon pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[0] = pol1;
		
		p[0] = p0;
		p[1] = p1;
		p[2] = p3;
		p[3] = p2;
		n = new Vector3D(0, -1,0);		
		pol1 = new Polygon(p, n, color , Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[1] = pol1;		
		
		p[2] = p5;
		p[3] = p4;		
		n = new Vector3D(0, -0.513f, -0.856f);		
		pol1 = new Polygon(p, n, color , Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[2] = pol1;
		
		p[0] = p2;
		p[1] = p3;
		p[2] = p7;
		p[3] = p6;
		n = new Vector3D(0, -0.513f, 0.856f);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[3] = pol1;				
		
		p[0] = p1;
		p[1] = p3;
		p[2] = p9;
		p[3] = p8;
		n = new Vector3D(-0.164f, -0.98f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[4] = pol1;
		
		p[0] = p1;
		p[1] = p5;
		p[2] = p10;
		p[3] = p8;
		n = new Vector3D(-0.5f, -0.66f, -0.55f);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[5] = pol1;
		
		p[0] = p3;
		p[1] = p7;
		p[2] = p11;
		p[3] = p9;
		n = new Vector3D(-0.5f, -0.66f, 0.55f);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[6] = pol1;
		
		p[0] = p8;
		p[1] = p10;			
		n = new Vector3D(-0.979f, -0.2f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[7] = pol1;
		
		p[0] = p5;
		p[1] = p7;
		p[2] = p11;
		p[3] = p10;		
		n = new Vector3D(-0.2f, 0.979f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[8] = pol1;							
		
		p[0] = p0;
		p[1] = p2;
		p[2] = p13;
		p[3] = p12;		
		n = new Vector3D(0.55f, -0.83f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[9] = pol1;
		
		p[0] = p14;
		p[1] = p15;				
		n = new Vector3D(1, 0, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[10] = pol1;
		
		
		p[2] = p6;
		p[3] = p4;		
		n = new Vector3D(0.895f, 0.446f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[11] = pol1;
		
		p[0] = p2;
		p[1] = p6;
		p[2] = p15;
		p[3] = p13;		
		n = new Vector3D(0.6f, -0.52f, 0.6f);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		turret[12] = pol1;						 		
		
		Cone gun = new Cone(4 * size, 0.4f * size, 0.4f * size, 0, 6, color, Draw3dHelper.FRONT_SIDE_IS_LIT);
		gun.turn0(Transform3D.getTurnMatrixZ(1.57f));
		gun.move(new Vector3D(6 * size, 1.5f * size, 0));
		turret[13] = gun; 
		
		
		//не совсем обязательно
		Cone hatch = new Cone(1, size, size, 0, 8, color);
		hatch.move(new Vector3D(-0.5f * size, 3.1f * size, 0));
		turret[14] = hatch;
		
		
		
		
		
		//не обязательно
		p[0] = p5;
		p[1] = p7;
		p[2] = p6;
		p[3] = p4;		
		n = new Vector3D(0, 1f, 0);		
		pol1 = new Polygon(p, n, color, 0);
		//turret[15] = pol1;
		
		Cone os = new Cone(0.5f*size, 2f*size, 2f * size, 0, 8, color);
		os.move(new Vector3D(-0.5f*size, 0.5f * size, 0));
		//turret[16] = os;
		
	}

	public void turn0(Matrix3D mt){
		for(int i = 0; i < turret.length; i++){
			turret[i].turn0(mt);	
		}
	}
	
	public void turn(Matrix3D mt, Vector3D centr){
		for(int i = 0; i < turret.length; i++){
			turret[i].turn(mt, centr);	
		}
	}
	
	public void turn(Vector3D axle, float alfa, Vector3D centr){
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		for(int i = 0; i < turret.length; i++){
			turret[i].turn(mt, centr);	
		}
	}
	
	public void move(Vector3D sh){
		for(int i = 0; i < turret.length; i++){
			turret[i].move(sh);	
		}		
	}
	
	public void rmove(Vector3D sh){
		for(int i = 0; i < turret.length; i++){
			turret[i].rmove(sh);	
		}		
	}
	
	public void draw(Draw3dHelper drwr  ){
		
		for(int i =  0; i <= 12; i++){
			Polygon pl = (Polygon)turret[i]; 
			int curCol = drwr.getScreenColor(pl.color, pl.normal, pl.vertexes[0], 1);
			if(curCol != Draw3dHelper.EMPTY_COLOR){			
				turret[i].draw(drwr);
			}	
		}
		
		for(int i = 12; i < turret.length; i++){
			turret[i].draw(drwr);
		}			
	}
	
	public boolean isOnScreen(Draw3dHelper drwr) {
		return true;
	}

	public void turn(Matrix3D matrix) {
		//nothing
	}
}	

class Base implements Shape{
	Shape base[];
	int color;
	
	public Base(int color, int size){
		this.color = color;
		
		base = new Shape[17];		
		
		Vector3D p0 = new Vector3D(-4*size, 3*size, 2*size);
		Vector3D p1 = new Vector3D(2*size, 3*size, 2*size);		
		Vector3D p2 = new Vector3D(2*size, 3*size, -2*size);
		Vector3D p3 = new Vector3D(-4*size, 3*size, -2*size);
		
		Vector3D p4 = new Vector3D(3.5f*size, 1.5f*size, 2*size);
		Vector3D p5 = new Vector3D(3.5f*size, 1.5f*size, -2f*size);
		Vector3D p6 = new Vector3D(3f*size, size, -2f*size);
		Vector3D p7 = new Vector3D(3f*size, size, 2f*size);
		
		Vector3D p8 = new Vector3D(-4.5f*size, 2.5f*size, 2*size);
		Vector3D p9 = new Vector3D(-4.5f*size, 2.5f*size, -2* size);
		Vector3D p10= new Vector3D(-4.5f*size, size, -2*size);
		Vector3D p11= new Vector3D(-4.5f*size, size, 2*size);
		
		Vector3D p[] = new Vector3D[4];
		p[0] = p0;
		p[1] = p1;
		p[2] = p2;
		p[3] = p3;
		Vector3D n = new Vector3D(0, -1, 0);		
		Polygon pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		base[0] = pol1;		
		
		p[0] = p1;
		p[1] = p2;
		p[2] = p5;
		p[3] = p4;		
		n = new Vector3D(-0.707f,-0.707f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		base[1] = pol1;
		
		p[0] = p7;
		p[1] = p6;		
		n = new Vector3D(-0.707f, 0.707f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		base[2] = pol1;
		
		p[0] = p0;
		p[1] = p3;
		p[2] = p9;
		p[3] = p8;
		n = new Vector3D(0.707f, -0.707f, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		base[3] = pol1;
		
		p[0] = p11;
		p[1] = p10;		
		n = new Vector3D(1, 0, 0);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		base[4] = pol1;
		
		p = new Vector3D[6];
		p[0] = p0;
		p[1] = p1;
		p[2] = p4;
		p[3] = p7;
		p[4] = p11;
		p[5] = p8;
		n = new Vector3D(0,0, -1);		
		pol1 = new Polygon(p, n, color, Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		base[5] = pol1;		
		
		
		p[0] = p3;
		p[1] = p2;
		p[2] = p5;
		p[3] = p6;
		p[4] = p10;
		p[5] = p9;
		n = new Vector3D(0,0, 1);		
		pol1 = new Polygon(p, n, color , Draw3dHelper.FRONT_SIDE_IS_VISIBLE);
		base[6] = pol1;
		
		//traks		
		int color2 = (color & 255)/3 + (((color & 0xff00)/3)& 0xff00 ) + (((color & 0xff0000)/3)& 0xff0000);   			
		Wheel wheel = new Wheel(color,  color2, size);
		wheel.turn0(Transform3D.getTurnMatrixX(1.57f));
		wheel.move(new Vector3D(-3.75f*size, 1.25f*size, -2.75f*size));
		base[7] = wheel;
		
		wheel = new Wheel(wheel);
		wheel.move(new Vector3D(3.25f*size, 0, 0));
		base[8] = wheel;
		
		wheel = new Wheel(wheel);
		wheel.move(new Vector3D(3.25f*size, 0, 0));
		base[9] = wheel;
		
		wheel = new Wheel(wheel);
		wheel.move(new Vector3D(0, 0, 5.5f*size));
		base[10] = wheel;
		
		wheel = new Wheel(wheel);
		wheel.move(new Vector3D(-3.25f*size, 0, 0));
		base[11] = wheel;
		
		wheel = new Wheel(wheel);
		wheel.move(new Vector3D(-3.25f*size, 0, 0));
		base[12] = wheel;
		
		p = new Vector3D[4];
		p[0] = new Vector3D(-3.75f*size, 2.5f*size, -2*size);
		p[1] = new Vector3D(2.75f*size, 2.5f*size, -2*size);		
		p[3] = new Vector3D(-3.75f*size, 2.5f*size, -3.5f*size);
		p[2] = new Vector3D(2.75f*size, 2.5f*size, -3.5f*size);
		
		n = new Vector3D(0, 1, 0);
		pol1 = new Polygon(p, n, color2, 0);
		base[13] = pol1;
		pol1 = new Polygon(pol1);
		pol1.move(new Vector3D(0, -2.5f * size, 0));
		base[14] = pol1;
		
		pol1 = new Polygon(pol1);
		pol1.move(new Vector3D(0, 0, 5.5f * size));
		base[15] = pol1;
		
		pol1 = new Polygon(pol1);
		pol1.move(new Vector3D(0, 2.5f * size, 0));
		base[16] = pol1;
		
	}

	public void turn0(Matrix3D mt){
		for(int i = 0; i < base.length; i++){
			base[i].turn0(mt);	
		}
	}
	
	public void turn(Matrix3D mt, Vector3D centr){
		for(int i = 0; i < base.length; i++){
			base[i].turn(mt, centr);	
		}
	}	
	
	public void turn(Matrix3D matrix) {
		//nothing
	}
	
	public void turn(Vector3D axle, float alfa, Vector3D centr){
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		for(int i = 0; i < base.length; i++){
			base[i].turn(mt, centr);	
		}
	}
	
	public void move(Vector3D sh){
		for(int i = 0; i < base.length; i++){
			base[i].move(sh);	
		}		
	}
	
	public void rmove(Vector3D sh){
		for(int i = 0; i < base.length; i++){
			base[i].rmove(sh);	
		}		
	}
	
	
	public void draw(Draw3dHelper drwr  ){				
		for(int i =  0; i <= 6; i++){		
			Polygon pl = (Polygon)base[i]; 
			int curCol = drwr.getScreenColor(pl.color, pl.normal, pl.vertexes[0], 1);
			if(curCol != Draw3dHelper.EMPTY_COLOR){			
				base[i].draw(drwr);
			}	
		}			
		
		for(int i =  7; i < base.length; i++){		
			base[i].draw(drwr);
		}			
	}
	
	public boolean isOnScreen(Draw3dHelper drwr) {
		return true;
	}

	
}	


class Wheel implements Shape{		
	Cone disk;
	Cone band; 
	
	public Wheel(Wheel wl){		
		disk = new Cone(wl.disk);
		band = new Cone(wl.band);						
	}
	
	public Wheel(int color1, int color2, int size){		
		disk = new Cone(0, 1, 1f * size, 0, 8, color1);
		band = new Cone(1.5f*size, 1.25f* size, 1.25f* size, 0, 8, color2);	
		//band.move(new Vector3D(0, 0.75f * size, 0));
	}

	public void turn0(Matrix3D mt){		
		disk.turn0(mt);
		band.turn0(mt);		
	}
	
	public void turn(Matrix3D mt) {
		Vector3D center = new Vector3D(band.center);
		disk.turn(mt, center);		
		band.turn(mt, center);
	}
	
	public void turn(Matrix3D mt, Vector3D centr){	
		disk.turn(mt, centr);		
		band.turn(mt, centr);
	}
	
	public void turn(Vector3D axle, float alfa, Vector3D centr){
		Matrix3D mt  = Transform3D.getTurnMatrix(axle, alfa);
		disk.turn(mt, centr);		
		band.turn(mt, centr);
	}
		
	
	public void move(Vector3D sh){	
		disk.move(sh);	
		band.move(sh);
	}
	
	public void rmove(Vector3D sh){	
		disk.rmove(sh);	
		band.rmove(sh);
	}
	
	
	public void draw(Draw3dHelper drwr  ){				
		disk.draw(drwr);
		band.draw(drwr);					
	}
	
	public boolean isOnScreen(Draw3dHelper drwr) {
		return true;
	}

	
}	
