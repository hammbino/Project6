/*
 * Project6
 * Created by Jeffrey Hammond on 11/29/16.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A frame to view images
 * */
class ImageFrame extends JFrame {

    private int position = 0; //Initial position is 0
    private File [] files; //Array to hold the file names
    private BufferedImage image;
    private double zoom = 1;

    ImageFrame() {
        setSize(getPreferredSize());
        getJpegs(".");//TODO handle if FILES is empty
        try {
            image = ImageIO.read(new File(files[0].getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not open file.");
        }

        JPanel imagePanel = new ImagePanel ();
        JPanel thumbnailPanel = new ThumbnailPanel ();

        JPanel imageSizingPanel = new JPanel();
        imageSizingPanel.setLayout(new FlowLayout());

        JButton zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(e -> {
            zoom += .25;
            if (zoom < 0)
                zoom = .5;
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
            if (zoom > 0) {
                repaint();
            } else {
                System.out.println("Maximum zoom out reached"); //TODO add dialogue box
            }
        });

        imageSizingPanel.add(zoomInButton);
        imageSizingPanel.add(defaultSizeButton);
        imageSizingPanel.add(zoomOutButton);

//        JPanel imageView = new FileImage();

        JPanel imageViewPanel = new JPanel();
        imageViewPanel.setLayout(new BoxLayout(imageViewPanel, BoxLayout.PAGE_AXIS));

        JPanel imageNavigationPanel = new JPanel();

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> {
            zoom = 1;
            position--;
            if(position < 0 || position >= files.length ) {
                position = files.length -1;
            }
            try {
                image = ImageIO.read(new File(files[position].getAbsolutePath()));
            } catch (IOException evt) {
                evt.printStackTrace();
                System.out.println("Could not open file.");
            }
            repaint();
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            zoom = 1;
            position++;
            if(position < 0 || position >= files.length ) {
                position = 0;
            }
            try {
                image = ImageIO.read(new File(files[position].getAbsolutePath()));
            } catch (IOException evt) {
                evt.printStackTrace();
                System.out.println("Could not open file.");
            }
            repaint();

        });


        imageNavigationPanel.add(previousButton);
        imageNavigationPanel.add(nextButton);



        JButton thumbnailButton = new JButton("View Thumbnails");
        thumbnailButton.addActionListener(e -> {
            imagePanel.setVisible(false);
            thumbnailPanel.setVisible(true);
            getContentPane().add(thumbnailPanel);
            getContentPane().remove(imagePanel);
            repaint();
        });

        JButton singleImageButton = new JButton("View Image");
        singleImageButton.addActionListener(e -> {
            imagePanel.setVisible(true);
            thumbnailPanel.setVisible(false);
            getContentPane().add(imagePanel);
            getContentPane().remove(thumbnailPanel);
            repaint();
        });

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
                for (int i = 0; i < files.length; i++ ) {
                    if (files[i].toString().equals(chooser.getSelectedFile().toString())) {
                        position = i;
                    }
                }
                repaint();
            } else if (result == JFileChooser.CANCEL_OPTION) {
                System.out.println("No File Selected"); //TODO add dialogue box
            }
        });


        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        imageViewPanel.add(thumbnailButton);
        imageViewPanel.add(singleImageButton);
        imageViewPanel.add(openButton);
        imageViewPanel.add(quitButton);

        JPanel albumButtonPanel = new JPanel();
        albumButtonPanel.setLayout(new BoxLayout(albumButtonPanel, BoxLayout.PAGE_AXIS));

        JButton saveAlbumButton = new JButton("Save Album");
        JButton openAlbumButton = new JButton("Open Album");

        albumButtonPanel.add(saveAlbumButton);
        saveAlbumButton.addActionListener(e -> {
            repaint();
        });

        albumButtonPanel.add(openAlbumButton);
        openAlbumButton.addActionListener(e -> {
            repaint();
        });


        Container contentPane = getContentPane();
//        contentPane.add(new ButtonPanel(), BorderLayout.NORTH);
//        contentPane.add(new ImageSizingPanel(), BorderLayout.NORTH);
        contentPane.add(imageSizingPanel, BorderLayout.NORTH);
        contentPane.add(imageViewPanel, BorderLayout.WEST);
//        contentPane.add(imageView, BorderLayout.CENTER);
        contentPane.add(thumbnailPanel, BorderLayout.CENTER);
        contentPane.add(imagePanel, BorderLayout.CENTER);

        contentPane.add(imageNavigationPanel, BorderLayout.SOUTH);
//        contentPane.add(new ImageNavigationPanel(), BorderLayout.SOUTH);
        contentPane.add(albumButtonPanel, BorderLayout.EAST);


    }

//    private class ButtonPanel extends JPanel {
//        ButtonPanel() {
//            //Todo this needs some love and a refactor
////            JButton openButton = new JButton("Open");
////            openButton.addActionListener(event -> {
////                JFileChooser chooser = new JFileChooser();
////                chooser.setCurrentDirectory(new File("."));
////                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
////                chooser.addChoosableFileFilter(filter);
////                chooser.setAcceptAllFileFilterUsed(false);
////                int result = chooser.showOpenDialog(ImageFrame.this);
////                if (result == JFileChooser.APPROVE_OPTION) {
////                    File selectedFile = chooser.getSelectedFile();
////                    try {
////                        image = ImageIO.read(new File(selectedFile.getAbsolutePath()));
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                        System.out.println("Could not open file.");
////                    }
////
////                    getJpegs(chooser.getCurrentDirectory().toString());
////                    for (int i = 0; i < files.length; i++ ) {
////                        if (files[i].toString().equals(chooser.getSelectedFile().toString())) {
////                            position = i;
////                        }
////                    }
////                    getParent().repaint();
////                } else if (result == JFileChooser.CANCEL_OPTION) {
////                    System.out.println("No File Selected");
////                }
////            });
////
////            JButton zoomInButton = new JButton("Zoom In");
////            zoomInButton.addActionListener(e -> {
////                zoom += .25;
////                getParent().repaint();
////
////            });
////
////            JButton defaultSizeButton = new JButton("100%");
////            defaultSizeButton.addActionListener(e -> {
////                zoom = 1;
////                getParent().repaint();
////            });
////
////            JButton zoomOutButton = new JButton("Zoom Out");
////            zoomOutButton.addActionListener(e -> {
////                zoom -= .25;
////                getParent().repaint();
////            });
//
////            JButton quitButton = new JButton("Quit");
////            quitButton.addActionListener(e -> System.exit(0));
//
////            add(openButton);
////            add(zoomInButton);
////            add(defaultSizeButton);
////            add(zoomOutButton);
////            add(quitButton);
//
//        }
//    }

