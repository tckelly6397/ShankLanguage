import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class DrawingPanel extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Shank.color);

        for(Ellipse2D ellipse : Shank.circles) {
            g.drawOval((int)ellipse.getX(), (int)ellipse.getY(), (int)ellipse.getWidth(), (int)ellipse.getHeight());
        }

        for(Rectangle2D rectangle : Shank.rectangles) {
            g.drawOval((int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight());
            System.out.println(rectangle.getX() + " " + rectangle.getHeight());
        }
    }
}
