package org.example.presentation;

import org.example.businessLayer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu extends JFrame implements ActionListener {
    private final AtomicInteger indexButton = new AtomicInteger(0);
    private final JPanel titlePanel = new JPanel();
    private final JButton[] button = new JButton[3];
    private final JPanel[] panels = new JPanel[3];
    private final FlowLayout layout = new FlowLayout();
    private final int R = 0, G = 0, B = 153;

    public Menu() {
        layout.setVgap(0);

        this.setTitle("Shop");
        this.setLayout(layout);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(R, G, B));

        titlePanel.setBackground(new Color(R, G, B));
        titlePanel.setLayout(new FlowLayout());
        JLabel title = new JLabel("   Shop Manager");
        title.setFont(new Font("arial", Font.BOLD, 60));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);

        this.add(titlePanel);
        this.add(createClientPanel());
        this.add(createProductPanel());
        this.add(createOrderPanel());

        this.setVisible(true);
    }

    private void setInitState() {
        layout.setVgap(0);
        this.add(titlePanel);
        for (int i = 0; i < 3; i++) {
            this.add(panels[i]);
        }
    }

    private JButton initButton(String imagePath, int width, int height) {
        button[indexButton.get()] = new JButton();
        button[indexButton.get()].setBorderPainted(false);
        button[indexButton.get()].setFocusPainted(false);
        button[indexButton.get()].setContentAreaFilled(false);
        button[indexButton.get()].setPreferredSize(new Dimension(width, height));
        button[indexButton.get()].setIcon(resizeIcon(new ImageIcon(imagePath), width, height));
        button[indexButton.get()].addActionListener(this);
        return button[indexButton.getAndIncrement()];
    }

    private JPanel createClientPanel() {
        panels[0] = new JPanel();
        panels[0].setBackground(new Color(R, G, B));
        panels[0].setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));
        panels[0].setPreferredSize(new Dimension(570, 400));

        panels[0].add(initButton("images/client.png", 300, 300));

        return panels[0];
    }

    private JPanel createProductPanel() {
        panels[1] = new JPanel();
        panels[1].setBackground(new Color(R, G, B));
        panels[1].setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panels[1].setPreferredSize(new Dimension(500, 400));

        panels[1].add(initButton("images/product.png", 250, 250));

        return panels[1];
    }

    private JPanel createOrderPanel() {
        panels[2] = new JPanel();
        panels[2].setBackground(new Color(R, G, B));
        panels[2].setLayout(new FlowLayout(FlowLayout.RIGHT, 150, 30));
        panels[2].setPreferredSize(new Dimension(500, 350));

        panels[2].add(initButton("images/order.png", 200, 200));

        return panels[2];
    }

    private static Icon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void dispatchAll() {
        this.remove(titlePanel);
        for (int i = 0; i < 3; i++) {
            this.remove(panels[i]);
        }
        this.revalidate();
        this.repaint();
    }

    public void restore() {
        this.setInitState();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == button[0]) {
            //CLIENT
            new ClientPane(this);
        } else if (source == button[1]) {
            //PRODUCT
            new ProductPane(this);
        } else if (source == button[2]) {
            //ORDER
            new OrderPane(this);
        }
        dispatchAll();
    }

    @Override
    public FlowLayout getLayout() {
        return layout;
    }
}
