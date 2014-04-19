package simple3d;

/**
 * 
 * A screen pixel with the screen coordinates and depth.
 * 
 * @author Alexander Lisovets
 * 
 */
public class PixelZ {

	public int x, y; // the screen coordinates
	public float depth; // the screen depth (depth == 1 / z)  
	public int col; //color

	public PixelZ() {
		set(0, 0, 0f, 0);
	}

	public PixelZ(int px, int py, float pz) {
		set(px, py, pz, 0);
	}

	public PixelZ(int px, int py, float pz, int pc) {
		set(px, py, pz, pc);
	}

	public PixelZ(PixelZ p) {
		set(p);
	}

	public void set(int px, int py, float pz, int pc) {
		x = px;
		y = py;
		depth = pz;
		col = pc;
	}

	public void set(int px, int py, float pz) {
		x = px;
		y = py;
		depth = pz;
	}

	public void set(PixelZ p) {
		x = p.x;
		y = p.y;
		depth = p.depth;
		col = p.col;
	}
}
