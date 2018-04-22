import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

public class TestSphere extends JPanel implements GLEventListener {
	private static final int REFRESH_FPS = 60;
	private GLU glu;
	private static FPSAnimator animator;
	private GLCanvas canvas;
	private Texture texture;
	private String textureFileName;
	
	private int rotate = 1;
	private float textureLeft;
	private float textureRight;
	private float textureTop;
	private float textureBottom;
	
	public TestSphere() {
		// TODO Auto-generated constructor stub
		canvas = new GLCanvas();
		this.setLayout(new BorderLayout());
		this.add(canvas, BorderLayout.CENTER);
		canvas.addGLEventListener(this);
		canvas.setFocusable(true);
		canvas.requestFocus();
		textureFileName = "galaxy3.png";
		animator = new FPSAnimator(canvas, REFRESH_FPS, true);
	}
	
	public static void main(String[] args)
	{
		final int WINDOW_WIDTH = 800;
		final int WINDOW_HEIGHT = 800;
		JFrame frame = new JFrame("test_windows");
		TestSphere sphere = new TestSphere();
		frame.setContentPane(sphere);
		frame.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		frame.setVisible(true);
		animator.start();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepth(1.0);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		
		try
		{
			File image = new File(textureFileName);
			texture = TextureIO.newTexture(image, false);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			
			TextureCoords textureCoords = texture.getImageTexCoords();
			textureBottom = textureCoords.bottom();
			textureLeft = textureCoords.left();
			textureRight = textureCoords.right();
			textureTop = textureCoords.top();
			
		} catch (GLException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		float[] lightAmbientValue = {0.5f, 0.5f, 0.5f, 1.0f};
		gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_AMBIENT, lightAmbientValue, 0);
		
		float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
		gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_DIFFUSE, lightDiffuseValue, 0);
		
		float[] lightDiffusePosition = {0.0f, 0.0f, 15.0f, 1.0f};
		gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_POSITION, lightDiffusePosition, 0);
		
		gl.glEnable(GLLightingFunc.GL_LIGHT1);
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		gl.glEnable(GL.GL_BLEND);
//		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
//		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glLoadIdentity();
		gl.glRotated(rotate, 0, 0, 1);
		gl.glRotated(rotate, 0, 1, 0);
		rotate += 1;
		gl.glTranslatef(0.0f, 0.0f, 0);
		texture.enable(gl);
		texture.bind(gl);
		gl.glEnable(GL2.GL_LIGHTING);
		
		float[] rgba = {1f, 1f, 1f, 0.8f};
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT, rgba, 1);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_SPECULAR, rgba, 1);
        gl.glMaterialf(GL.GL_FRONT, GLLightingFunc.GL_SHININESS, 0.5f);
        
		GLUquadric earth = glu.gluNewQuadric();
		glu.gluQuadricTexture(earth, true);
		glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
		glu.gluQuadricNormals(earth, GLU.GLU_SMOOTH);
		glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
//		glu.gluQuadricOrientation(earth, GLU.GLU_INSIDE);
		
		final float radius = 10f;
		final int slice = 40;
		final int stacks = 16;
		glu.gluSphere(earth, radius, slice, stacks);
		
//		gl.glTranslatef(10.0f, 0.0f, 0);
//
//		glu.gluSphere(earth, radius, slice, stacks);

		glu.gluDeleteQuadric(earth);
		gl.glFlush();
//		gl.glBegin(GL2.GL_QUADS); // of the color cube
//
//	      // Front Face
//	      gl.glNormal3f(0.0f, 0.0f, 1.0f);
//	      gl.glTexCoord2f(textureLeft, textureBottom);
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f); // bottom-left of the texture and quad
//	      gl.glTexCoord2f(textureRight, textureBottom);
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f); // bottom-right of the texture and quad
//	      gl.glTexCoord2f(textureRight, textureTop);
//	      gl.glVertex3f(1.0f, 1.0f, 1.0f); // top-right of the texture and quad
//	      gl.glTexCoord2f(textureLeft, textureTop);
//	      gl.glVertex3f(-1.0f, 1.0f, 1.0f); // top-left of the texture and quad
//
//	      // Back Face
//	      gl.glNormal3f(0.0f, 0.0f, -1.0f);
//	      gl.glTexCoord2f(textureRight, textureBottom);
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	      gl.glTexCoord2f(textureRight, textureTop);
//	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//	      gl.glTexCoord2f(textureLeft, textureTop);
//	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//	      gl.glTexCoord2f(textureLeft, textureBottom);
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	      
//	      // Top Face
//	      gl.glNormal3f(0.0f, 1.0f, 0.0f);
//	      gl.glTexCoord2f(textureLeft, textureTop);
//	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//	      gl.glTexCoord2f(textureLeft, textureBottom);
//	      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
//	      gl.glTexCoord2f(textureRight, textureBottom);
//	      gl.glVertex3f(1.0f, 1.0f, 1.0f);
//	      gl.glTexCoord2f(textureRight, textureTop);
//	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//	      
//	      // Bottom Face
//	      gl.glNormal3f(0.0f, -1.0f, 0.0f);
//	      gl.glTexCoord2f(textureRight, textureTop);
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	      gl.glTexCoord2f(textureLeft, textureTop);
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	      gl.glTexCoord2f(textureLeft, textureBottom);
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//	      gl.glTexCoord2f(textureRight, textureBottom);
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//	      
//	      // Right face
//	      gl.glNormal3f(1.0f, 0.0f, 0.0f);
//	      gl.glTexCoord2f(textureRight, textureBottom);
//	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//	      gl.glTexCoord2f(textureRight, textureTop);
//	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//	      gl.glTexCoord2f(textureLeft, textureTop);
//	      gl.glVertex3f(1.0f, 1.0f, 1.0f);
//	      gl.glTexCoord2f(textureLeft, textureBottom);
//	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//	      
//	      // Left Face
//	      gl.glNormal3f(-1.0f, 0.0f, 0.0f);
//	      gl.glTexCoord2f(textureLeft, textureBottom);
//	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//	      gl.glTexCoord2f(textureRight, textureBottom);
//	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//	      gl.glTexCoord2f(textureRight, textureTop);
//	      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
//	      gl.glTexCoord2f(textureLeft, textureTop);
//	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//
//	      gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		if (height <= 0) {
			height = 1;
		}
		
		float aspect = (float)width / (float) height;
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(30.0, aspect, 0.1, 100);
		glu.gluLookAt(0, 0, 101, 0, 0, 0, -40, 0, 0);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

}