//    private class ImageNavigationPanel extends JPanel {
//        ImageNavigationPanel() {
//            JButton previousButton = new JButton("Previous");
//            previousButton.addActionListener(e -> {
//                zoom = 1;
//                position--;
//                if(position < 0 || position >= files.length ) {
//                    position = files.length -1;
//                }
//                try {
//                    image = ImageIO.read(new File(files[position].getAbsolutePath()));
//                } catch (IOException evt) {
//                    evt.printStackTrace();
//                    System.out.println("Could not open file.");
//                }
////                setSize(getPreferredSize());
//                getParent().repaint();
//              });
//
//            JButton nextButton = new JButton("Next");
//            nextButton.addActionListener(e -> {
//                zoom = 1;
//                position++;
//                if(position < 0 || position >= files.length ) {
//                    position = 0;
//                }
//                try {
//                    image = ImageIO.read(new File(files[position].getAbsolutePath()));
//                } catch (IOException evt) {
//                    evt.printStackTrace();
//                    System.out.println("Could not open file.");
//                }
//                getParent().repaint();
//
//            });
//
////            JButton thumbnailButton = new JButton("View Thumbnails");
//
//            add(previousButton);
//            add(nextButton);
////            add(thumbnailButton);
////
////            thumbnailButton.setEnabled(false);
//
////            nextButton.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "getNext");
////            nextButton.getActionMap().put("getNext", nextButton.getAction());
//        }
//
//    }

//
//    private class AlbumSaveAndOpenPanel extends JPanel {
//        JButton saveAlbumButton = new JButton("Save Album");
//        JButton openAlbumButton = new JButton("Open Album");
//
//        AlbumSaveAndOpenPanel() {
//            add(saveAlbumButton);
//            saveAlbumButton.addActionListener(e -> {
//                zoom -= .25;
//                getParent().repaint();
//            });
//            add(openAlbumButton);
//            openAlbumButton.addActionListener(e -> {
//                zoom -= .25;
//                getParent().repaint();
//            });
//        }
//    }

//    class FileImage extends JPanel {
//        JPanel cards; //a panel that uses CardLayout
//        FileImage() {
//            //Create the "imageViews".
//            JPanel singleView = new JPanel();
//            singleView.add(new ImagePanel());
//
//            JPanel thumbnailView = new JPanel();
//            thumbnailView.add(new ThumbnailPanel());
//
//            //Create the panel that contains the "cards".
//            cards = new JPanel(new CardLayout());
//            cards.add(singleView);
//            cards.add(thumbnailView);
//        }
//
//        public void changeCardView() {
//            CardLayout cl = (CardLayout) (cards.getLayout());
//            cl.next(cards);
//        }
//    }

    /**
     * A component that displays an image
     */
    class ImagePanel extends JPanel {
        ImagePanel () {
            setVisible(true);
        }
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
     * A component that displays four images
     */
    private class ThumbnailPanel extends JPanel {
        ThumbnailPanel() {
            setVisible(false);
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int w = 100;
                int h = 100;
                g.drawImage(image, 0, 0, w, h, null);
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

//        if (image != null && image.getWidth() > screenSize.getWidth()/2 ) {
//            return new Dimension(image.getWidth(), screenSize.height / 2);
//        }

        return new Dimension(screenSize.width/2, screenSize.height/2);
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

//    //Method to set the picture on the label
//    private void setLabelIcon(Path path, File name){
//        File file = new File(path+"\\"+name);
//        java.awt.image.BufferedImage image = null;
//
//        try{
//            image = ImageIO.read(file); //Read the image from the file.
//        }catch(IOException ie){
//            javax.swing.JOptionPane.showMessageDialog(this,"Error reading image file","Error",
//                    javax.swing.JOptionPane.ERROR_MESSAGE);
//        }
//        ImageIcon icon = null;
//        if (image != null) {
//            icon = new ImageIcon(image);
//        }
//        int width = 600;
//        int height = 400;
//        Image img = null; //Images produced will remain a fixed size, 600 * 400
//        if (icon != null) {
//            img = icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
//        }
//
//        ImageIcon newIcon = null; //Create a new image icon from an image object.
//        if (img != null) {
//            newIcon = new ImageIcon(img);
//        }
//
//        //Now we want to create a caption for the pictures using their file names
//        String pictureName = file.getName();
//        int pos = pictureName.lastIndexOf("."); 	   //This removes the extensions
//        String caption = pictureName.substring(0,pos); //from the file names. e.g .gif, .jpg, .png
//        picLabel.setIcon(newIcon);					//Set the imageIcon on the Label
//        picLabel.setText(caption);					//Set the caption
//        picLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM); //Caption appears below the image
//
//    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}