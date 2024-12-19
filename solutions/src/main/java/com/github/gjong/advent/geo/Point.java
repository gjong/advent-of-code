package com.github.gjong.advent.geo;

import java.util.HashSet;
import java.util.Set;

public record Point(int x, int y) implements Direction {

    public Point translate(Direction translation) {
        return Point.of(x + translation.x(), y + translation.y());
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

    public Point subtract(Direction other) {
        return new Point(x - other.x(), y - other.y());
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

    public Set<Point> cornerNeighbours() {
        return Set.of(
                this.translate(1, 1),
                this.translate(-1, -1),
                this.translate(-1, 1),
                this.translate(1, -1));
    }

    /**
     * Generate a list of all directly attached points.
     * Similar to {@link #neighbours()}, but includes the vertical neighbours as well.
     *
     * @return
     */
    public Set<Point> allNeighbours() {
        var neighbours = new HashSet<>(neighbours());
        neighbours.addAll(cornerNeighbours());
        return neighbours;
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

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    public static Point of(String xAndY) {
        var parts = xAndY.split(",");
        return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    // These points are memory optimizations
    public static Point zero = new  Point(0, 0);
    public static Point east = new  Point(1, 0);
    public static Point west = new  Point(-1, 0);
    public static Point north = new  Point(0, -1);
    public static Point south = new  Point(0, 1);

    public static Point directionFromChar(char c) {
        if (c == 'U' || c == 'N') { // Up, North
            return north;
        } else if (c == 'D' || c == 'S') {// Down, South
            return south;
        } else if (c == 'R' || c == 'E') { // Right, East
            return east;
        }
        return west;
    }
}
