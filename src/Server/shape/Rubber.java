package Server.shape;

import java.awt.*;
import java.util.ArrayList;

public class Rubber extends Shape {
	public Rubber() {
		dotsX = new ArrayList<>();
		dotxY = new ArrayList<>();
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setPaint(new Color(255, 255, 255));
		// Set the color of the pen white
		g2d.setStroke(new BasicStroke(stroke + 4, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_BEVEL));
		if(!dotsX.isEmpty()){
			for (int i=0; i<dotsX.size()-1; i++){
				g2d.drawLine(dotsX.get(i), dotxY.get(i), dotsX.get(i+1), dotxY.get(i+1));
			}
		}
	}
}
