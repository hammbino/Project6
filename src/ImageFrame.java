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
import java.io.StreamCorruptedException;

/**
 * A frame to view images
 * */
class ImageFrame extends JFrame {

    private int position = 0; //Initial position is 0
    private boolean thumbnailViewEnabled = false;
    private File [] files; //Array to hold the file names
    BufferedImage image;
    private int fileLocal = 0;
    BufferedImage thumbnail1;
    BufferedImage thumbnail2;
    BufferedImage thumbnail3;
    BufferedImage thumbnail4;
    private double zoom = 1;

    ImageFrame() {
        setSize(getPreferredSize());
        getJpegs(".");//TODO handle if FILES is empty
        setImage();

        JPanel imagePanel = new ImagePanel ();
        JPanel thumbnailPanel = new ThumbnailPanel();

        JPanel imageSizingPanel = new JPanel();
        imageSizingPanel.setLayout(new FlowLayout());

        JButton zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(e -> {
//            if (zoom <= 0)
//                zoom = .25;
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
            if (zoom > .25) {
                zoom -= .25;
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
//            setThumbnails();
            setImage();
            repaint();
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            zoom = 1;
            position++;
            if(position < 0 || position >= files.length ) {
                position = 0;
            }
//            setThumbnails();
            setImage();
            repaint();
        });


        imageNavigationPanel.add(previousButton);
        imageNavigationPanel.add(nextButton);


        JButton singleImageButton = new JButton("View Image");
        JButton thumbnailButton = new JButton("View Thumbnails");

        thumbnailButton.addActionListener(e -> {
            imagePanel.setVisible(false);
            thumbnailPanel.setVisible(true);
            zoomInButton.setEnabled(false);
            zoomOutButton.setEnabled(false);
            defaultSizeButton.setEnabled(false);
            thumbnailButton.setEnabled(false);
            singleImageButton.setEnabled(true);
            getContentPane().add(thumbnailPanel);
            getContentPane().remove(imagePanel);
            repaint();
        });


        singleImageButton.addActionListener(e -> {
            imagePanel.setVisible(true);
            thumbnailPanel.setVisible(false);
            zoomInButton.setEnabled(true);
            zoomOutButton.setEnabled(true);
            defaultSizeButton.setEnabled(true);
            singleImageButton.setEnabled(false);
            thumbnailButton.setEnabled(true);
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
//        contentPane.add(thumbnailPanel, BorderLayout.CENTER);
        contentPane.add(imagePanel, BorderLayout.CENTER);

        contentPane.add(imageNavigationPanel, BorderLayout.SOUTH);
//        contentPane.add(new ImageNavigationPanel(), BorderLayout.SOUTH);
        contentPane.add(albumButtonPanel, BorderLayout.EAST);

        singleImageButton.setEnabled(false);
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
                int w = 500;//image.getWidth();
                int h = 400;//image.getHeight();
//                image = image.getTile(500, 400);
                g.drawImage(image, 0, 0, (int) (w * zoom), (int) (h * zoom), null);
            }
        }
    }

    private void setImage() {
        try {
            image = ImageIO.read(new File(files[position].getAbsolutePath()));
        } catch (IOException evt) {
            evt.printStackTrace();
            System.out.println("Could not open file.");
        }
    }

    /**
     * A component that displays four images
     */
    private class ThumbnailPanel extends JPanel {

        int groupLocal = 1;

        ThumbnailPanel() {
            setVisible(false);
            setLayout(new GridLayout(2, 2));
//            setThumbnails();
        }


//        public void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            if (files != null) {
//                try {
//                    thumbnail1 = ImageIO.read(new File(files[fileLocal++].getAbsolutePath()));
//                    thumbnail2 = ImageIO.read(new File(files[fileLocal++].getAbsolutePath()));
//                    thumbnail3 = ImageIO.read(new File(files[fileLocal++].getAbsolutePath()));
//                    thumbnail4 = ImageIO.read(new File(files[fileLocal++].getAbsolutePath()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    System.out.println("Could not open file.");
//                }
//                int w = 100;
//                int h = 100;
//                g.drawImage(thumbnail1, 0, 0, w, h, null);
//                g.drawImage(thumbnail2, 0, 0, w, h, null);
//                g.drawImage(thumbnail3, 0, 0, w, h, null);
//                g.drawImage(thumbnail4, 0, 0, w, h, null);
//            }
//        }
    }

    private void setThumbnails() {
        if (files != null && fileLocal < files.length) {
            for (int i = fileLocal % 4; i < 4; i++) {
                String tempThumbnail = "Thumbnail" + i;
                if (fileLocal < files.length) {
                    switch (i) {
                        case 0:
                            try {
                                thumbnail1 = ImageIO.read(new File(files[position].getAbsolutePath()));
                            } catch (IOException evt) {
                                evt.printStackTrace();
                                System.out.println("Could not open file.");
                            }
                            position++;
//                            thumbnail1.s
//                                    setIcon(new ImageIcon(files[fileLocal++].getAbsolutePath()));
                            break;
                        case 1:
                            try {
                                thumbnail2 = ImageIO.read(new File(files[position].getAbsolutePath()));
                            } catch (IOException evt) {
                                evt.printStackTrace();
                                System.out.println("Could not open file.");
                            }
                            position++;
//                            thumbnail2.setIcon(new ImageIcon(files[fileLocal++].getAbsolutePath()));
                            break;
                        case 2:
                            try {
                                thumbnail3 = ImageIO.read(new File(files[position].getAbsolutePath()));
                            } catch (IOException evt) {
                                evt.printStackTrace();
                                System.out.println("Could not open file.");
                            }
                            position++;
//                            thumbnail3.setIcon(new ImageIcon(files[fileLocal++].getAbsolutePath()));
                            break;
                        case 3:
                            try {
                                thumbnail4 = ImageIO.read(new File(files[position].getAbsolutePath()));
                            } catch (IOException evt) {
                                evt.printStackTrace();
                                System.out.println("Could not open file.");
                            }
                            position++;
//                            thumbnail4.setIcon(new ImageIcon(files[fileLocal++].getAbsolutePath()));
                            break;
                    }
                    position++;
                }
            }

        }
    }



//    /**
//     * A component that displays four images
//     */
//    private class ThumbnailPanel extends JPanel {
//        ThumbnailPanel() {
//            setLayout(new GridLayout());
//
//            setVisible(false);
//        }
//        public void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            if (image != null) {
//                int w = 100;
//                int h = 100;
//                g.drawImage(image, 0, 0, w, h, null);
//            }
//        }
//    }

    /**
     * Sets default dimensions of the JFrame
     */
    public Dimension getPreferredSize() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setLocation(screenSize.width/5, screenSize.height/5);

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
}