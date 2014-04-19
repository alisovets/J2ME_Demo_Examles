package simple3d;


/**
 * 
 * This class s needed to help to draw a 3d-scene.
 * It creates the bitmap buffer to store the color and the depth of screen pixels.
 * it's possible to set the lighting and the screen distance.
 * Then use methods to draw pixels and triangles specifies their 3d coordinates, full light colors and normal vector.
 * It makes the objects arrange on the screen considering their mutual overlap and the size are based on accounting the perspective. 
 * 
 * @author Alexander Lisovets
 *
 */
public class Draw3dHelper {
	public final static int EMPTY_COLOR = 0xff000000;
	public final static int BOTH_OF_SIDES_ARE_LIT = 0;
	public final static int FRONT_SIDE_IS_VISIBLE = 1;
	public final static int FRONT_SIDE_IS_LIT = 2;
	public final static int BACK_SIDE_IS_VISIBLE = 3;
	public final static int BACK_SIDE_IS_LIT = 4;
	
	private int width, height;
	private int halfWidth, halfHeight; // help to use origin of coordinates at center of the screen
	private float cameraFrontDistance; // Front camera distance
	private float ambientLightLevel; //[0..1] 
	private Vector3D mainLight;
	
	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

	final private int []colorBuffer;
	final private float []depthBuffer;
	private float maxZ;
	private int numberOfPixels;
	private PixelZ[] vertexes; 
	private float currentZ; 
	private int currentX, currentY, currentColor;

	/**
	 * Create object specified screen size 
	 * @param screenWidth 
	 * @param screenHeight
	 */
	public Draw3dHelper(int screenWidth, int screenHeight) {
		width = screenWidth;
		height = screenHeight;
		halfWidth = width / 2;
		halfHeight = height / 2;
		numberOfPixels = width * height;
		cameraFrontDistance = halfWidth + halfHeight;
		maxZ = 3f / cameraFrontDistance;
		colorBuffer = new int[numberOfPixels];
		depthBuffer = new float[numberOfPixels];
		cleanDrawBuffer(EMPTY_COLOR);
		ambientLightLevel = 0.3f;
		mainLight = new Vector3D(0.5f, -0.7f, 0.51f);
		vertexes = new PixelZ[3];
		for (int i = 0; i < vertexes.length; i++) {
			vertexes[i] = new PixelZ();
		}
	}

	/**
	 * Constructor
	 * @param screenWidth the screen width. 
	 * @param screenHeight screen height
	 * @param mainLight the main light vector with scalar value is not more then 1  
	 * @param ambientLightLevel the ambient light level 
	 * @param cameraFrontDistance  the distance between a watcher (camera) an the screen.
	 */
	public Draw3dHelper(int screenWidth, int screenHeight, Vector3D mainLight, float ambientLightLevel, float cameraFrontDistance) {
		width = screenWidth;
		height = screenHeight;
		halfWidth = width / 2;
		halfHeight = height / 2;
		numberOfPixels = width * height;
		this.cameraFrontDistance = cameraFrontDistance;
		maxZ = 3f / cameraFrontDistance;
		colorBuffer = new int[numberOfPixels];
		depthBuffer = new float[numberOfPixels];
		cleanDrawBuffer(EMPTY_COLOR);
		this.ambientLightLevel = ambientLightLevel;
		this.mainLight = mainLight;
		vertexes = new PixelZ[3];
		for (int i = 0; i < vertexes.length; i++) {
			vertexes[i] = new PixelZ();
		}
	}
	
	/**
	 * sets ambient light level. This level define intensity of surfaces that is not lit with the main lighting. 
	 * @param ambientLightLevel value must be between 0..1
	 */
	public void setAmbientLightLevel(float ambientLightLevel){
		this.ambientLightLevel = ambientLightLevel;
	}
	
	/**
	 * sets the main light vector.
	 * @param mainLight
	 */
	public void setMainLight(Vector3D mainLight){
		this.mainLight = mainLight;
	}
	
