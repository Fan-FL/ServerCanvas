package Server.UI;

import Server.shape.*;
import Server.shape.Shape;
import Server.util.JsonMessageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
    The DrawArea class is about the painting action of the mouse
 */
public class DrawArea extends JPanel {
    public enum ShapeType {
        PENCIL, LINE, RECT, FILLRECT, OVAL, FILLOVAL,
        CIRCLE, FILLCIRCLE, ROUNDRECT, FILLROUNDRECT, RUBBER, WORD;
    }

    private WhiteBoardWindow whiteboard = null;
    public java.util.List<Shape> shapeList = new java.util.ArrayList<Shape>(); // drawing graphs

    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    private ShapeType currentShapeType = ShapeType.PENCIL; // Set default pen as Pencil
    private Shape currentShape = null;
    private Shape lastShape = null;
    private Color color = Color.black; // current color of the pen
    private int R, G, B;

    private int f1, f2;
    private String stytle; // Current character style
    private float stroke = 1.0f; // set brush size and initialize it as 1.0

    public DrawArea(WhiteBoardWindow whiteBoardServer) {
        whiteboard = whiteBoardServer;
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        // Set the cursor as a crossing
        setBackground(Color.white); // Set the background as white
        addMouseListener(new MyMouseAdapter()); // Add mouse activity
        addMouseMotionListener(new MyMouseMotionAdapter());
        createNewShape();

    }

    // Get and set functions
    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroke(float f) {
        stroke = f;
    }

    public String getStytle() {
        return stytle;
    }

    public void setStytle(String stytle) {
        this.stytle = stytle;
    }

    public void clearCanvas() {
        reentrantReadWriteLock.writeLock().lock();
        this.shapeList.clear();
        this.currentShape = null;
        this.lastShape = null;
        reentrantReadWriteLock.writeLock().unlock();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        reentrantReadWriteLock.readLock().lock();
        if (!shapeList.isEmpty()) {
            for (Shape shape : shapeList) {
                draw(g2d, shape);
            }
        }
        reentrantReadWriteLock.readLock().unlock();
        if(currentShape!=null){
            draw(g2d, currentShape);
        }
    }

    /*
        // Pass the pen object to sub-classes
     */
    private void draw(Graphics2D g2d, Shape i) {
        i.draw(g2d);
    }

    public void addShape(Shape shape, String from){
        reentrantReadWriteLock.writeLock().lock();
        if(from.equals("local")){
            currentShape = null;
            String addShapeMsg = JsonMessageUtil.assembleAddShapeobjectData(shape);
            this.shapeList.add(shape);
            this.whiteboard.getController().sendToClients(addShapeMsg);
        }else if(from.equals("client")){
            this.shapeList.add(shape);
        }
        reentrantReadWriteLock.writeLock().unlock();
        repaint();
        System.out.println(shapeList.size());
    }

    private void createNewShape() {
        if (currentShapeType == ShapeType.WORD) {
            setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
        Shape shape = null;
        switch (currentShapeType) {
            case PENCIL:
                shape = new Pencil();
                shape.classType = "Pencil";
                break;
            case LINE:
                shape = new Line();
                shape.classType = "Line";
                break;
            case RECT:
                shape = new Rect();
                shape.classType = "Rect";
                break;
            case FILLRECT:
                shape = new FillRect();
                shape.classType = "FillRect";
                break;
            case OVAL:
                shape = new Oval();
                shape.classType = "Oval";
                break;
            case FILLOVAL:
                shape = new FillOval();
                shape.classType = "FillOval";
                break;
            case CIRCLE:
                shape = new Circle();
                shape.classType = "Circle";
                break;
            case FILLCIRCLE:
                shape = new FillCircle();
                shape.classType = "FillCircle";
                break;
            case ROUNDRECT:
                shape = new RoundRect();
                shape.classType = "RoundRect";
                break;
            case FILLROUNDRECT:
                shape = new FillRoundRect();
                shape.classType = "FillRoundRect";
                break;
            case RUBBER:
                shape = new Rubber();
                shape.classType = "Rubber";
                break;
            case WORD:
                shape = new Word();
                shape.classType = "Word";
                break;
        }
        if (shape != null) {
            shape.type = currentShapeType;
            shape.R = R;
            shape.G = G;
            shape.B = B;
            shape.stroke = stroke;
            this.lastShape = this.currentShape;
            this.currentShape = shape;
        }
    }


    /*
        Choose current color
     */
    public void chooseColor() {
        color = JColorChooser.showDialog(whiteboard, "Please choose color", color);
        try {
            R = color.getRed();
            G = color.getGreen();
            B = color.getBlue();
        } catch (Exception e) {
            R = 0;
            G = 0;
            B = 0;
        }
    }

    /*
     The size of the pen
     */
    public void setStroke() {
        String input;
        input = JOptionPane.showInputDialog("Please enter size of brush( >0 )");
        try {
            stroke = Float.parseFloat(input);

        } catch (Exception e) {
            stroke = 1.0f;

        }

    }

    /*
        Text input
     */
    public void setCurrentShapeType(ShapeType shapeType) {
        currentShapeType = shapeType;
    }

    /*
        Font of the characters
     */
    public void setFont(int i, int font) {
        if (i == 1) {
            f1 = font;
        } else
            f2 = font;
    }

    /*
        The corresponding responses for the mouse actions
     */
    class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent me) {
            // TODO mouse enter
            whiteboard.setStratBar("Mouse enters in:[" + me.getX() + " ,"
                    + me.getY() + "]");
        }

