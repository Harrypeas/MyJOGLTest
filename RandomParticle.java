import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

import javafx.scene.layout.Border;
import sun.net.www.content.text.plain;

import static java.awt.event.KeyEvent.VK_T;

/**
 * NeHe Lesson #19a: Fireworks
 */
public class RandomParticle implements com.jogamp.opengl.GLEventListener, KeyListener {
   private static String TITLE = "NeHe Lesson #19a: Fireworks";  // window's title
   private static final int CANVAS_WIDTH = 600;  // width of the drawable
   private static final int CANVAS_HEIGHT = 600; // height of the drawable
   private static final int FPS = 60; // animator's target frames per second
   private static GLJPanel canvas;
   private com.jogamp.opengl.glu.GLU glu;  // for the GL Utility
   
   private static final int MAX_PARTICLES = 100; // max number of particles
   private Particle[] particles = new Particle[MAX_PARTICLES];


   private int rotate = 0;
   // Texture applied over the shape
   private Texture texture;
   private String textureFileName = "earth_cartoon.png";

   private float textureTop, textureBottom, textureLeft, textureRight;

   /** The entry main() method */
   public static void main(String[] args) {
	  GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
	  capabilities.setBackgroundOpaque(false);
	  capabilities.setAlphaBits(8);
//	  capabilities.set
	  capabilities.setDoubleBuffered(true);
	  capabilities.setHardwareAccelerated(true);

      canvas = new GLJPanel(capabilities);  // heavy-weight GLCanvas
      canvas.setRequestFocusEnabled(true);
      canvas.shouldPreserveColorBufferIfTranslucent();
      canvas.setAutoSwapBufferMode(true);
      canvas.repaint();
//      canvas.pres
      canvas.setLayout(new BorderLayout());
      RandomParticle renderer = new RandomParticle();
      
      canvas.addGLEventListener(renderer);
      canvas.setOpaque(false);
      
      JPanel panel = new JPanel()
    		  {
    	  @Override
    	protected void paintComponent(Graphics g) {
    		// TODO Auto-generated method stub
    		super.paintComponent(g);
    		setOpaque(false);
    		g.setColor(new Color(255,  0, 0,100));
    		g.fillOval(20, 20, 100, 100);
    	}
    		  };
    		  
    		  canvas.add(panel, BorderLayout.CENTER);
      
      // Create the top-level container frame
      final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
      frame.setUndecorated(true);
      frame.setBackground(new Color(0, 0, 0,0));
      frame.setVisible(true);
      frame.setSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      frame.setLayout(new BorderLayout());
      frame.getContentPane().add(canvas, BorderLayout.CENTER);

      frame.addWindowListener(new WindowAdapter() {
         @Override 
         public void windowClosing(WindowEvent e) {
            // Use a dedicate thread to run the stop() to ensure that the
            // animator stops before program exits.
            new Thread() {
               @Override 
               public void run() {
//                  animator.stop(); // stop the animator loop
                  System.exit(0);
               }
            }.start();
         }
      });
      frame.setTitle(TITLE);
      AnimatorBase animator = new FPSAnimator(canvas, 60);
      canvas.getAnimator().start();
   }
   
   // ------ Implement methods declared in GLEventListener ------

   /**
    * Called back immediately after the OpenGL context is initialized. Can be used 
    * to perform one-time initialization. Run only once.
    */
   @Override
   public void init(com.jogamp.opengl.GLAutoDrawable drawable) {
      com.jogamp.opengl.GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
      glu = new com.jogamp.opengl.glu.GLU();           

      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
      gl.glClearDepth(1.0f);      // set clear depth value to farthest

      gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction
      gl.glShadeModel(GLLightingFunc.GL_SMOOTH); // blends colors nicely, and smoothes out lighting
//      gl.glEnable(GLLightingFunc.GL_NORMALIZE);
      
      gl.glBlendFunc(com.jogamp.opengl.GL.GL_ONE, com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA); // Type Of Blending To Perform
      gl.glEnable(GL.GL_LINE_SMOOTH);
//      gl.glEnable(com.jogamp.opengl.GL.GL_BLEND); // Enable Blending
      gl.glEnable(GL2.GL_ALPHA_TEST);
//      gl.glAlphaFunc(GL2.GL_GREATER, 0.1f);
      gl.glClearColor(1, 1, 1, 0);
      gl.setSwapInterval(1);

      try {
         // Use URL so that can read from JAR and disk file.
         texture = TextureIO.newTexture(
               new File(textureFileName), false);
         texture.bind(gl);
         texture.enable(gl);
      } catch (com.jogamp.opengl.GLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

//      gl.glEnable(GL2.GL_COLOR_MATERIAL);
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
      // Use linear filter for texture if image is smaller than the original texture
      gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);

      // Texture image flips vertically. Shall use TextureCoords class to retrieve
      // the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
      TextureCoords textureCoords = texture.getImageTexCoords();
      textureTop = textureCoords.top();
      textureBottom = textureCoords.bottom();
      textureLeft = textureCoords.left();
      textureRight = textureCoords.right();

      // Initialize the particles
      for (int i = 0; i < MAX_PARTICLES; i++) {
    	 float posx = (float)Math.random() * 20 - 10;
    	 float posy = (float)Math.random() * 20 - 10;
    	 float posz = (float)Math.random() * 20 - 10;
    	 float[] color = {(float)Math.random(),(float)Math.random(),(float)Math.random(),1.0f};
         particles[i] = new Particle(posx, posy, posz, color);
      }
      
      gl.glFlush();
   }

