package main;

import javax.swing.UIManager;

import model.HorizontalLine;
import model.Model;
import model.VerticalLine;
import physics.Circle;
import view.RunGui;

/**
 * @author Murray Wood Demonstration of MVC and MIT Physics Collisions 2014
 */

public class Main {

	public static void main(String[] args) {
		try {
			// Use the platform look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Look and Feel error in Main");
		}

		Model model = new Model();

		//model.setBallSpeed(200, 200);

		constructCircularBumpers(model);
        constructSquareBumpers(model);

		RunGui gui = new RunGui(model);
		gui.createAndShowGUI();
	}

	public static void constructSquareBumpers(Model model) {
        int numElements = 5;
        int side = 20;

        for(int i = 0; i < numElements; i++) {
            // Calculate left upper corner position
            int x = (int) (Math.random() * 300 + 100);
            int y = (int) (Math.random() * 300 + 100);

            // Add circles at the corners
            model.addCircle(new Circle(x, y, 0)); // top left corner
            model.addCircle(new Circle(x + side, y, 0)); // top right corner
            model.addCircle(new Circle(x,y + side, 0)); // bottom left corner
            model.addCircle(new Circle(x + side, y + side, 0)); // bottom right corner

            // Add vertical lines
            model.addVerticalLine(new VerticalLine(x, y, side)); // connect top corners
            model.addVerticalLine(new VerticalLine(x, y + side, side)); // connect bottom corners

            // Add horizontal lines
            model.addHorizontalLine(new HorizontalLine(x, y, side)); // connect left corners
            model.addHorizontalLine(new HorizontalLine(x + side, y, side)); // connect right corners
        }
	}

	public static void constructCircularBumpers(Model model) {
        int numElements = 5;
	    for(int i = 0; i < numElements; i++) {
            model.addCircle(new Circle((int) (Math.random() * 300 + 100), (int) (Math.random() * 300 + 100), 10));
        }
	}
}