import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Driver {
	public static void main(String[] args) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int panelWidth = screen.width;
		int panelHeight = screen.height;
		
		Display loader = new Display(panelWidth/2, panelHeight/2, Color.black, Color.green);
		JFrame frame1 = new JFrame();
		frame1.setTitle("Ipconfig Impersonator");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.add(loader);
		frame1.pack();
		frame1.setLocation(550, 100);
		frame1.setVisible(true);
	}
}
