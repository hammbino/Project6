/**
 * Project6
 * Created by jeffreyhammond on 11/29/16.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

/**
 * A frame to view images
 * */
class ImageFrame extends JFrame {

    private BufferedImage image;
    private float zoom = 1;

    ImageFrame() {
        setSize(getPreferredSize());
        JPanel buttonPanel = new JPanel();
        JPanel picPanel = new ImagePanel();

        JButton openButton = new JButton("Open");

//Todo this needs some love and a refactor

        openButton.addActionListener(event -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
            chooser.addChoosableFileFilter(filter);
            int result = chooser.showOpenDialog(ImageFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                try {
                    image = ImageIO.read(new File(selectedFile.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Could not open file.");
                }
                repaint();
            } else if (result == JFileChooser.CANCEL_OPTION) {
                System.out.println("No File Selected");
            }
        });

        JButton zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(e -> {
            zoom += .25;
            repaint();

        });

        JButton defaultSizeButton = new JButton("100%");
        defaultSizeButton.addActionListener(e -> {
            zoom = 1;
            repaint();
        });

        JButton zoomOutButton = new JButton("Zoom Out");
        zoomOutButton.addActionListener(e -> {
            zoom -= .25;
            repaint();
        });

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(openButton);
        buttonPanel.add(zoomInButton);
        buttonPanel.add(defaultSizeButton);
        buttonPanel.add(zoomOutButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(picPanel);
    }

    /**
     * A component that displays an image
     */
    private class ImagePanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int w = image.getWidth();
                int h = image.getHeight();
                g.drawImage(image, 0, 0, (int) (w * zoom), (int) (h * zoom), null);
            }
        }
    }

    /**
     * Sets default dimensions of the JFrame
     */
    public Dimension getPreferredSize() {
        if (image == null) {
            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            return new Dimension(screenSize.width / 2, screenSize.height / 2);
        } else {
            return new Dimension(image.getWidth(), image.getHeight());
        }
    }
}