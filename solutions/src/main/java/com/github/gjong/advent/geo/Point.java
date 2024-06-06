package com.github.gjong.advent.geo;

import java.util.Set;

public record Point(int x, int y) {

    public Point translate(Point translation) {
        return Point.of(x + translation.x, y + translation.y);
    }

    public Point translate(int x, int y) {
        return Point.of(this.x + x, this.y + y);
    }

    public Point rotateCW() {
        return new Point(-y, x);
    }

    public Point rotateCCW() {
        return new Point(y, -x);
    }

    public boolean touches(Point other) {
        return (x >= (other.x - 1) && x <= (other.x + 1))
                && (y >= (other.y - 1) && y <= (other.y + 1));
    }

    public static Point zero = new Point(0, 0);

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    public boolean isLeft() {
        return this.x < 0;
    }

    public boolean isHorizontal() {
        return isLeft() || isRight();
    }

    public boolean isRight() {
        return this.x > 0;
    }

    public boolean isUp() {
        return this.y < 0;
    }

    public boolean isDown() {
        return this.y > 0;
    }

    /**
     * Build the list of neighbours, this includes all points except the diagonal points that touch
     * this point.
     *
     * @return
     */
    public Set<Point> neighbours() {
        return Set.of(
                this.translate(0, 1),
                this.translate(0, - 1),
                this.translate(-1, 0),
                this.translate(1, 0));
    }

    public Point left() {
        return this.translate(-1, 0);
    }

    public Point right() {
        return this.translate(1, 0);
    }

    public Point up() {
        return this.translate(0, - 1);
    }

    public Point down() {
        return this.translate(0, 1);
    }

    /**
     * Return the inverse of this point. This is the point with the same magnitude but opposite
     * direction.
     */
    public Point inverse() {
        return new Point(-x, -y);
    }

    public static Point directionFromChar(char c) {
        if (c == 'U' || c == 'N') { // Up, North
            return zero.up();
        } else if (c == 'D' || c == 'S') {// Down, South
            return zero.down();
        } else if (c == 'R' || c == 'E') { // Right, East
            return zero.right();
        }
        return zero.left();
    }
}
