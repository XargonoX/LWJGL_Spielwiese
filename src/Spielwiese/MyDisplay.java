package Spielwiese;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;
import org.lwjgl.util.glu.GLU;

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
    private float CameraYaw_f = 0.0f;
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
    
    private int targetWidth_i = 800;

    private int targetHeight_i = 600;
    
    private float xrot=0.1f;
    private float yrot=0.1f;
    private float zrot=0.1f;
    
	
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
		this.position_V3f = new Vector3f(0f, 0f, 20f);
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
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		//Calculate the aspect ratio of the window
		GLU.gluPerspective(45.0f,((float)targetWidth_i)/((float)targetHeight_i),0.1f,100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_TEXTURE_2D);    
		// Enable Texture Mapping ( NEW )
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
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


	public void modifyCameraYaw(float amount_f){
		this.CameraYaw_f += amount_f;
	
	}
	public void modifyCameraPitch(float amount_f){
		this.CameraPitch_f += amount_f;
	}
	public void walkForward(float distance_f){
		this.position_V3f.x -= distance_f * (float)Math.sin(Math.toRadians(this.CameraYaw_f));
		this.position_V3f.z += distance_f * (float)Math.cos(Math.toRadians(this.CameraYaw_f));
	}
	public void walkBackward(float distance_f){
		this.position_V3f.x += distance_f * (float)Math.sin(Math.toRadians(this.CameraYaw_f));
		this.position_V3f.z -= distance_f * (float)Math.cos(Math.toRadians(this.CameraYaw_f));
	}
	public void strafeLeft(float distance_f){
		this.position_V3f.x -= distance_f * (float)Math.sin(Math.toRadians(this.CameraYaw_f));
		this.position_V3f.z += distance_f * (float)Math.cos(Math.toRadians(this.CameraYaw_f));
	}
	public void strafeRight(float distance_f){
		this.position_V3f.x -= distance_f * (float)Math.sin(Math.toRadians(this.CameraYaw_f));
		this.position_V3f.z += distance_f * (float)Math.cos(Math.toRadians(this.CameraYaw_f));
	}
	
	public void lookThrough(){
		GL11.glRotatef(this.CameraPitch_f, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(this.CameraYaw_f, 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(position_V3f.x, position_V3f.y, position_V3f.z);
	}
		
	public void initDisplay(boolean fullscreen_b){
		DisplayMode chosenMode_dM = null;
		
		try{
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				
				for(int i=0; i<modes.length;i++){
					if((modes[i].getWidth() == this.targetWidth_i)&&(modes[i].getHeight() == this.targetHeight_i)){
						chosenMode_dM = modes[i];
						break;
					}
				}
		} catch (LWJGLException e){
			Sys.alert("Error", "Unable to determine display modes.");
			System.exit(0);
			
		}
		if (chosenMode_dM == null){
			Sys.alert("Error", "unable to find appropriate display mode.");
			System.exit(0);
		}
		
		try{
			Display.setDisplayMode(chosenMode_dM);
			Display.setFullscreen(fullscreen_b);
			Display.setTitle("My Secret Title");
			Display.create();
		}
		catch (LWJGLException e){
			Sys.alert("Error", "Unable to create display.");
			System.exit(0);
		}
		
	}

	  private void run(){
	      BasicFPS camera = new BasicFPS(0, 0, 0);
	            float dx        = 0.0f;
	            float dy        = 0.0f;
	            float dt        = 0.0f; //length of frame
	            float lastTime  = 0.0f; // when the last frame was
	            float time      = 0.0f;
	            float mouseSensitivity = 0.15f;
	            float movementSpeed = 10.0f; //move 10 units per second
	            //hide the mouse
	            Mouse.setGrabbed(true);
	        while(gameRunning_b){
	            update();
	            render();
	            Display.update();

	 

	             //keep looping till the display window is closed the ESC key is down

	            /*

	            while (!Display.isCloseRequested() ||

	             !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))

	                {

	                */

	                time = Sys.getTime();

	                

	                //here is your movement speed, which can be changed to anything

	                dt = 0.0005f;

	                

	                lastTime = time;

	 

	 

	                //distance in mouse movement from the last getDX() call.

	                dx = Mouse.getDX();

	                //distance in mouse movement from the last getDY() call.

	                dy = Mouse.getDY();

	 

	                //control camera yaw from x movement from the mouse

	                camera.yaw(dx * mouseSensitivity);

	                //control camera pitch from y movement from the mouse

	                camera.pitch(-dy * mouseSensitivity);

	 

	 

	                //when passing in the distrance to move

	                //we times the movementSpeed with dt this is a time scale

	                //so if its a slow frame u move more then a fast frame

	                //so on a slow computer you move just as fast as on a fast computer

	                

	                //OVER HERE! What do I do to make the boolean canWalk actually work the right way?

	                

	                if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward

	                {

	                    camera.walkForward(movementSpeed*dt);

	                }

	                if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards

	                {

	                    camera.walkBackwards(movementSpeed*dt);

	                }

	                if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left

	                {

	                    camera.strafeLeft(movementSpeed*dt);

	                }

	                if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right

	                {

	                    camera.strafeRight(movementSpeed*dt);

	                }

	 

	                //set the modelview matrix back to the identity

	                GL11.glLoadIdentity();

	                //look through the camera before you draw anything

	                camera.lookThrough();

	                //you would draw your scene here.

	 

	                //draw the buffer to the screen

	                //Display.update();

	            //}

	 

	            // finally check if the user has requested that the display be

	            // shutdown

	            if (Display.isCloseRequested()) {

	                    gameRunning = false;

	                    Display.destroy();

	                    System.exit(0);

	                }

	            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))

	            {

	                Sys.alert("Close","To continue, press ESCAPE on your keyboard or OK on the screen.");

	                System.exit(0);

	                

	            }

	        }

	    }

//Class End
}