   /**
    * Call-back handler for window re-size event. Also called when the drawable is 
    * first set to visible.
    */
   @Override
   public void reshape(com.jogamp.opengl.GLAutoDrawable drawable, int x, int y, int width, int height) {
	   com.jogamp.opengl.GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

      if (height == 0) height = 1;   // prevent divide by zero
      float aspect = (float)width / height;

      // Set the view port (display area) to cover the entire window
      gl.glViewport(0, 0, width, height);

      // Setup perspective projection, with aspect ratio matches viewport
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);  // choose projection matrix
      gl.glLoadIdentity();             // reset projection matrix
      glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
      glu.gluLookAt(0, 0, 30, 0, 0, 0, 0, 1, 0);
      // Enable the model-view transform
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glLoadIdentity(); // reset
   }

   /**
    * Called back by the animator to perform rendering.
    */
   @Override
   public void display(com.jogamp.opengl.GLAutoDrawable drawable) {
	  com.jogamp.opengl.GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	  gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
      gl.glLoadIdentity();  // reset the model-view matrix
      
      gl.glRotatef(rotate, 0, 0, 1);
//      gl.glRotatef(rotate, 0, 1, 0);
      rotate += 2;

      for (int i = 0; i < MAX_PARTICLES; i++) {
         if (true) {
           
//        	gl.glColor4f(particles[i].color[0], particles[i].color[1], particles[i].color[2], particles[i].color[3]);
            gl.glBegin(GL.GL_TRIANGLE_STRIP); // build quad from a triangle strip

            float px = particles[i].x;
            float py = particles[i].y;
            float pz = particles[i].z;

            gl.glTexCoord2d(textureRight, textureTop);
            gl.glVertex3f(px + 0.2f, py + 0.2f, pz); // Top Right
            gl.glTexCoord2d(textureLeft, textureTop);
            gl.glVertex3f(px - 0.2f, py + 0.2f, pz); // Top Left
            gl.glTexCoord2d(textureRight, textureBottom);
            gl.glVertex3f(px + 0.2f, py - 0.2f, pz); // Bottom Right
            gl.glTexCoord2d(textureLeft, textureBottom);
            gl.glVertex3f(px - 0.2f, py - 0.2f, pz); // Bottom Left
            gl.glEnd();

            particles[i].rotate();
         }
      	}
      drawable.swapBuffers();
      }

   /** 
    * Called back before the OpenGL context is destroyed. Release resource such as buffers. 
    */
   @Override
   public void dispose(com.jogamp.opengl.GLAutoDrawable drawable) { }
   
   class Particle
   {
	   float x;
	   float y;
	   float z;
	   float[] color;
	   float direction;
	   
	   public Particle(float x, float y, float z, float[] color) {
		// TODO Auto-generated constructor stub
		   this.x = x;
		   this.y = y;
		   this.z = z;
		   this.color = color;
		   direction = (float) (Math.random() - 0.5);
	}
	   
	   public void rotate()
	   {
		   x = x + direction / 2;
		 
		   y = 	y + direction / 2;
		   
		   z = z + direction / 2;
		   
		   if (x > 10) {
			x = -10;
		}
		   else if (x < -10) {
			x = 10;
		}
		   else if (y > 10) {
			y= -10;
		}
		   else if (y < -10) {
			y = 10;
		}
		   else if (z > 30) {
				z= -10;
				direction = (float) (Math.random() - 0.5);
			}
			   else if (z < -10) {
				z = 30;
				direction = (float) (Math.random() - 0.5);
			}
	   }
   }

   @Override
   public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
         case VK_T:
//            if (!enabledBurst) enabledBurst = true;
            break;
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {}

   @Override
   public void keyTyped(KeyEvent e) {}
}
