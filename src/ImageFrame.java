/*
 * Project6
 * Created by Jeffrey Hammond on 11/29/16.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * A frame to view images
 * */
class ImageFrame extends JFrame {

    private int position = 0;
    private BufferedImage image;
    private JButton thumbnail1 = new JButton();
    private JButton thumbnail2 = new JButton();
    private JButton thumbnail3 = new JButton();
    private JButton thumbnail4 = new JButton();
    private double zoom = 1;
    private JFileChooser chooser;
    private Album album;

    private JLabel photographLabel = new JLabel();

    ImageFrame() {

        // A label for displaying the pictures
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        photographLabel.setText("Testing");

        setSize(getPreferredSize());
        setMinimumSize(new Dimension(550,500));
        getJpegs(".");//TODO handle if FILES is empty
        setImage();

        JPanel imagePanel = new ImagePanel ();
        JPanel thumbnailPanel = new JPanel();

        thumbnailPanel.setVisible(false);
        thumbnailPanel.setLayout(new GridLayout(2, 2));
        thumbnailPanel.add(thumbnail1);
        thumbnailPanel.add(thumbnail2);
        thumbnailPanel.add(thumbnail3);
        thumbnailPanel.add(thumbnail4);

        JPanel imageSizingPanel = new JPanel();
        imageSizingPanel.setLayout(new FlowLayout());

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
            if (zoom > .25) {
                zoom -= .25;
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Maximum zoom out reached", "Image Too Small", JOptionPane.ERROR_MESSAGE);
                System.out.println("Maximum zoom out reached");
            }
        });

        imageSizingPanel.add(zoomInButton);
        imageSizingPanel.add(defaultSizeButton);
        imageSizingPanel.add(zoomOutButton);

        JPanel imageViewPanel = new JPanel();
        imageViewPanel.setLayout(new BoxLayout(imageViewPanel, BoxLayout.PAGE_AXIS));

        JPanel imageNavigationPanel = new JPanel();

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> {
            zoom = 1;
            position--;
            if(position < 0 || position >= album.getAlbumSize() ) {
                position = album.getAlbumSize() - 1;
            }
            if(thumbnailPanel.isDisplayable()) {
                position -= 7;
                if (position < 0) {
                    position = (album.getAlbumSize() + position);
                }
                setThumbnails();
            }
            else {
                setImage();
            }
            repaint();
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            zoom = 1;

            if(position < 0 || position >= album.getAlbumSize() ) {
                position = 0;
            }
            if(thumbnailPanel.isDisplayable()) {
                setThumbnails();
            }
            else {
                position++;
                setImage();
            }
            repaint();
        });


        imageNavigationPanel.add(previousButton);
        imageNavigationPanel.add(nextButton);

        JButton setCaptionButton = new JButton("Set Caption");
        setCaptionButton.addActionListener(e -> {
            //TODO Get Caption
            String s = JOptionPane.showInputDialog(this, "Caption for Image:", null, JOptionPane.PLAIN_MESSAGE);
            if ((s != null) && (s.length() > 0)) {
                album.setFileCaption(s, position);
                setImage();
            }
            repaint();
        });

        JButton thumbnailButton = new JButton("View Thumbnails");
        thumbnailButton.addActionListener(e -> {
            imagePanel.setVisible(false);
            thumbnailPanel.setVisible(true);
            imageSizingPanel.setEnabled(false);
            imageSizingPanel.setVisible(false);
            thumbnailButton.setVisible(false);
            setCaptionButton.setVisible(false);
            getContentPane().add(thumbnailPanel);
            getContentPane().remove(imagePanel);
            setThumbnails();
            repaint();
        });

        thumbnail1.addActionListener(e -> {
            imagePanel.setVisible(true);
            thumbnailPanel.setVisible(false);
            imageSizingPanel.setEnabled(true);
            imageSizingPanel.setVisible(true);
            setCaptionButton.setVisible(true);
            thumbnailButton.setVisible(true);
            getContentPane().add(imagePanel);
            getContentPane().remove(thumbnailPanel);
            position = album.getNameLocation(thumbnail1.getText());
            setImage();
            repaint();
        });
        thumbnail2.addActionListener(e -> {
            imagePanel.setVisible(true);
            thumbnailPanel.setVisible(false);
            imageSizingPanel.setEnabled(true);
            imageSizingPanel.setVisible(true);
            setCaptionButton.setVisible(true);
            thumbnailButton.setVisible(true);
            getContentPane().add(imagePanel);
            getContentPane().remove(thumbnailPanel);
            position = album.getNameLocation(thumbnail2.getText());
            setImage();
            repaint();
        });
        thumbnail3.addActionListener(e -> {
            imagePanel.setVisible(true);
            thumbnailPanel.setVisible(false);
            imageSizingPanel.setEnabled(true);
            imageSizingPanel.setVisible(true);
            setCaptionButton.setVisible(true);
            thumbnailButton.setVisible(true);
            getContentPane().add(imagePanel);
            getContentPane().remove(thumbnailPanel);
            position = album.getNameLocation(thumbnail3.getText());
            setImage();
            repaint();
        });
        thumbnail4.addActionListener(e -> {
            imagePanel.setVisible(true);
            thumbnailPanel.setVisible(false);
            imageSizingPanel.setEnabled(true);
            imageSizingPanel.setVisible(true);
            setCaptionButton.setVisible(true);
            thumbnailButton.setVisible(true);
            getContentPane().add(imagePanel);
            getContentPane().remove(thumbnailPanel);
            position = album.getNameLocation(thumbnail4.getText());
            setImage();
            repaint();
        });

        JButton openButton = new JButton("Open");
        openButton.addActionListener(event -> {
            chooser.setCurrentDirectory(new File("."));
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
                position = album.getFileLocation(chooser.getSelectedFile());
                if(thumbnailPanel.isDisplayable()) {
                    setThumbnails();
                } else {
                    setImage();
                }
                repaint();
            } else if (result == JFileChooser.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(this, "No File Selected", "No Files", JOptionPane.ERROR_MESSAGE);
                System.out.println("No File Selected"); //TODO add dialogue box
            }
        });

        // set up file chooser
        chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg");
        chooser.setFileFilter(filter);
        chooser.setAccessory(new ImagePreviewer(chooser));
        chooser.setFileView(new FileIconView(filter, new ImageIcon("palette.gif")));

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        imageViewPanel.add(thumbnailButton);
        imageViewPanel.add(setCaptionButton);
        imageViewPanel.add(openButton);
        imageViewPanel.add(quitButton);

        JPanel albumButtonPanel = new JPanel();
        albumButtonPanel.setLayout(new BoxLayout(albumButtonPanel, BoxLayout.PAGE_AXIS));

        JButton saveAlbumButton = new JButton("Save Album");
        JButton openAlbumButton = new JButton("Open Album");

        albumButtonPanel.add(saveAlbumButton);
        saveAlbumButton.addActionListener(e -> {
//            int result = chooser.showOpenDialog(ImageFrame.this);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = chooser.getSelectedFile();
            album.setPositionWhenSaved(position);
            if(thumbnailPanel.isDisplayable()) {
               album.setIsThumbnailPanelDisplayed(1);
            } else {
                album.setIsThumbnailPanelDisplayed(0);
            }

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("listImages.dat")))
            {
                out.writeObject(album);
            } catch (IOException evt) {
                evt.printStackTrace();
            }
        });

        albumButtonPanel.add(openAlbumButton);
        openAlbumButton.addActionListener(e -> {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("listImages.dat"))) {
//                // retrieve all records into album
                album = (Album) in.readObject();
                position = album.getPositionWhenSaved();
                if(album.getIsThumbnailPanelDisplayed() == 1) {
                    imagePanel.setVisible(false);
                    thumbnailPanel.setVisible(true);
                    imageSizingPanel.setEnabled(false);
                    imageSizingPanel.setVisible(false);
                    thumbnailButton.setVisible(false);
                    setCaptionButton.setVisible(false);
                    getContentPane().add(thumbnailPanel);
                    getContentPane().remove(imagePanel);
                    setThumbnails();
                } else {
                    imagePanel.setVisible(true);
                    thumbnailPanel.setVisible(false);
                    imageSizingPanel.setEnabled(true);
                    imageSizingPanel.setVisible(true);
                    setCaptionButton.setVisible(true);
                    thumbnailButton.setVisible(true);
                    getContentPane().add(imagePanel);
                    getContentPane().remove(thumbnailPanel);
                    setImage();
                }
            } catch (ClassNotFoundException | IOException evt) {
                evt.printStackTrace();
            }
            repaint();
        });

        add(imageSizingPanel, BorderLayout.NORTH);
        add(imageViewPanel, BorderLayout.WEST);
        add(imagePanel, BorderLayout.CENTER);
        add(imageNavigationPanel, BorderLayout.SOUTH);
        add(albumButtonPanel, BorderLayout.EAST);
    }

    private void getJpegs(String fileDir) {
        //TODO
        album = new Album(fileDir);
        if(album.getAlbumSize() < 1) {
            JOptionPane.showMessageDialog(this, "There were no images in current directory", "No Files", JOptionPane.ERROR_MESSAGE);
            System.out.println("There were no images in current directory.");
        }
    }

    /**
     * A component that displays an image
     */
    private class ImagePanel extends JPanel {

        ImagePanel () {
            setLayout(new BorderLayout());
            add(photographLabel, BorderLayout.SOUTH);
            setVisible(true);
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int w = 500;//image.getWidth();
                int h = 400;//image.getHeight();
                g.drawImage(image, 0, 0, (int) (w * zoom), (int) (h * zoom), null);
            }
        }
    }

    /**
     * A method that sets an image
     */
    private void setImage() {
        if (position < 0) {
            position = album.getAlbumSize() - 1;
        }
        if (position >= album.getAlbumSize()) {
            position = 0;
        }
        try {
            image = ImageIO.read(album.getFileAtLocation(position));
            //TODO add filename and caption to label.
            photographLabel.setText("<html>" +  album.getName(position) + "<br>" + album.getFileCaption(position) + "</html>");
        } catch (IOException evt) {
            evt.printStackTrace();
            System.out.println("Could not open file.");
        }

    }

    private void setThumbnails() {
        int numberToIterate = 4;
        if (position < 0) {
            position = album.getAlbumSize() - 1;
        }
        if (position >= album.getAlbumSize()) {
            position = 0;
        }
        if (album.getAlbumSize() > 0) {
            if (album.getAlbumSize() < numberToIterate)
                numberToIterate = album.getAlbumSize();
            for (int i = 0; i < numberToIterate; i++) {
                if (position > album.getAlbumSize() -1) {
                    position = 0;
                }
                ImageIcon thumbnailImage = new ImageIcon(album.getFileURL(position));
                Image tempImage = thumbnailImage.getImage();
                Image scaledImage = tempImage.getScaledInstance(80, 60, Image.SCALE_SMOOTH);
                thumbnailImage.setImage(scaledImage);
                switch (i) {
                    case 0:
                        thumbnail1.setIcon(thumbnailImage);
                        thumbnail1.setText("<html>" +  album.getName(position) + "<br>" + album.getFileCaption(position) + "</html>");
                        thumbnail1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                        thumbnail1.setHorizontalTextPosition(SwingConstants.CENTER);
                        break;
                    case 1:
                        thumbnail2.setIcon(thumbnailImage);
                        thumbnail2.setText("<html>" +  album.getName(position) + "<br>" + album.getFileCaption(position) + "</html>");
                        thumbnail2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                        thumbnail2.setHorizontalTextPosition(SwingConstants.CENTER);
                        break;
                    case 2:
                        thumbnail3.setIcon(thumbnailImage);
                        thumbnail3.setText("<html>" +  album.getName(position) + "<br>" + album.getFileCaption(position) + "</html>");
                        thumbnail3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                        thumbnail3.setHorizontalTextPosition(SwingConstants.CENTER);
                        break;
                    case 3:
                        thumbnail4.setIcon(thumbnailImage);
                        thumbnail4.setText("<html>" +  album.getName(position) + "<br>" + album.getFileCaption(position) + "</html>");
                        thumbnail4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                        thumbnail4.setHorizontalTextPosition(SwingConstants.CENTER);
                        break;
                    default:
                        break;
                }
            position++;
            }
        }
    }

    /**
     * Sets default dimensions of the JFrame
     */
    public Dimension getPreferredSize() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setLocation(screenSize.width/5, screenSize.height/5);

        return new Dimension(screenSize.width/2, (screenSize.height/2) + 100);
    }
}