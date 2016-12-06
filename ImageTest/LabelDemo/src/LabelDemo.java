import java.awt.*;

import javax.swing.*;

class LabelDemo extends JFrame

{

    public LabelDemo() {

        super("JLabel Demo");

        setSize(600, 100);

        JPanel content = (JPanel) getContentPane();

        content.setLayout(new GridLayout(1, 4, 4, 4));

        JLabel label = new JLabel();

        label.setText("JLabel");

        label.setBackground(Color.white);

        content.add(label);

        label = new JLabel("JLabel",

                SwingConstants.CENTER);

        label.setOpaque(true);

        label.setBackground(Color.white);

        content.add(label);

        label = new JLabel("JLabel");

        label.setFont(new Font("Helvetica", Font.BOLD, 18));

        label.setOpaque(true);

        label.setBackground(Color.white);

        content.add(label);

        ImageIcon image = new ImageIcon("flight.gif");

        label = new JLabel("JLabel", image,

                SwingConstants.RIGHT);

        label.setVerticalTextPosition(SwingConstants.TOP);

        label.setOpaque(true);

        label.setBackground(Color.white);

        content.add(label);

        setVisible(true);

    }

    public static void main(String args[]) {

        new LabelDemo();

    }

}