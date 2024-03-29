import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class Paint {
    private String shapeType;
    private Color shapeColor;
    private RemoteCanvas remoteCanvas;

    public final ActionListener shapeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            shapeType = e.getActionCommand();
        }
    };

    public Paint(){}
    public String getShapeType() {
        return shapeType;
    }

    public void setColor(Color color) {
        shapeColor = color;
    }

    public void drawText(String text, Point start) {
        try{
            remoteCanvas.makeText(text, start.x, start.y, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawLine(Point start, Point end) {
        try{
            remoteCanvas.makeLine(start.x, start.y, end.x, end.y, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawCircle(Point start, Point end) {
        int radius = (int) Math.sqrt(Math.pow(end.getX()- start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));

        try{
            remoteCanvas.makeCircle(start.x, start.y, radius, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawOval(Point start, Point end) {
        try{
            remoteCanvas.makeOval(start.x, start.y, end.x, end.y, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawRectangle(Point start, Point end) {
        try{
            remoteCanvas.makeRectangle(start.x, start.y, end.x, end.y, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setRemoteCanvas(RemoteCanvas remoteCanvas) {
        this.remoteCanvas = remoteCanvas;
    }

    public RemoteCanvas getRemoteCanvas() {
        return remoteCanvas;
    }

    public void setImage(BufferedImage image) {
        try{
            remoteCanvas.setImage(new SerializableImage(image));
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void saveImage(String fileName) {
        File file = new File(fileName);
        try {
           ImageIO.write(this.getRemoteCanvas().getImage().getImage(), "png", file);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearWhiteboard(){
        try {
            remoteCanvas.clear();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
