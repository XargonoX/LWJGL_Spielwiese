package Spielwiese;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

public class MyDisplay {
	/**
	 * Saves the current Frames per Second 
	 */
	private long fps_l;
	/**
	 * Time at last Frame
	 */
	private long lastTime_l;
	/**
	 * 3d vector to store the camera's position in
	 */
    private Vector3f position_V3f = null;
    /**
     * the rotation around the Y axis of the camera
     */
    private float CameraYyaw_f = 0.0f;
    /**
     * the rotation around the X axis of the camera
     */
    private float CameraPitch_f = 0.0f;
    /**
     * Step Size of Moving Objects
     */
    private float positionStepSize_f = 0.1f;
    /**
     * Step Size of rotating Objects
     */
    private float rotationStepSize_f = 0.1f;
	
	/**
	 * Current Rotation angle of the Quad
	 */
	private float quadRotationX_f = 0f;
	private float quadRotationY_f = 0f;
	private float quadRotationZ_f = 0f;
	
	/**
	 * Current Position of the Quad
	 */
	private float quadPositionX_f = 0f;
	private float quadPositionY_f = 0f;
	private float quadPositionZ_f = -2f;
	
	private float view_f = 10f;
	
	public MyDisplay(){
		lastTime_l = Sys.getTime();
	}
	
	public static void main(String[] argv) {
		MyDisplay MainDisplay = new MyDisplay();
		MainDisplay.createDisplay();
		MainDisplay.initializeOpenGL();
		while (!Display.isCloseRequested()) {
		    MainDisplay.checkKeyboard();
		}
		Display.destroy();
	}