	/**
	 * sets - camera front distance.
	 * @param cameraFrontDistance  the distance between a watcher (camera) an the screen.
	 */
	public void setCameraFrontDistance(float cameraFrontDistance){
		this.cameraFrontDistance = cameraFrontDistance;
	}
	
	/**
	 * @return the color buffer
	 */
	public int[] getColorBuffer(){
		return colorBuffer;
	}
	
	/**
	 * checks if point is inside the screen borders 
	 * @param point
	 * @return true if point inside the screen borders 
	 */
	public boolean isOnScreen(Vector3D point){
		
		int x = (int)getScreenX(point);
		if(x < 0 || x >= width ){
			return false;
		}		
		int y = (int)getScreenX(point);
		return !(y < 0 || x >= height );
	}
	
	/**
	 * gets a screen's X-coordinate based on real 3d the position
	 * @param p the point with real coordinates
	 * @return screen's X-coordinate
	 */
	public float getScreenX(Vector3D p) {
		return (p.x * cameraFrontDistance / p.z) + halfWidth;
	}
	
	/**
	 * gets a screen's Y-coordinate based on real 3d the position
	 * @param p
	 * @return screen's Y-coordinate
	 */
	public float getScreenY(Vector3D p) {
		return (halfHeight) - (float) (p.y * cameraFrontDistance / p.z);
	}

	
	/**
	 *  draws the current screen pixel if the new point is closer then than the existing
	 */
 	public void drawCurrentPixel() {
		
		//check if the current pixel is out of the screen borders;  
		if (currentZ > maxZ)
			return;
		if ((currentX < 0) || (currentX >= width)) {
			return;
		}
		if ((currentY < 0) || (currentY >= height)) {
			return;
		}
		
		int index = currentY * width + currentX;
		if (depthBuffer[index] >= currentZ) // there is a closer point
			return;
		
		//draw
		colorBuffer[index] = currentColor;
		depthBuffer[index] = currentZ;
	}

	/**
	 * draws one Screen Pixel
	 * @param pixel
	 * @param color
	 */
	public void drawPoint(PixelZ pixel) {
		if (pixel.depth > maxZ) //it's too close 
			return;
		if ((pixel.x < 0) || (pixel.x >= width)) // out of the screen borders
			return;
		if ((pixel.y < 0) || (pixel.y >= height)) // out of the screen borders
			return;
		int index = pixel.y * width + pixel.x;
		if (depthBuffer[index] >= pixel.depth)  // there is a closer point
			return;
		//draw
		colorBuffer[index] = pixel.col;
		depthBuffer[index] = pixel.depth;
	}

	/**
	 *  draws one 3d Point
	 * @param position
	 * @param color
	 */
	public void drawPoint(Vector3D position, int color) {
		if (position.z < 0)
			return;
		currentZ = 1f / position.z;
		//calculate the screen coordinates from the real 
		currentX = (int) (position.x * cameraFrontDistance * currentZ) + halfWidth;
		if ((currentX < 0) || (currentX >= width)) 
			return;
		//
		currentY = halfHeight - (int) (position.y * cameraFrontDistance * currentZ);
		if ((currentY < 0) || (currentY >= height))
			return;
		int index = currentY * width + currentX;
		if (depthBuffer[index] >= currentZ) // there is a closer point
			return;
		
		//draw
		colorBuffer[index] = color;
		depthBuffer[index] = currentZ;
	}


	/**
	 * calculates and returns the visible color of the pixel defined on the  
	 * 3d position, normal vector and real color  
	 * @param color - real color
	 * @param normal
	 * @param position
	 * @return the screen color
	 */
	public int getScreenColor(int color, Vector3D normal, Vector3D position) {
		float intens  = normal.x * mainLight.x + normal.y * mainLight.y + normal.z * mainLight.z;

		float watcherFactor = normal.x * position.x + normal.y * position.y + normal.z * position.z;
		if (watcherFactor < 0f) {
			intens = -intens;
		}

		//try to kill the light pollution on the edge
		//TODO: I need a good idea.
		float criticFactor = position.z / 40;
		if ((watcherFactor < criticFactor) && (watcherFactor > -criticFactor))
			if (intens > 0)
				intens = -intens;
		

		if (intens < ambientLightLevel)
			intens = ambientLightLevel;

		return ((int) ((color & 0xff0000) * intens) & 0xff0000)
				+ ((int) ((color & 0xff00) * intens) & 0xff00)
				+ (int) ((color & 0xff) * intens);
	}

