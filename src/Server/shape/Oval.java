package Server.shape;

import java.awt.*;

public class Oval extends Shape {
	// This function will draw a oval
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setPaint(new Color(R, G, B));
		g2d.setStroke(new BasicStroke(stroke));
		g2d.drawOval(Math.min(x1, x2), Math.min(y2, y2), Math.abs(x1 - x2),
				Math.abs(y1 - y2));
	}
}
