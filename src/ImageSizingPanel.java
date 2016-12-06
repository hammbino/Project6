import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Project6
 * Created by jeffreyhammond on 12/5/16.
 */
public class ImageSizingPanel extends JPanel {
    private double zoom = 1;
    ImageSizingPanel () {
        setLayout(new FlowLayout());

        JButton zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(e -> {
            zoom += .25;
            if (zoom < 0)
                zoom = .5;
            getParent().repaint();

        });

        JButton defaultSizeButton = new JButton("100%");
        defaultSizeButton.addActionListener(e -> {
            zoom = 1;
            getParent().repaint();
        });

        JButton zoomOutButton = new JButton("Zoom Out");
        zoomOutButton.addActionListener(e -> {
            zoom -= .25;
            if (zoom > 0) {
                repaint();
            } else {
                System.out.println("Maximum zoom out reached"); //TODO add dialogue box
            }
        });

        add(zoomInButton);
        add(defaultSizeButton);
        add(zoomOutButton);
    }
//
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        if ( != null) {
//            int w = image.getWidth();
//            int h = image.getHeight();
//            g.drawImage(image, 0, 0, (int) (w * zoom), (int) (h * zoom), null);
//        }
//    }
}