	 /**
	 * calculates and returns the visible color of the pixel defined on the  
	 * 3d position, normal vector and real color  
	 * @param color - real color
	 * @param normal
	 * @param position
	 * @param viewType - defined how to show and light up the front and back sides  
	 * @return the screen color
	 */
	public int getScreenColor(int color, Vector3D normal, Vector3D position, int viewType) {
		
		//check if the front side is visible
		//if  watcherFactor > 0 then front side is visible in this point;
		float watcherFactor = normal.x * position.x + normal.y * position.y + normal.z * position.z; 
		
		float intens;
		if (watcherFactor < 0f) {
			if (viewType == FRONT_SIDE_IS_LIT)
				return ((int) ((color & 0xff0000) * ambientLightLevel) & 0xff0000)
						+ ((int) ((color & 0xff00) * ambientLightLevel) & 0xff00)
						+ (int) ((color & 0xff) * ambientLightLevel);
			if (viewType == FRONT_SIDE_IS_VISIBLE)
				return EMPTY_COLOR;
			intens = -1;
		} else {
			if (viewType == BACK_SIDE_IS_VISIBLE)
				return EMPTY_COLOR;
			if ((viewType == BACK_SIDE_IS_LIT))
				return ((int) ((color & 0xff0000) * ambientLightLevel) & 0xff0000)
						+ ((int) ((color & 0xff00) * ambientLightLevel) & 0xff00)
						+ (int) ((color & 0xff) * ambientLightLevel);
			intens = 1;
		}
		intens = intens * (normal.x * mainLight.x + normal.y * mainLight.y + normal.z * mainLight.z);

		//try to kill the light pollution on the edge
		//TODO: I need a better idea.				
		float criticFactor = position.z / 40;
		if ((watcherFactor < criticFactor) && (watcherFactor > -criticFactor))
			if (intens > 0)
				intens = -intens;

		if (intens < ambientLightLevel)
			intens = ambientLightLevel;

		return ((int) ((color & 0xff0000) * intens) & 0xff0000)
				+ ((int) ((color & 0xff00) * intens) & 0xff00)
				+ (int) ((color & 0xff) * intens);
		
	}

	
	

	public float getAmbientLightLevel() {
		return ambientLightLevel;
	}

	public Vector3D getMainLight() {
		return mainLight;
	}

	/**
	 * fills the color buffer specified color and mark all buffer is empty 
	 * @param color
	 */
	public void cleanDrawBuffer(int color) {
		for (int i = 0; i < numberOfPixels; i++) {
			depthBuffer[i] = 0f;
			colorBuffer[i] = color;
		}
	}


