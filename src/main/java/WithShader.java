import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class WithShader implements GLEventListener{

	private int fragmentShader;
	private int vertexShader;
	private int shaderProgram;
	private static FPSAnimator animator;
	
	private GLU glu;
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JFrame frame = new JFrame("test");
				frame.setSize(800, 800);
				frame.setVisible(true);
				GLCanvas canvas = new GLCanvas();
				canvas.addGLEventListener(new WithShader());
				frame.getContentPane().setLayout(new BorderLayout());
				frame.getContentPane().add(canvas, BorderLayout.CENTER);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				animator = new FPSAnimator(canvas, 60, true);
				animator.start();
			}
		});
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL2 gl = arg0.getGL().getGL2();
//		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glBegin(GL2.GL_POLYGON);
	      {
	         gl.glTexCoord2f(0.0f, 1.0f);
	         gl.glVertex3f(-1f, 1.0f, 1.0f);  // Top Left
	         gl.glTexCoord2f(1.0f, 1.0f);
	         gl.glVertex3f(1.0f, 1.0f, 1.0f);   // Top Right
	         gl.glTexCoord2f(1.0f, 0.0f);
	         gl.glVertex3f(1.0f, -1.0f, 1.0f);  // Bottom Right
	         gl.glTexCoord2f(0.0f, 0.0f);
	         gl.glVertex3f(-1f, -1.0f, 1.0f); // Bottom Left
	      }
	      // Done Drawing The Quad
	        gl.glEnd();

	        // Flush all drawing operations to the graphics card
	        gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL2 gl = arg0.getGL().getGL2();
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.setSwapInterval(1);
		attachShader(gl);
		
	}

	public void attachShader(GL2 gl)
	{
		vertexShader = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		fragmentShader = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
		String[] vert =  {//"#version 330\n" +
//				"layout(location = 0) in vec3 position;\n" +
				"void main()\n" +
				"{\n" +
				"\tgl_Position = ftransform();\n" +
				"}"};
		String[] frag = { //"#version 330\n" +
				//"out vec4 color;\n" +
				"void main()\n" +
				"{\n" +
				"\tgl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
				"}"};
		gl.glShaderSource(vertexShader, 1, vert, null, 0);
		gl.glCompileShader(vertexShader);
		gl.glShaderSource(fragmentShader, 1, frag, null, 0);
		gl.glCompileShader(fragmentShader);
		
		shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertexShader);
		gl.glAttachShader(shaderProgram, fragmentShader);
		gl.glLinkProgram(shaderProgram);
		gl.glUseProgram(shaderProgram);
	}
	
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		GL2 gl = arg0.getGL().getGL2();
		glu = new GLU();
		if (arg4 <= 0) {
			arg4 = 1;
		}
		
		float aspect = (float)arg3 / arg4;
		gl.glViewport(0, 0, arg3, arg4);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45, aspect, 0.1, 100);
        glu.gluLookAt(0, 0, 10, 0, 0, -5, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

}
