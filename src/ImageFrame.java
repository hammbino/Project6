/*
 * Project6
 * Created by Jeffrey Hammond on 11/29/16.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A frame to view images
 * */
class ImageFrame extends JFrame {

    //Variables declaration
    int position = 0; //Initial position is 0
    File [] files; //Array to hold the file names
    final Path path = Paths.get("."); //Directory path to your images
    private JButton NextButton;
    private JButton PreviousButton;
    private JLabel picLabel;
    private BufferedImage image;
    private float zoom = 1;
    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpeg", "*.jpg", "*.gif", "*.png");
    // End of variables declaration


    ImageFrame() {
        getJpegs(".");//TODO handle if FILES is empty
        try {
            image = ImageIO.read(new File(files[0].getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not open file.");
        }
//        setSize(getPreferredSize());
        add(new ButtonPanel(), BorderLayout.SOUTH);
        add(new ImagePanel());
        add(new ImageNavigationPanel(), BorderLayout.WEST);
//        setLabelIcon(path, files[position]); //sets the label to display the first
        //image in the directory on window opening.
//        PreviousButton.setEnabled(false);
    }

    private class ButtonPanel extends JPanel {
        ButtonPanel() {
            //Todo this needs some love and a refactor
            JButton openButton = new JButton("Open");
            openButton.addActionListener(event -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
                chooser.addChoosableFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);
                int result = chooser.showOpenDialog(ImageFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    try {
                        image = ImageIO.read(new File(selectedFile.getAbsolutePath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Could not open file.");
                    }
                    getJpegs(chooser.getCurrentDirectory().toString());
                    getParent().repaint();
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("No File Selected");
                }
            });

            JButton zoomInButton = new JButton("Zoom In");
            zoomInButton.addActionListener(e -> {
                zoom += .25;
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
                getParent().repaint();
            });

            JButton quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> System.exit(0));

            add(openButton);
            add(zoomInButton);
            add(defaultSizeButton);
            add(zoomOutButton);
            add(quitButton);

        }
    }

    private class ImageNavigationPanel extends JPanel {
        ImageNavigationPanel() {

            Action nextImageAction = new getNextImage("Next");
            Action previousImageAction = new getPreviousImage("Previous");

            add(new JButton(previousImageAction));
            add(new JButton(nextImageAction));

            InputMap imap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            imap.put(KeyStroke.getKeyStroke("RIGHT"), "Next");
            imap.put(KeyStroke.getKeyStroke("LEFT"), "Previous");

            ActionMap amap = getActionMap();
            amap.put("Next", nextImageAction);
            amap.put("Previous", previousImageAction);


//            JButton previousButton = new JButton("Previous");
//            previousButton.addActionListener(e -> {
//                zoom = 1;
//                position--;
//                if(position < 0 ) {
//                    position = files.length -1;
//                }
//                try {
//                    image = ImageIO.read(new File(files[position].getAbsolutePath()));
//                } catch (IOException evt) {
//                    evt.printStackTrace();
//                    System.out.println("Could not open file.");
//                }
//                getParent().repaint();
//              });
//            JButton nextButton = new JButton("Next");
//
//            nextButton.addActionListener(e -> getNextImage("Next"));

            JButton thumbnailButton = new JButton("View Thumbnails");


            add(thumbnailButton);

            thumbnailButton.setEnabled(false);

//            nextButton.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "getNext");
//            nextButton.getActionMap().put("getNext", nextButton.getAction());
        }

    }

    public class getNextImage extends AbstractAction {

        public getNextImage(String name) {
            putValue(Action.NAME, name);
            putValue(Action.SHORT_DESCRIPTION, "Click to see next image");
        }

        public void actionPerformed(ActionEvent event) {
            zoom = 1;
            position++;
            if(position == files.length) {
                position = 0;
            }
            try {
                image = ImageIO.read(new File(files[position].getAbsolutePath()));
            } catch (IOException evt) {
                evt.printStackTrace();
                System.out.println("Could not open file.");
            }
            getParent().repaint();
        }
    }

    public class getPreviousImage extends AbstractAction {

        public getPreviousImage(String name) {
            putValue(Action.NAME, name);
            putValue(Action.SHORT_DESCRIPTION, "Click to see next image");
        }

        public void actionPerformed(ActionEvent event) {
            zoom = 1;
            position--;
            if (position < 0) {
                position = files.length - 1;
            }
            try {
                image = ImageIO.read(new File(files[position].getAbsolutePath()));
            } catch (IOException evt) {
                evt.printStackTrace();
                System.out.println("Could not open file.");
            }
            getParent().repaint();

        }
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
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setLocation(screenSize.width/5, screenSize.height/10);

        if (image != null && image.getWidth() > screenSize.getWidth()/2 ) {
            return new Dimension(image.getWidth(), screenSize.height / 2);
        }

        return new Dimension(screenSize.width / 2, screenSize.height / 2);
    }


    private void getJpegs(String fileDir) {
        File dir = new File(fileDir);
        files = dir.listFiles((dir1, name) -> name.matches("\\w*(.jpeg|.jpg)"));

        if (files != null) {
            for (File jpeg : files) {
                System.out.println(jpeg);
            }
        }
    }

    //Method to set the picture on the label
    private void setLabelIcon(Path path, File name){
        File file = new File(path+"\\"+name);
        java.awt.image.BufferedImage image = null;

        try{
            image = ImageIO.read(file); //Reas the image from the file.
        }catch(IOException ie){
            javax.swing.JOptionPane.showMessageDialog(this,"Error reading image file","Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        ImageIcon icon = new ImageIcon(image);
        int width = 600;
        int height = 400;
        Image img = icon.getImage().getScaledInstance(width,height,java.awt.Image.SCALE_SMOOTH); //Images produced will remain a fixed size, 600 * 400

        ImageIcon newIcon = new ImageIcon(img); //Create a new imageicon from an image object.

        //Now we want to create a caption for the pictures using their file names
        String pictureName = file.getName();
        int pos = pictureName.lastIndexOf("."); 	   //This removes the extensions
        String caption = pictureName.substring(0,pos); //from the file names. e.g .gif, .jpg, .png
        picLabel.setIcon(newIcon);					//Set the imageIcon on the Label
        picLabel.setText(caption);					//Set the caption
        picLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM); //Caption appears below the image

    }
}

//
//    JButton previousButton = new JButton("Previous");
//            previousButton.addActionListener(e -> {
//                    zoom = 1;
//                    position--;
//                    if(position < 0 ) {
//        position = files.length -1;
//        }
//        try {
//        image = ImageIO.read(new File(files[position].getAbsolutePath()));
//        } catch (IOException evt) {
//        evt.printStackTrace();
//        System.out.println("Could not open file.");
//        }
//        getParent().repaint();
//        });
//        JButton nextButton = new JButton("Next");
//
//        nextButton.addActionListener(e -> getNextImage("Next"));
//
//        JButton thumbnailButton = new JButton("View Thumbnails");
//
//
//        add(thumbnailButton);
//
//        thumbnailButton.setEnabled(false);
//
//        nextButton.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "getNext");
//        nextButton.getActionMap().put("getNext", nextButton.getAction());
//        }
