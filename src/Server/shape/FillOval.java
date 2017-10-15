package Server.shape;

import java.awt.*;

public class FillOval extends Shape {
    // This function will draw a filled oval
    public void draw(Graphics2D g2d) {
        g2d.setPaint(new Color(R, G, B));
        g2d.setStroke(new BasicStroke(stroke));
        g2d.fillOval(Math.min(x1, x2), Math.min(y2, y2), Math.abs(x1 - x2),
                Math.abs(y1 - y2));
    }
}
