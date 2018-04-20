import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

public class OpacheTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2617534866019768531L;
	private static boolean isDrag = false;
	private static int x;
	private static int y;
	private static int hor = 0;
	private static int ver = 0;
	public static JFrame frame;
	public static int Width = 800;
	public static int Height = 800;
	private static int height_increament = 0;
	private static int width_increament = 0;
	
	private static boolean rs_S, rs_N, rs_W, rs_E, rs_SW, rs_SE, rs_NW, rs_NE;
	public static void main(String[] args) throws IOException
	{
		x= 100;
		y= 100;
		frame = new JFrame("test");
		frame.setAlwaysOnTop(true);
		JPanel panel2 = new OpachePanel();
		GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
		capabilities.setBackgroundOpaque(false);
		GLJPanel panel = new GLJPanel(capabilities);
		panel.addGLEventListener(new RandomParticle());
		FPSAnimator animator = new FPSAnimator(panel, 60, true);
		animator.start();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new LineBorder(Color.CYAN));
		panel.add(panel2, BorderLayout.CENTER);
		frame.setContentPane(panel);
		frame.setSize(Width,Height);
		frame.setUndecorated(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		
//		frame.setOpacity(0.7f);
		frame.setVisible(true);
		frame.setLocation(x, y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		rs_S = rs_N = rs_W = rs_E = rs_SW = rs_NW = rs_SE = rs_NE = false;
 		
		frame.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (isDrag == true) {
					isDrag = false;
					x = e.getXOnScreen() - hor;
					y = e.getYOnScreen() - ver;
				}
				
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				rs_S = rs_N = rs_W = rs_E = rs_SW = rs_NW = rs_SE = rs_NE = false;
				Height = frame.getHeight();
				Width = frame.getWidth();
				OpachePanel.bgImg = OpachePanel.backgroundImg.getScaledInstance(OpacheTest.Width, OpacheTest.Height, BufferedImage.SCALE_SMOOTH);
				panel.repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				height_increament = e.getYOnScreen();
				width_increament = e.getXOnScreen();
				// TODO Auto-generated method stub
				if (Math.abs(e.getYOnScreen() - frame.getLocation().getY()) < 5 && Math.abs(e.getXOnScreen() - frame.getLocation().getX()) < 5) {
					frame.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
					rs_NW = true;
				}
				else if (Math.abs(e.getYOnScreen() - frame.getLocation().getY()) < 5 && Math.abs(e.getXOnScreen() - frame.getLocation().getX() - frame.getWidth()) < 5) {
					frame.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
					rs_NE = true;
				}
				else if (Math.abs(e.getYOnScreen() - frame.getLocation().getY() - frame.getHeight()) < 5 && Math.abs(e.getXOnScreen() - frame.getLocation().getX()) < 5) {
					frame.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
					rs_SW = true;
				}
				else if (Math.abs(e.getYOnScreen() - frame.getLocation().getY() - frame.getHeight()) < 5 && Math.abs(e.getXOnScreen() - frame.getLocation().getX() - frame.getWidth()) < 5) {
					frame.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
					rs_SE = true;
				}
				else if (Math.abs(e.getXOnScreen() - frame.getLocation().getX()) < 5) {
					frame.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
					rs_W = true;
				}
				else if (Math.abs(e.getXOnScreen() - frame.getLocation().getX() - frame.getWidth()) < 5) {
					frame.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
					rs_E = true;
				}
				else if (Math.abs(e.getYOnScreen() - frame.getLocation().getY() - frame.getHeight()) < 5) {
					frame.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
					rs_S = true;
				}
				else if (Math.abs(e.getYOnScreen() - frame.getLocation().getY()) < 5) {
					frame.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
					rs_N = true;
				}
				else
				{
				isDrag = true;
				}
				hor = e.getXOnScreen() - x;
				ver = e.getYOnScreen() - y;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		frame.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				if (isDrag) {
					frame.setLocation(e.getXOnScreen()- hor, e.getYOnScreen() - ver);
					x = e.getX() - hor;
					y = e.getY() - ver;
				}
				
				if (rs_N) {
					frame.setLocation((int) frame.getLocation().getX(), e.getYOnScreen() - ver);
					frame.setSize(Width, Height - e.getYOnScreen() + height_increament);				
					y = e.getYOnScreen() - ver;
				}
				else if (rs_W) {
					frame.setLocation(e.getXOnScreen() - hor, (int) frame.getLocation().getY());
					frame.setSize(Width - e.getXOnScreen() + width_increament, Height);	
					x = e.getXOnScreen() - hor;
				}
				else if (rs_S) {
					frame.setLocation((int) frame.getLocation().getX(), (int) frame.getLocation().getY());
					frame.setSize(Width, Height + e.getYOnScreen() - height_increament);				
				}
				else if (rs_E) {
					frame.setLocation((int) frame.getLocation().getX(), (int) frame.getLocation().getY());
					frame.setSize(Width + e.getXOnScreen() - width_increament, Height);	
				}
				else if (rs_NW) {
					frame.setLocation(e.getXOnScreen() - hor, e.getYOnScreen() - ver);
					frame.setSize(Width - e.getXOnScreen() + width_increament, Height - e.getYOnScreen() + height_increament);				
					y = e.getYOnScreen() - ver;
					x = e.getXOnScreen() - hor;
				}
				else if (rs_SW) {
					frame.setLocation(e.getXOnScreen() - hor, (int) frame.getLocation().getY());
					frame.setSize(Width - e.getXOnScreen() + width_increament, Height + e.getYOnScreen() - height_increament);				
					x = e.getXOnScreen() - hor;
				}
				else if (rs_NE) {
					frame.setLocation((int) frame.getLocation().getX(), e.getYOnScreen() - ver);
					frame.setSize(Width + e.getXOnScreen() - width_increament, Height - e.getYOnScreen() + height_increament);				
					y = e.getYOnScreen() - ver;
				}
				else if (rs_SE) {
					frame.setLocation((int) frame.getLocation().getX(), (int) frame.getLocation().getY());
					frame.setSize(Width + e.getXOnScreen() - width_increament, Height + e.getYOnScreen() - height_increament);				
				}
				
			}
		});
		
	}
	
	
}
