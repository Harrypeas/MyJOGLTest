import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class OpachePanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8882724561739425634L;
	private boolean isOpaque = true;
	public static Image bgImg = null;
	public static Image backgroundImg = null;
	public OpachePanel() throws IOException {
		// TODO Auto-generated constructor stub
		JLabel label = new JLabel();
		Image image = ImageIO.read(new File("off.png"));
		Image image2 = image.getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH);
		Image image3 = ImageIO.read(new File("on.png"));
		Image image4 = image3.getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH);
		label.setIcon(new ImageIcon(image2));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc_test = new GridBagConstraints();
		gbc_test.gridx = 0;
		gbc_test.gridy = 0;
		gbc_test.insets = new Insets(10, 10, 10, 10);
		gbc_test.anchor = GridBagConstraints.NORTHWEST;
		// gbc_test.
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("clicked");
				// TODO Auto-generated method stub
				if (isOpaque == true) {
					OpacheTest.frame.setOpacity(1.f);
					super.mouseClicked(e);
					isOpaque = false;
				}
				else if (isOpaque == false) {
					OpacheTest.frame.setOpacity(0.6f);
					super.mouseClicked(e);
					isOpaque = true;
				}
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setIcon(new ImageIcon(image4));
				super.mouseEntered(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setIcon(new ImageIcon(image2));
				super.mouseExited(e);
			}
		});
		 
		add(label,gbc_test);
		
		gbc_test.gridx = 0;
		gbc_test.gridy = 1;
		gbc_test.weightx = 1;
		gbc_test.weighty = 1;
		
		gbc_test.fill = GridBagConstraints.BOTH;
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
//		panel.setSize(new Dimension(1000, 700));
		panel.setBorder(new LineBorder(Color.WHITE, 3, true));
		add(panel, gbc_test);
		
		DefaultListModel<String> model = new DefaultListModel<>();
		model.addElement("hpeas");
		model.addElement("peas");
		JList<String> list = new JList<>(model);
		list.setOpaque(false);
		list.setForeground(new Color(20, 70, 60));
		list.setBackground(new Color(0, 0, 0, 0));
		list.setSelectionForeground(new Color(80, 238, 231));
		list.setSelectionBackground(new Color(0, 0, 0, 0));
		list.setBorder(new LineBorder(Color.BLUE, 2, true));
		list.setFont(new Font("Palatino", Font.BOLD, 25));
		panel.add(list, BorderLayout.CENTER);
		
		backgroundImg = ImageIO.read(new File("galaxy.png"));
		bgImg = backgroundImg.getScaledInstance(OpacheTest.Width, OpacheTest.Height, BufferedImage.SCALE_SMOOTH);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.setColor(new Color(60, 220, 220, 100));
		
		setOpaque(false);
		//			System.out.println("ing");
		g.drawImage(bgImg, 0, 0, null);
//		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
}
