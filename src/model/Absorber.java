package model;

import physics.LineSegment;

public class Absorber {
    private VerticalLine vl;
    private int position;

    public Absorber(VerticalLine vl) {
        this.vl = vl;
        this.position = vl.getY();
    }

    public LineSegment getLineSeg() {
        return this.vl.getLineSeg();
    }

    public int getPosition() {
        return this.position;
    }
}
