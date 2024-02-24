package org.example.presentation;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Modifier extends JFrame {
    private final JButton execute = new JButton("Execute");
    private final JButton cancel = new JButton("Cancel");
    private final JTextField[] entries;
    private final JComboBox[] options = new JComboBox[2];
    private final String[] fields;
    private ArrayList<String> data;

    public Modifier(String[] fields) {
        entries = new JTextField[fields.length];
        this.fields = fields;
    }

    public void createSimple(ArrayList<String> data) {
        this.data = data;

        this.setTitle("Setup Manager");
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        int height = fields.length * 40 + 50;

        JPanel[] panels = new JPanel[2];
        for (int i = 0; i < 2; i++) {
            panels[i] = new JPanel();
            panels[i].setPreferredSize(new Dimension(170, height));
        }

        panels[0].setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panels[1].setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        JPanel[] helpers = new JPanel[fields.length];
        JLabel[] labels = new JLabel[fields.length];

        for (int i = 0; i < fields.length; i++) {
            helpers[i] = new JPanel();
            helpers[i].setPreferredSize(new Dimension(150, 30));
            helpers[i].setLayout(new FlowLayout(FlowLayout.RIGHT));
            labels[i] = new JLabel(fields[i]);
            labels[i].setFont(new Font("arial", Font.BOLD, 15));
            helpers[i].add(labels[i]);
            panels[0].add(helpers[i]);
        }

        for (int i = 0; i < fields.length; i++) {
            entries[i] = new JTextField();
            entries[i].setPreferredSize(new Dimension(150, 30));
            panels[1].add(entries[i]);
        }

        execute.setPreferredSize(new Dimension(150, 30));

        cancel.setPreferredSize(new Dimension(150, 30));

        panels[0].add(execute);
        panels[1].add(cancel);

        this.add(panels[0]);
        this.add(panels[1]);

        this.setLocationRelativeTo(null);

        this.pack();
        this.setVisible(true);
    }

    public void createOrder(ArrayList<String> data, String[] clientData, String[] productData) {
        this.data = data;

        this.setTitle("Setup Manager");
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        int height = fields.length * 40 + 50;

        JPanel[] panels = new JPanel[2];
        for (int i = 0; i < 2; i++) {
            panels[i] = new JPanel();
            panels[i].setPreferredSize(new Dimension(170, height));
        }

        panels[0].setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panels[1].setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        JPanel[] helpers = new JPanel[fields.length];
        JLabel[] labels = new JLabel[fields.length];

        for (int i = 0; i < fields.length; i++) {
            helpers[i] = new JPanel();
            helpers[i].setPreferredSize(new Dimension(150, 30));
            helpers[i].setLayout(new FlowLayout(FlowLayout.RIGHT));
            labels[i] = new JLabel(fields[i]);
            labels[i].setFont(new Font("arial", Font.BOLD, 15));
            helpers[i].add(labels[i]);
            panels[0].add(helpers[i]);
        }

        entries[0] = new JTextField();
        entries[0].setPreferredSize(new Dimension(150, 30));
        panels[1].add(entries[0]);
        options[0] = new JComboBox<>(clientData);
        options[0].setPreferredSize(new Dimension(150, 30));
        panels[1].add(options[0]);
        options[1] = new JComboBox<>(productData);
        options[1].setPreferredSize(new Dimension(150, 30));
        panels[1].add(options[1]);

        execute.setPreferredSize(new Dimension(150, 30));


        cancel.setPreferredSize(new Dimension(150, 30));

        panels[0].add(execute);
        panels[1].add(cancel);

        this.add(panels[0]);
        this.add(panels[1]);

        this.setLocationRelativeTo(null);

        this.pack();
        this.setVisible(true);
    }

    public void uploadOrder() {
        data.add(entries[0].getText());
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < 2; j++) {
            String temp = (String) options[j].getSelectedItem();
            if (temp == null)
                return;
            if (temp.equals(""))
                return;
            for (int i = 0; Character.isDigit(temp.charAt(i)); i++) {
                builder.append(temp.charAt(i));
            }
            data.add(builder.toString());
            builder.delete(0, builder.length());
        }
    }

    public void upload() {
        for (JTextField entry : entries) {
            data.add(entry.getText());
        }
    }

    public JButton[] getButtons() {
        JButton[] buttons = new JButton[2];
        buttons[0] = execute;
        buttons[1] = cancel;
        return buttons;
    }

}