import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import static javax.swing.JOptionPane.showInputDialog;

public class WhiteboardPanel extends JPanel implements MouseListener, MouseMotionListener{
    private Paint paint;
    private Point startPoint, endPoint;
    private static final String CIRCLE = "Circle";
    private static final String OVAL = "Oval";
    private static final String RECTANGLE = "Rectangle";
    private static final String TEXT = "Text";

    public WhiteboardPanel(Paint paint) {
        this.paint = paint;
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        try{
            graphics.drawImage(paint.getRemoteCanvas().getImage().getImage(), 0, 0, getWidth(), getHeight(), null);
            this.repaint();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Canvas panel null pointer");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endPoint = e.getPoint();
        String shapeType = paint.getShapeType();
        switch (shapeType) {
            case CIRCLE -> {
                paint.drawCircle(startPoint, endPoint);
                break;
            }
            case OVAL -> {
                paint.drawOval(startPoint, endPoint);
                break;
            }
            case RECTANGLE -> {
                paint.drawRectangle(startPoint, endPoint);
                break;
            }
            case TEXT -> {
                String text = showInputDialog("Input text:");
                if(text != null && !text.isEmpty()){
                    paint.drawText(text, startPoint);
                } else {
                    System.out.println("Cancel input or no input!");
                }
                break;
            }
            default -> paint.drawLine(startPoint, endPoint);
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
