package enseirb.t3.e_health.graph;

import java.util.Date;

public class Point {
	private Date x;
	private double y;

	public Point(Date x, double y) {
		this.x = x;
		this.y = y;
	}

	public Date getX() {
		return x;
	}

	public void setX(Date x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
