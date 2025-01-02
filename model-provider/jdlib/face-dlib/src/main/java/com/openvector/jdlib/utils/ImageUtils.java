package com.openvector.jdlib.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class ImageUtils {

    private static JFrame frame;
    private static JLabel label;

    public static void drawRectangle(BufferedImage image, Rectangle rectangle) {
        Graphics2D graph = image.createGraphics();
        graph.setColor(Color.GREEN);
        graph.draw(rectangle);
        graph.dispose();
    }

    public static void showImage(BufferedImage image) {
        if (frame == null) {
            frame = new JFrame();
            frame.setTitle("Jdlib Viewer");
            frame.setResizable(false);
            frame.setSize(image.getWidth(), image.getHeight());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            label = new JLabel();
            label.setIcon(new ImageIcon(image));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        } else {
            label.setIcon(new ImageIcon(image));
        }
    }

    public static void drawFaceDescriptor(BufferedImage image, FaceDescriptor facedes) {
    Graphics2D graph = image.createGraphics();
        graph.setColor(Color.GREEN);
        graph.draw(facedes.getFaceBox());
        graph.setColor(Color.RED);
        facedes.getFacialLandmarks().stream().forEach((point) -> {
            graph.fillOval(point.x, point.y, 3, 3);
        });
        graph.dispose();
    }
}