	/**
	 * draws (fills buffer arrays) triangle that defined the PixelZ vertex array
	 * @param pVertexes array of vertex of the triangle 
	 * @param color
	 */
	public void dr3angl(PixelZ pVertexes[], int color) {

		vertexes[0].set(pVertexes[0]);
		vertexes[1].set(pVertexes[1]);
		vertexes[2].set(pVertexes[2]);

		// sort the vertexes by Y
		PixelZ tmpVertex;
		if (vertexes[0].y > vertexes[1].y) {
			tmpVertex = vertexes[1];
			vertexes[1] = vertexes[0];
			vertexes[0] = tmpVertex;
		}
		if (vertexes[1].y > vertexes[2].y) {
			tmpVertex = vertexes[2];
			vertexes[2] = vertexes[1];
			vertexes[1] = tmpVertex;
			if (vertexes[0].y > vertexes[1].y) {
				vertexes[1] = vertexes[0];
				vertexes[0] = tmpVertex;
			}
		}

		// check if the triangle out of the screen
		if ((vertexes[2].y < 0) || (vertexes[0].y >= height))
			return;
		if (vertexes[2].y == vertexes[0].y) // degenerated into a line, dont
											// draw it
			return;
		if ((vertexes[0].x & vertexes[1].x & vertexes[2].x) < 0)
			return;
		if ((vertexes[0].x >= width) && (vertexes[1].x >= width)
				&& (vertexes[2].x >= width))
			return;

		// sort by X
		if (vertexes[0].y == vertexes[1].y) {
			if (vertexes[0].x > vertexes[1].x) {
				tmpVertex = vertexes[1];
				vertexes[1] = vertexes[0];
				vertexes[0] = tmpVertex;
			}
		}

		// compute the slopes of the sides of the triangle.
		// for the line formula: x = slopeX * y + b and z = slopeZ * x + b2
		//
		float slopeX1, slopeX2, slopeX3;
		float slopeZx, slopeZy, endX, startX;
		int deltaY1, deltaY2;
		float depth;
		deltaY1 = vertexes[1].y - vertexes[0].y;
		deltaY2 = vertexes[2].y - vertexes[0].y;
		slopeX1 = (float) (vertexes[1].x - vertexes[0].x) / deltaY1;
		slopeX2 = (float) (vertexes[2].x - vertexes[0].x) / deltaY2;
		if (slopeX1 == slopeX2)
			return;
		slopeX3 = (float) (vertexes[2].x - vertexes[1].x)
				/ (vertexes[2].y - vertexes[1].y);
		slopeZy = (float) (vertexes[2].depth - vertexes[0].depth) / deltaY2;
		slopeZx = (vertexes[1].depth - vertexes[0].depth - slopeZy * deltaY1)
				/ (vertexes[1].x - vertexes[0].x - slopeX2 * deltaY1);

		currentY = vertexes[0].y;
		int maxY = vertexes[2].y;
		if (maxY > height)
			maxY = height;
		
		//Start to fill triangle space
		currentColor = color ;
		if (slopeX1 > slopeX2) {
			startX = vertexes[0].x;
			depth = currentZ = vertexes[0].depth;
			if (deltaY1 != 0) {
				endX = startX;
				if (vertexes[1].y >= maxY)
					deltaY2 = maxY;
				else
					deltaY2 = vertexes[1].y;
				for (; currentY < deltaY2; currentY++) {
					for (currentX = (int) startX; currentX <= endX; currentX++) {
						drawCurrentPixel();
						currentZ += slopeZx;
					}
					depth += slopeZy;
					endX += slopeX1;
					startX += slopeX2;
					currentZ = depth;
				}
			} else
				endX = vertexes[1].x;
			for (; currentY < maxY; currentY++) {
				for (currentX = (int) startX; currentX <= endX; currentX++) {
					drawCurrentPixel();
					currentZ += slopeZx;
				}
				depth += slopeZy;
				endX += slopeX3;
				startX += slopeX2;
				currentZ = depth;
			}
		} 
		
		else {
			startX = vertexes[0].x;
			depth = currentZ = vertexes[0].depth;
			if (deltaY1 != 0) {
				endX = startX;
				if (vertexes[1].y >= maxY)
					deltaY2 = maxY;
				else
					deltaY2 = vertexes[1].y;
				for (; currentY < deltaY2; currentY++) {
					for (currentX = (int) startX; currentX >= endX; currentX--) {
						drawCurrentPixel();
						currentZ -= slopeZx;
					}
					depth += slopeZy;
					endX += slopeX1;
					startX += slopeX2;
					currentZ = depth;
				}
			} else {
				endX = vertexes[1].x;
			}
			for (; currentY < maxY; currentY++) {
				for (currentX = (int) startX; currentX >= endX; currentX--) {
					drawCurrentPixel();
					currentZ -= slopeZx;
				}
				depth += slopeZy;
				endX += slopeX3;
				startX += slopeX2;
				currentZ = depth;
			}
		}

	}
}