        @Override
        public void mouseExited(MouseEvent me) {
            // TODO mouse exits
            whiteboard.setStratBar("Mouse exits from:[" + me.getX() + " ,"
                    + me.getY() + "]");
        }

        @Override
        public void mousePressed(MouseEvent me) {
            // Create new graph object
            createNewShape();
            whiteboard.setStratBar("Mouse clicked at:[" + me.getX() + " ,"
                    + me.getY() + "]");

            switch (currentShapeType) {
                case RUBBER:
                    currentShape.dotsX.add(me.getX());
                    currentShape.dotxY.add(me.getY());
                    break;
                case LINE:
                case RECT:
                case FILLRECT:
                case OVAL:
                case FILLOVAL:
                case CIRCLE:
                case FILLCIRCLE:
                case ROUNDRECT:
                case FILLROUNDRECT:
                    currentShape.x1 = currentShape.x2 = currentShape.ix = me.getX();
                    currentShape.y1 = currentShape.y2 = currentShape.iy = me.getY();
                    break;
                case WORD:
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            whiteboard.setStratBar("Mouse loosen at:[" + me.getX() + " ,"
                    + me.getY() + "]");

            switch (currentShapeType) {
                case PENCIL:
                case RUBBER:
                    currentShape.dotsX.add(me.getX());
                    currentShape.dotxY.add(me.getY());
                    break;
                case LINE:
                    currentShape.x2 = me.getX();
                    currentShape.y2 = me.getY();
                    break;
                case RECT:
                case FILLRECT:
                case OVAL:
                case FILLOVAL:
                case CIRCLE:
                case FILLCIRCLE:
                case ROUNDRECT:
                case FILLROUNDRECT:
                    if (me.getY() >= currentShape.iy) {
                        if(me.getX() > currentShape.ix){
                            currentShape.x1 = me.getX();
                            currentShape.y1 = me.getY();
                            currentShape.x2 = currentShape.ix;
                            currentShape.y2 = currentShape.iy;
                        }else{
                            currentShape.x1 = currentShape.ix;
                            currentShape.y1 = me.getY();
                            currentShape.x2 = me.getX();
                            currentShape.y2 = currentShape.iy;
                        }
                    } else {
                        if(me.getX() > currentShape.ix){
                            currentShape.x1 = me.getX();
                            currentShape.y1 = currentShape.iy;
                            currentShape.x2 = currentShape.ix;
                            currentShape.y2 = me.getY();
                        }else{
                            currentShape.x1 = currentShape.ix;
                            currentShape.y1 = currentShape.iy;
                            currentShape.x2 = me.getX();
                            currentShape.y2 = me.getY();
                        }
                    }
                    break;
                case WORD:
                    currentShape.x1 = me.getX();
                    currentShape.y1 = me.getY();
                    String input;
                    input = JOptionPane.showInputDialog("Please enter your input:");
                    currentShape.s1 = input;
                    currentShape.x2 = f1;
                    currentShape.y2 = f2;
                    currentShape.s2 = stytle;
                    currentShapeType = ShapeType.WORD;
                    break;
            }
            addShape(currentShape, "local");
        }
    }

    /*
        The corresponding reaction for scroll and drag of the mouse
     */
    class MyMouseMotionAdapter extends MouseMotionAdapter {
        // Drag mouse operation
        public void mouseDragged(MouseEvent me){
            whiteboard.setStratBar("Mouse dragged at:[" + me.getX() + " ,"
                    + me.getY() + "]");
            switch (currentShapeType) {
                case PENCIL:
                case RUBBER:
                    currentShape.dotsX.add(me.getX());
                    currentShape.dotxY.add(me.getY());
                    break;
                case LINE:
                    currentShape.x2 = me.getX();
                    currentShape.y2 = me.getY();
                    break;
                case RECT:
                case FILLRECT:
                case OVAL:
                case FILLOVAL:
                case CIRCLE:
                case FILLCIRCLE:
                case ROUNDRECT:
                case FILLROUNDRECT: {
                    if (me.getY() >= currentShape.iy) {
                        if (me.getX() > currentShape.ix) {
                            currentShape.x1 = me.getX();
                            currentShape.y1 = me.getY();
                            currentShape.x2 = currentShape.ix;
                            currentShape.y2 = currentShape.iy;
                        } else {
                            currentShape.x1 = currentShape.ix;
                            currentShape.y1 = me.getY();
                            currentShape.x2 = me.getX();
                            currentShape.y2 = currentShape.iy;
                        }
                    } else {
                        if (me.getX() > currentShape.ix) {
                            currentShape.x1 = me.getX();
                            currentShape.y1 = currentShape.iy;
                            currentShape.x2 = currentShape.ix;
                            currentShape.y2 = me.getY();
                        } else {
                            currentShape.x1 = currentShape.ix;
                            currentShape.y1 = currentShape.iy;
                            currentShape.x2 = me.getX();
                            currentShape.y2 = me.getY();
                        }
                    }
                    break;
                }
                case WORD:
                    break;
            }
            repaint();
        }

        /*
         Mouse movement operation
         */
        @Override
        public void mouseMoved(MouseEvent me) {
            whiteboard.setStratBar("Mouse moves at:[" + me.getX() + " ,"
                    + me.getY() + "]");
        }
    }
}
