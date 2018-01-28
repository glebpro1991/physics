package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Ball;
import model.HorizontalLine;
import model.Model;
import model.VerticalLine;
import physics.Circle;

/**
 * @author Murray Wood Demonstration of MVC and MIT Physics Collisions 2014
 */

public  class Board extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	protected int width;
	protected int height;
	protected Model gm;

	public Board(int w, int h, Model m) {
		// Observe changes in Model
		m.addObserver(this);
		width = w;
		height = h;
		gm = m;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	// Fix onscreen size
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		// Draw all the vertical lines
		for (VerticalLine vl : gm.getVerticalLines()) {
			g2.fillRect(vl.getX(), vl.getY(), vl.getWidth(), 1);
		}

		// Draw all the horizontal lines
		for (HorizontalLine hl : gm.getHorizontalLines()) {
			g2.fillRect(hl.getX(), hl.getY(), 1, hl.getHeight());
		}

		// Draw circles
		for (Circle c : gm.getCircles()) {
			int x = (int) c.getCenter().x() - (int) c.getRadius();
			int y = (int) c.getCenter().y() - (int) c.getRadius();
			int width = (int) (2 * c.getRadius());
			g2.drawOval(x, y, width, width);
		}
		
		Ball b = gm.getBall();
		if (b != null) {
			g2.setColor(b.getColour());
			int x = (int) (b.getExactX() - b.getRadius());
			int y = (int) (b.getExactY() - b.getRadius());
			int width = (int) (2 * b.getRadius());
			g2.fillOval(x, y, width, width);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
			repaint();
		}
	
}
