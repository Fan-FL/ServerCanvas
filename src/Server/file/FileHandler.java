package Server.file;

import Server.UI.DrawArea;
import Server.UI.WhiteBoardWindow;
import Server.shape.Shape;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class FileHandler {
    private WhiteBoardWindow whiteboard;
    DrawArea drawarea = null;

    public FileHandler(WhiteBoardWindow wb, DrawArea da) {
        whiteboard = wb;
        drawarea = da;
    }

    /*
        Create a new image
     */
    public void newFile() {
        drawarea.setCurrentShapeType(DrawArea.ShapeType.PENCIL);    //Set default pen as pencil
        drawarea.setColor(Color.black);    //Set default color
        drawarea.setStroke(1.0f);        //Set size of the pen
        drawarea.clearCanvas();
        drawarea.repaint();
    }

    /*
        Open a file
     */
    public void openFile() {
        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Canvas Images", "cvs");
        //Show .jpg and .gif image only
        filechooser.setFileFilter(filter);
        int returnVal = filechooser.showOpenDialog(whiteboard);

        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File fileName = filechooser.getSelectedFile();
        fileName.canRead();
        if (fileName == null || fileName.getName().equals("")) {
            //When file name does not exit
            JOptionPane.showMessageDialog(filechooser, "File name", "Please input filename!", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                drawarea.clearCanvas();
                FileInputStream ifs = new FileInputStream(fileName);
                ObjectInputStream input = new ObjectInputStream(ifs);

                int countNumber = 0;
                Shape inputRecord;
                countNumber = input.readInt();
                for (int i = 0; i < countNumber; i++) {
                    inputRecord = (Shape) input.readObject();
                    drawarea.shapeList.add(inputRecord);
                }
                input.close();
                drawarea.repaint();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(whiteboard, "File path error", "Source file not found!", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(whiteboard, "File read error", "Cannot read this file", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(whiteboard, "Create object error", "Already at the end of the file!", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    /*
        save image
     */
    public void saveFile() {
        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = filechooser.showSaveDialog(whiteboard);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        File fileName = filechooser.getSelectedFile();
        fileName.canWrite();
        if (fileName == null || fileName.getName().equals("")){
            //If the file name does not exit
            JOptionPane.showMessageDialog(filechooser, "File name", "Please input file name!", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                fileName.delete();
                FileOutputStream fos = new FileOutputStream(fileName + ".cvs");
                //Output file in the form of bytes
                ObjectOutputStream output = new ObjectOutputStream(fos);
                //Shape record;
                output.writeInt(drawarea.shapeList.size());

                for (int i = 0; i < drawarea.shapeList.size(); i++) {
                    Shape p = drawarea.shapeList.get(i);
                    output.writeObject(p);
                    output.flush();
                }
                output.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
