package model;

import physics.LineSegment;

public class HorizontalLine {

    private int xpos;
    private int ypos;
    private int height;
    private LineSegment ls;

    public HorizontalLine(int x, int y, int h) {
        xpos = x;
        ypos = y;
        height = h;
        ls = new LineSegment(x, y, x, y + h);
    }

    public LineSegment getLineSeg() {
        return ls;
    }

    public int getX() {
        return xpos;
    }

    public int getY() {
        return ypos;
    }

    public int getHeight() {
        return height;
    }
}