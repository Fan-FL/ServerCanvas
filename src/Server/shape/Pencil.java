package Server.shape;

import java.awt.*;
import java.util.ArrayList;

public class Pencil extends Shape {
    public Pencil() {
        dotsX = new ArrayList<>();
        dotxY = new ArrayList<>();
    }

	// unlimited painting
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setPaint(new Color(R, G, B));
		g2d.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_BEVEL));
		if(!dotsX.isEmpty()){
            for (int i=0; i<dotsX.size()-1; i++){
                g2d.drawLine(dotsX.get(i), dotxY.get(i), dotsX.get(i+1), dotxY.get(i+1));
            }
        }
	}
}
