import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class NotShader implements GLEventListener, MouseWheelListener {

	private static int FPS = 60;
	private static FPSAnimator animator;
	private static GLU glu;
	private static Texture texture;
	
	private float texcoordTop;
	private float texcoordBottom;
	private float texcoordLeft;
	private float texcoordRight;
	
	private float rotate_angle = 0;
	private float eye = 5;
	private float aspect;
	
	private float[] light_Ambient = {1f, 1f, 1f, 1.0f};
	private float[] light_Diffuse = {0.8f, 0.8f, 0.8f, 1.0f};
	private float lightPos[] = {0.0f, 0.0f, 10.0f, 1.0f};
	
	private String texture_name = "galaxy.png";
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame("NO_SHADER");
				frame.setSize(600, 600);
				frame.setVisible(true);
				GLCanvas glCanvas = new GLCanvas();
//				glCanvas.setPreferredSize(new Dimension(600,600));
				frame.getContentPane().add(glCanvas);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				NotShader render = new NotShader();
				glCanvas.addGLEventListener(render);
				glCanvas.addMouseWheelListener(render);
				glCanvas.setFocusable(true);
				glCanvas.requestFocus();
				
				animator = new FPSAnimator(glCanvas, FPS, true);
				glCanvas.getAnimator().start();
			}
		});
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL2 gl = arg0.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		setEye(gl);
		gl.glLoadIdentity();
		gl.glRotatef(rotate_angle, 0, 0, 1);
		texture.enable(gl);
		texture.bind(gl);
		gl.glEnable(GL2.GL_LIGHTING);
		
		gl.glBegin(GL2.GL_POLYGON);
		gl.glNormal3i(0, 0, 1);
		gl.glTexCoord2f(texcoordLeft, texcoordTop);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(texcoordRight, texcoordTop);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glTexCoord2f(texcoordRight, texcoordBottom);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);
		gl.glTexCoord2f(texcoordLeft, texcoordBottom);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glEnd();
		
		rotate_angle += 1;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL2 gl = arg0.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepth(1.0f);
		
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glShadeModel(GL2.GL_FLAT);
		
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);

		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_NORMALIZE);
//		gl.glHint(GL2.GL_POINT_SMOOTH_HINT, GL2.GL_NICEST);
		
		try {
			File file = new File(texture_name);
			texture = TextureIO.newTexture(file, false);
			
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_MIPMAP);
			
			texcoordTop = texture.getImageTexCoords().top();
			texcoordBottom = texture.getImageTexCoords().bottom();
			texcoordLeft = texture.getImageTexCoords().left();
			texcoordRight = texture.getImageTexCoords().right();
			
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, light_Ambient, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, light_Diffuse, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
			gl.glEnable(GL2.GL_LIGHT1);
//			gl.glDisable(GL2.GL_LIGHTING);

			gl.glColor4f(1f, 1f, 1f, 1f);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		GL2 gl = arg0.getGL().getGL2();
		if (arg4 <= 0) {
			arg4 = 1;
		}
		gl.glViewport(0, 0, arg3, arg4);
		aspect = (float)arg3 / (float)arg4;
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45, aspect, 0.1, 100.0);
		glu.gluLookAt(0, 0, eye, 0, 0, -10, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void setEye(GL2 gl)
	{
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45, aspect, 0.1, 100.0);
		glu.gluLookAt(0, 0, eye, 0, 0, -10, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if (e.getWheelRotation() == 1) {
			eye -= 0.05;
//			System.out.println("eye1:" + eye);

		}
		else if (e.getWheelRotation() == -1) {
			eye += 0.05;
//			System.out.println("eye2:" + eye);
		}
	}
	
}
