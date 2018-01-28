package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import model.Absorber;
import model.Ball;
import model.Model;

/**
 * @author Murray Wood Demonstration of MVC and MIT Physics Collisions 2014
 */

public class RunListener implements ActionListener, KeyListener {

	private Timer timer;
	private Model model;

	public RunListener(Model m) {
		model = m;
		timer = new Timer(50, this);
	}

	@Override
	public final void actionPerformed(final ActionEvent e) {

		if (e.getSource() == timer) {
			model.moveBall();
		} else
			switch (e.getActionCommand()) {
			case "Start":
				timer.start();
				break;
			case "Stop":
				timer.stop();
				break;
			case "Tick":
				model.moveBall();
				break;
			case "Quit":
				System.exit(0);
				break;
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("KEY PRESSED");
		Ball ball = model.getBall();
		Absorber absorber = model.getAbsorber();
		double radius = ball.getRadius();

		timer.start();
		// Make sure the ball is on the absorber
		if(absorber.getPosition() - ball.getRadius() == ball.getExactY()) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				model.shootBall(0, 600);
				return;
			}

			// Make sure the ball doesn't go over edges
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if((ball.getExactX() + radius) < 500) {
					ball.setExactX(ball.getExactX() + radius);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if((ball.getExactX() - radius) > 0) {
					ball.setExactX(ball.getExactX() - radius);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}

