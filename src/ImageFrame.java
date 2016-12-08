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
import java.util.ArrayList;

/**
 * A frame to view images
 * */
class ImageFrame extends JFrame {

    private int position = 0; //Initial position is 0
    private ArrayList<File> files = new ArrayList<>(); //Array to hold the file names
    private BufferedImage image;
    private JButton thumbnail1 = new JButton();
    private JButton thumbnail2 = new JButton();
    private JButton thumbnail3 = new JButton();
    private JButton thumbnail4 = new JButton();
    private double zoom = 1;
    private JFileChooser chooser;

    private JLabel photographLabel = new JLabel();

    ImageFrame() {

        // A label for displaying the pictures
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setSize(getPreferredSize());
        setMinimumSize(new Dimension(650,600));
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
                System.out.println("Maximum zoom out reached"); //TODO add dialogue box
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
            if(position < 0 || position >= files.size() ) {
                position = files.size() - 1;
            }
            if(thumbnailPanel.isDisplayable()) {
                position -= 7;
                if (position < 0) {
                    position = (files.size() + position);
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

            if(position < 0 || position >= files.size() ) {
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

        setCaptionButton.addActionListener(e -> {
            //TODO Get Caption
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
            position = files.indexOf(thumbnail1.getText());
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
            position = files.indexOf(thumbnail2.getText());
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
            position = files.indexOf(new File(thumbnail3.getText()));
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
            position = files.indexOf(thumbnail4.getText()); //TODO check against file of names
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
                //Todo Could possibly be set the same way when clicking on thumbnail
                for (int i = 0; i < files.size(); i++ ) {
                    if (files.get(i).toString().equals(chooser.getSelectedFile().toString())) {
                        position = i;
                    }
                }
                if(thumbnailPanel.isDisplayable()) {
                    setThumbnails();
                }
                repaint();
            } else if (result == JFileChooser.CANCEL_OPTION) {
                System.out.println("No File Selected"); //TODO add dialogue box
            }
        });

        // set up file chooser
        chooser = new JFileChooser();
        // accept all image files ending with .jpg, .jpeg
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
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("listImages.dat")))
            {
                out.writeObject(files);
            } catch (IOException evt) {
                evt.printStackTrace();
            }
        });

        albumButtonPanel.add(openAlbumButton);
        openAlbumButton.addActionListener(e -> {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("listImages.dat")))
            {
                // retrieve all records into files array
                files = (ArrayList<File>) in.readObject();
                setImage();
                setThumbnails();

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

    void getJpegs(String fileDir) {
        files.clear();
        File dir = new File(fileDir);
        for (File fileName: dir.listFiles((dir1, name) -> name.matches("\\w*(.jpeg|.jpg)"))) {
            files.add(fileName);
        }
//  TODO create ALBUM CLASS that stores the list of captions file names and file URLs

        if (files != null) {
            for (File jpeg : files) {
                System.out.println(jpeg);
            }
        }
    }

    /**
     * A component that displays an image
     */
    private class ImagePanel extends JPanel {
        ImagePanel () {
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
            position = files.size() - 1;
        }
        if (position >= files.size()) {
            position = 0;
        }
        try {
            image = ImageIO.read(new File(files.get(position).getAbsolutePath()));
        } catch (IOException evt) {
            evt.printStackTrace();
            System.out.println("Could not open file.");
        }
    }

    private void setThumbnails() {
        int numberToIterate = 4;
        if (position < 0) {
            position = files.size() - 1;
        }
        if (position >= files.size()) {
            position = 0;
        }
        if (files.size() > 0) {
            if (files.size() < numberToIterate)
                numberToIterate = files.size();
            for (int i = 0; i < numberToIterate; i++) {
                if (position > files.size() -1) {
                    position = 0;
                }
                ImageIcon thumbnailImage = new ImageIcon(files.get(position).getAbsolutePath());
                Image tempImage = thumbnailImage.getImage();
                Image scaledImage = tempImage.getScaledInstance(80, 60, Image.SCALE_SMOOTH);
                thumbnailImage.setImage(scaledImage);
                String pictureName = files.get(position).getName();
                switch (i) {
                    case 0:
                        thumbnail1.setIcon(thumbnailImage);
                        thumbnail1.setText(pictureName);
                        thumbnail1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                        thumbnail1.setHorizontalTextPosition(SwingConstants.CENTER);
                        break;
                    case 1:
                        thumbnail2.setIcon(thumbnailImage);
                        thumbnail2.setText(pictureName);
                        thumbnail2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                        thumbnail2.setHorizontalTextPosition(SwingConstants.CENTER);
                        break;
                    case 2:
                        thumbnail3.setIcon(thumbnailImage);
                        thumbnail3.setText(pictureName);
                        thumbnail3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                        thumbnail3.setHorizontalTextPosition(SwingConstants.CENTER);
                        break;
                    case 3:
                        thumbnail4.setIcon(thumbnailImage);
                        thumbnail4.setText(pictureName);
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

        return new Dimension(screenSize.width/2, screenSize.height/2);
    }


//        //Now we want to create a caption for the pictures using their file names
//        String pictureName = file.getName();
//        int pos = pictureName.lastIndexOf("."); 	   //This removes the extensions
//        String caption = pictureName.substring(0,pos); //from the file names. e.g .gif, .jpg, .png
//        picLabel.setIcon(newIcon);					//Set the imageIcon on the Label
//        picLabel.setText(caption);					//Set the caption
//        picLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM); //Caption appears below the image
}