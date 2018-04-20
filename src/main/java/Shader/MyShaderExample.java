package Shader;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.AnimatorBase;

public class MyShaderExample  implements GLEventListener {

	private static AnimatorBase animator;
	private int[] VAO = new int[1];
	private int[] ArrayBuffer = new int[1];
	private int[] EBO = new int[1];
	private int[] textureId = new int[1];
	private int shaderProgram;
	private int vertShader;
	private int fragShader;
	private GLU glu;
	
	private float vertices[] = {
			-0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,0.0f, 0.0f,  // 0
			0.5f,  -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,1.0f, 0.0f,  // 1
			0.5f,  0.5f,  0.0f, 0.0f, 0.0f, 1.0f,1.0f, 1.0f,  // 2
			-0.5f, 0.5f,  0.0f, 1.0f, 1.0f, 0.0f,0.0f, 1.0f   // 3
	};
	
	private short[] indices = {
			0,1,2,
			0,2,3
	};
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GLCapabilities capabilities = new GLCapabilities(GLProfile.getMaximum(true));
				
				JFrame jFrame = new JFrame("test");
				jFrame.setSize(800, 800);
				jFrame.setVisible(true);
				GLCanvas canvas = new GLCanvas(capabilities);
				jFrame.getContentPane().setLayout(new BorderLayout());
				canvas.addGLEventListener(new MyShaderExample());
				animator = new Animator(canvas);
				animator.start();
				jFrame.getContentPane().add(canvas, BorderLayout.CENTER);
				jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
	
//	public MyShaderExample() {
//		// TODO Auto-generated constructor stub
//		
//	}
	
	private void BindBuffer(GL4 gl) throws IOException
	{	
		gl.glGenVertexArrays(1, VAO, 0);
		gl.glBindVertexArray(VAO[0]);
		
		gl.glGenBuffers(1, ArrayBuffer, 0);
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, ArrayBuffer[0]);
		
		FloatBuffer fBuffer = Buffers.newDirectFloatBuffer(vertices);
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertices.length * Float.SIZE / 8, fBuffer, GL4.GL_STATIC_DRAW);
		
		ShortBuffer sBuffer = Buffers.newDirectShortBuffer(indices);
		gl.glGenBuffers(1, EBO, 0);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.length * Short.SIZE / 8, sBuffer, GL4.GL_STATIC_DRAW);
		
		gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 8*(Float.SIZE) / 8, 0);
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false, 8*(Float.SIZE) / 8, 3*(Float.SIZE) / 8);
		gl.glEnableVertexAttribArray(1);
		gl.glVertexAttribPointer(2, 2, GL4.GL_FLOAT, false, 8*(Float.SIZE) / 8, 6 *(Float.SIZE) / 8);
		gl.glEnableVertexAttribArray(2);
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
		gl.glBindVertexArray(0);
		
		gl.glGenTextures(1, textureId,0);
		gl.glBindTexture(GL4.GL_TEXTURE_2D, textureId[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_NEAREST);
		TextureReader.Texture texture = TextureReader.readTexture("galaxy2.png");
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
		gl.glGenerateMipmap(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL4 gl = arg0.getGL().getGL4();
		gl.glClearColor(0.18f, 0.04f, 0.14f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl.glBindVertexArray(VAO[0]);
		gl.glUseProgram(shaderProgram);
		
		gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[0]);
		gl.glUniform1i(gl.glGetUniformLocation(shaderProgram, "tex"), 0);
		gl.glDrawElements(GL.GL_TRIANGLES, 6, GL.GL_UNSIGNED_SHORT, 0);
//		GL4.glDrawArrays(GL.GL_TRIANGLES, 0, 3);
		gl.glBindVertexArray(0);
		gl.glUseProgram(0);
		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		GL4 gl = arg0.getGL().getGL4();
		//gl.glversion
		System.out.println(gl.glGetString(GL4.GL_SHADING_LANGUAGE_VERSION));
		System.out.println(gl.glGetString(GL4.GL_VERSION));

		gl.setSwapInterval(1);
		try {
			BindBuffer(gl);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			bindShader(gl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void bindShader(GL4 gl) throws Exception
	{
		try
		{
		vertShader = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
		fragShader = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
		String[] vert =  {"#version 400\n" +
				"layout(location = 0) in vec3 position;\n" +
				"layout(location = 1) in vec3 color;\n" +
				"layout(location = 2) in vec2 textCoord;\n" +
				"out vec3 vertColor;\n" +
				"out vec2 TextCoord;\n" +
				"void main()\n" +
				"{\n" +
				"\tgl_Position = vec4(position, 1.0);\n" +
				"vertColor = color;\n" +
				"TextCoord = textCoord;\n"+
				"}"};
		String[] frag = { "#version 400\n" +
				"in vec3 vertColor;\n" +
				"in vec2 TextCoord;\n" +
				"uniform sampler2D tex;\n" +
				"out vec4 color;\n" +
				"void main()\n" +
				"{\n" +
//				"gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
				"color = texture(tex, TextCoord) * vec4(vertColor,0.5);\n" +
				"}"};
		
		gl.glShaderSource(vertShader, 1, vert, null);
		gl.glCompileShader(vertShader);
		
		gl.glShaderSource(fragShader, 1, frag, null);
		gl.glCompileShader(fragShader);
		
		shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertShader);
		gl.glAttachShader(shaderProgram, fragShader);
		gl.glLinkProgram(shaderProgram);
		gl.glValidateProgram(shaderProgram);
	      IntBuffer intBuffer = IntBuffer.allocate(1);
	      gl.glGetProgramiv(shaderProgram, GL4.GL_LINK_STATUS,intBuffer);
	      if (intBuffer.get(0)!=1){
	         gl.glGetProgramiv(shaderProgram, GL4.GL_INFO_LOG_LENGTH,intBuffer);
	         int size = intBuffer.get(0);
	         System.err.println("Program link error: ");
	         if (size>0){
	            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
	            gl.glGetProgramInfoLog(shaderProgram, size, intBuffer, byteBuffer);
	            for (byte b:byteBuffer.array()){
	               System.err.print((char)b);
	            }
	         } else {
	            System.out.println("Unknown");
	         }
	         System.exit(1);
	      }
		gl.glUseProgram(shaderProgram);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		gl.glDetachShader(shaderProgram, vertShader);
		gl.glDetachShader(shaderProgram, fragShader);
	}
	
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		GL4 gl = arg0.getGL().getGL4();
		glu = new GLU();
		if (arg3 <= 0) {
			arg3 = 1;
		}
		gl.glViewport(0, 0, arg3, arg4);
	}

}
