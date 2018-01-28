package model;

import java.util.ArrayList;
import java.util.Observable;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

/**
 * @author Murray Wood Demonstration of MVC and MIT Physics Collisions 2014
 */

public class Model extends Observable {

	private ArrayList<VerticalLine> verticalLines;
	private ArrayList<HorizontalLine> horizontalLines;
	private ArrayList<Circle> circles;
	private Absorber absorber;
	private Ball ball;
	private Walls gws;
	private int L;

	public Model() {

		// Ball position (25, 25) in pixels. Ball velocity (100, 100) pixels per tick
		//ball = new Ball(25, 25, 100, 100);
        ball = new Ball(25, 25, 0, 0);

		// Wall size 500 x 500 pixels
		gws = new Walls(0, 0, 500, 500);

		L = 25;

		// Lines and circles
		verticalLines = new ArrayList<>();
		horizontalLines = new ArrayList<>();
		circles = new ArrayList<>();

		//addAbsorber();
		//setBallOnAbsorber();
		setBallForFreeFall();
	}

    private void addAbsorber() {
        VerticalLine vl = new VerticalLine(0, 470, 500);
        verticalLines.add(vl);
        absorber = new Absorber(vl);
    }

    private void setBallForFreeFall() {
        ball.setExactX(25);
        ball.setExactX(25);
    }

    private void setBallOnAbsorber() {
        // Place the ball on the absorber
        ball.setExactX(20);
        ball.setExactY(absorber.getPosition() - ball.getRadius());
    }

    public void moveBall() {

		double moveTime = 0.05; // 0.05 = 20 times per second as per Gizmoball
        double tickTime = moveTime;

		if (ball != null && !ball.stopped()) {

			CollisionDetails cd = timeUntilCollision();
			double tuc = cd.getTuc();
			if (tuc > moveTime) {
				// No collision ...
				ball = movelBallForTime(ball, moveTime);
			} else {
				//System.out.println("The time until collision is: " + String.format("%.3f", tuc) + "ms");
				// We've got a collision in tuc
				ball = movelBallForTime(ball, tuc);
				// Post collision velocity ...
				ball.setVelo(cd.getVelo());
				tickTime = tuc;
			}

			applyForces(tickTime);

			// Notify observers ... redraw updated view
			this.setChanged();
			this.notifyObservers();
		}

	}

	private Ball movelBallForTime(Ball ball, double time) {

		double newX = 0.0;
		double newY = 0.0;
		double xVel = ball.getVelo().x();
		double yVel = ball.getVelo().y();
		newX = ball.getExactX() + (xVel * time);
		newY = ball.getExactY() + (yVel * time);
		/*
		System.out.println("The current ball position is x: "
				+ String.format("%.3f", ball.getExactX()) + " y: " + String.format("%.3f", ball.getExactY()) + "; "
				+ " the updated ball position is x: " + String.format("%.3f", newX)
				+ " y: " + String.format("%.3f", newY) + " the time is "
				+ String.format("%.3f", time) + "ms");
		*/
		//System.out.println("The velocity in x direction is " + String.format("%.3f", xVel) + " in y direction is " + String.format("%.3f", yVel));
		ball.setExactX(newX);
		ball.setExactY(newY);
		return ball;
	}

	private CollisionDetails timeUntilCollision() {
		// Find Time Until Collision and also, if there is a collision, the new speed vector.
		// Create a physics.Circle from Ball
		Circle ballCircle = ball.getCircle();
		Vect ballVelocity = ball.getVelo();
		Vect newVelo = new Vect(0, 0);

		// Now find shortest time to hit a vertical line or a wall line
		double shortestTime = Double.MAX_VALUE;
		double time = 0.0;

		// Time to collide with 4 walls
		ArrayList<LineSegment> lss = gws.getLineSegments();
		for (LineSegment line : lss) {
			time = Geometry.timeUntilWallCollision(line, ballCircle, ballVelocity);
			if (time < shortestTime) {
				shortestTime = time;
				newVelo = Geometry.reflectWall(line, ball.getVelo(), 1.0);
			}
		}

        // Time to collide with the absorber
        if(absorber != null) {
            LineSegment abs = absorber.getLineSeg();
            time = Geometry.timeUntilWallCollision(abs, ballCircle, ballVelocity);
            if (time < shortestTime) { // collison with absorber happens, transfer the ball
                shortestTime = time;
                newVelo = Geometry.reflectWall(abs, ball.getVelo(), 0.0);
            }
        }


		// Time to collide with any vertical lines
		for (VerticalLine line : verticalLines) {
			LineSegment ls = line.getLineSeg();
			time = Geometry.timeUntilWallCollision(ls, ballCircle, ballVelocity);
			if (time < shortestTime) {
				shortestTime = time;
				newVelo = Geometry.reflectWall(ls, ball.getVelo(), 1.0);
			}
		}


		//Time to collide with any horizontal lines
		for (HorizontalLine line : horizontalLines) {
			LineSegment ls = line.getLineSeg();
			time = Geometry.timeUntilWallCollision(ls, ballCircle, ballVelocity);
			if (time < shortestTime) {
				shortestTime = time;
				newVelo = Geometry.reflectWall(ls, ball.getVelo(), 1.0);
			}
		}

		// Time to collide with circles
		for (Circle circle : circles) {
			time = Geometry.timeUntilCircleCollision(circle, ballCircle, ballVelocity);
			if (time < shortestTime) {
				shortestTime = time;
				newVelo = Geometry.reflectCircle(circle.getCenter(), ball.getCircle().getCenter(), ball.getVelo());
			}
		}

		return new CollisionDetails(shortestTime, newVelo);
	}

	public void applyForces(double deltaT) {
        ball.setVelo(new Vect(ball.getVelo().x(),ball.getVelo().y() + applyGravity(deltaT)));
        applyFriction(deltaT);
    }

	public double applyGravity(double deltaT) {
	    return 25.0*deltaT*L;
    }

    public void applyFriction(double deltaT) {
	    double mu1 = 0.025; // per second
	    double mu2 = 0.025; // per L
        double oldVelocity = ball.getVelo().y();

	    double newVelocity = oldVelocity * (1 - (mu1 * deltaT) - (mu2 * Math.abs(oldVelocity) * deltaT));
        System.out.println("New velocity " + newVelocity);
        System.out.println("Old velocity " + oldVelocity);

        ball.setVelo(new Vect(ball.getVelo().x(), newVelocity));
    }

    public void shootBall(int impulseX, int impulseY) {
        System.out.println(-impulseY);
	    setBallSpeed(impulseX, -impulseY);
    }


	public Ball getBall() {
		return ball;
	}

	public ArrayList<VerticalLine> getVerticalLines() {
		return verticalLines;
	}

	public ArrayList<HorizontalLine> getHorizontalLines() {
		return horizontalLines;
	}

	public void addVerticalLine(VerticalLine l) {
		verticalLines.add(l);
	}

	public void addHorizontalLine(HorizontalLine l) {
		horizontalLines.add(l);
	}

	public void setBallSpeed(int x, int y) {
		ball.setVelo(new Vect(x, y));
	}

	public void addCircle(Circle c) {
		circles.add(c);
	}

	public ArrayList<Circle> getCircles() {
		return circles;
	}

	public Absorber getAbsorber() {
	    return this.absorber;
    }
}