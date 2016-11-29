//I declare that the following source code is my work.
//I understand and can explain everything I have written, if asked.
//I understand that copying any source code, in whole or in part,
//that is not in my textbook nor provided or expressly permitted by the instructor,
//constitutes cheating. I will receive a zero on this project for
//poor academic performance if I am found in violation of this policy.

//Jeffrey Hammond
//CS3250
//Project 6 - JIMachine

import java.awt.*;
import javax.swing.*;

public class JIMachine {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new ImageFrame();
            frame.setTitle("JIMachine");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}