package Server.shape;

import java.awt.*;

public class Line extends Shape {
    // Draw straight line
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setPaint(new Color(R, G, B)); // Set the color of the pen

        g2d.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL));
        g2d.drawLine(x1, y1, x2, y2); // Draw a straight line

    }
}