	public void updatefps() {
		if (Sys.getTime() - lastTime_l > 1000) {
			Display.setTitle("Heikos Window -- fps_l: " + fps_l);
			fps_l = 0;
			lastTime_l += 1000;
		}
		fps_l++;
	}
	private void createDisplay(){
		try {
			Display.setDisplayMode(new DisplayMode(1024,768));
			Display.setTitle("Heikos Window");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	private void initializeOpenGL(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_CULL_FACE);
 
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
 
        final double fov = 45.0;
        final double zNear = 0.01;
        final double zFar = 100;
        final double aspect = (double) Display.getDisplayMode().getWidth() / (double) Display.getDisplayMode().getHeight();
        final double yPlane = Math.tan(Math.toRadians(fov / 2.0)) * zNear;
        final double xPlane = yPlane * aspect;
 
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glFrustum(-xPlane, +xPlane, -yPlane, +yPlane, zNear, zFar);
 
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
	}
	private void drawQuad(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -5f);
        GL11.glRotatef(quadRotationX_f, 1.0f + quadPositionX_f, 0.0f, 0.0f);
        GL11.glRotatef(quadRotationY_f, 0.0f, 1.0f + quadPositionY_f, 0.0f); // Rotate The cube around the Y axis
        GL11.glRotatef(quadRotationZ_f, 0.0f, 0.0f, 1.0f + quadPositionZ_f);
        
        GL11.glBegin(GL11.GL_QUADS);
        // Front
        GL11.glColor3f(1.0f, 0.5f, 0.0f);
        GL11.glVertex3f(-1.0f + quadPositionX_f, -1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, -1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, +1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(-1.0f + quadPositionX_f, +1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
 
        // Back
        GL11.glColor3f(0.0f, 0.5f, 0.0f);
        GL11.glVertex3f(-1.0f + quadPositionX_f, -1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(-1.0f + quadPositionX_f, +1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, +1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, -1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
 
        // Left
        GL11.glColor3f(0.0f, 0.5f, 1.0f);
        GL11.glVertex3f(-1.0f + quadPositionX_f, -1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(-1.0f + quadPositionX_f, -1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(-1.0f + quadPositionX_f, +1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(-1.0f + quadPositionX_f, +1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
 
        // Right
        GL11.glColor3f(0.5f, 1.0f, 0.0f);
        GL11.glVertex3f(+1.0f + quadPositionX_f, -1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, +1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, +1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, -1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
 
        // Down
        GL11.glColor3f(0.5f, 0.0f, 1.0f);
        GL11.glVertex3f(-1.0f + quadPositionX_f, -1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(-1.0f + quadPositionX_f, -1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, -1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, -1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        
        // Up
        GL11.glColor3f(1.0f, 0.0f, 0.5f);
        GL11.glVertex3f(-1.0f + quadPositionX_f, +1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
        GL11.glVertex3f(-1.0f + quadPositionX_f, +1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, +1.0f + quadPositionY_f, +1.0f + quadPositionZ_f );
        GL11.glVertex3f(+1.0f + quadPositionX_f, +1.0f + quadPositionY_f, -1.0f + quadPositionZ_f );
 
        GL11.glEnd();
 

	}
	
	private void drawRoom(){
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
 
        GL11.glTranslatef(0.0f, 0.0f, -5f);
        GL11.glRotatef(0, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(0, 0.0f, 1.0f, 0.0f); // Rotate The cube around the Y axis
        GL11.glRotatef(0, 0.0f, 0.0f, 1.0f);
 
        GL11.glBegin(GL11.GL_QUADS);
 
        // Back
        GL11.glColor3f(0.7f, 1.0f, 1.0f);
        GL11.glVertex3f(-4.0f, -4.0f, -4.0f -view_f);
        GL11.glVertex3f(+4.0f, -4.0f, -4.0f -view_f);
        GL11.glVertex3f(+4.0f, +4.0f, -4.0f -view_f);
        GL11.glVertex3f(-4.0f, +4.0f, -4.0f -view_f);
        
        //Left
        GL11.glColor3f(0.7f, 0.7f, 0.4f);
        GL11.glVertex3f(-4.0f, -4.0f, -4.0f -view_f);
        GL11.glVertex3f(-4.0f, +4.0f, -4.0f -view_f);
        GL11.glVertex3f(-4.0f, +4.0f, +4.0f +view_f);
        GL11.glVertex3f(-4.0f, -4.0f, +4.0f +view_f);
        
      //Right
        GL11.glColor3f(0.7f, 0.7f, 0.4f);
        GL11.glVertex3f(+4.0f, -4.0f, -4.0f -view_f);
        GL11.glVertex3f(+4.0f, -4.0f, +4.0f +view_f);
        GL11.glVertex3f(+4.0f, +4.0f, +4.0f +view_f);
        GL11.glVertex3f(+4.0f, +4.0f, -4.0f -view_f);
        
      //Top
        GL11.glColor3f(0.3f, 0.3f, 0.3f);
        GL11.glVertex3f(-4.0f, +4.0f, -4.0f -view_f);
        GL11.glVertex3f(+4.0f, +4.0f, -4.0f -view_f);
        GL11.glVertex3f(+4.0f, +4.0f, +4.0f +view_f);
        GL11.glVertex3f(-4.0f, +4.0f, +4.0f +view_f);
        
        //Floor
        GL11.glColor3f(0.3f, 0.7f, 0.7f);
        GL11.glVertex3f(-4.0f, -4.0f, +4.0f +view_f);
        GL11.glVertex3f(+4.0f, -4.0f, +4.0f +view_f);
        GL11.glVertex3f(+4.0f, -4.0f, -4.0f -view_f);
        GL11.glVertex3f(-4.0f, -4.0f, -4.0f -view_f);
        
        
       
        
        GL11.glEnd();
		
		
	}
	
	private void checkKeyboard(){
		
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
					quadPositionY_f++;
				    refreshView();
				    System.out.println("Left");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
					quadPositionY_f--;
				    refreshView();
				    System.out.println("Right");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
					quadPositionX_f++;
				    refreshView();
				    System.out.println("Up");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					quadPositionX_f--;
					refreshView();
					System.out.println("Down");
				}	
				if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
					quadPositionZ_f++;
					refreshView();
					System.out.println("Down");
					}
				if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
					quadPositionZ_f--;
					refreshView();
					System.out.println("Down");
					}
				if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				    quadRotationX_f += 0.5;
				    refreshView();
				    System.out.println("Rotation_X");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				    quadRotationX_f -= 0.5;
				    refreshView();
				    System.out.println("Rotation_Z");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				    quadRotationY_f+=0.5;
				    refreshView();
				    System.out.println("Rotation_Y");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				    quadRotationY_f-=0.5;
				    refreshView();
				    System.out.println("Rotation_Y");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				    quadRotationZ_f+=0.5;
				    refreshView();
				    System.out.println("Rotation_Y");
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
				    quadRotationZ_f-=0.5;
				    refreshView();
				    System.out.println("Rotation_Y");
				}
		refreshView();
	}
	private void refreshView(){
		drawQuad();
		drawRoom();
	   	updatefps();
	   	Display.sync(60);
	    Display.update();
	}
}