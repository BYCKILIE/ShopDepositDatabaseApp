package org.example.presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillSubmenuFrame extends JPanel {
    private final Menu parent;
    private final JButton find = new JButton("FIND");
    private final JButton exit = new JButton("EXIT");
    private final DefaultTableModel tableContent;

    public BillSubmenuFrame(Menu frame) {

        frame.getLayout().setVgap(30);

        JLabel titleLabel = new JLabel("Bill Pane");
        titleLabel.setFont(new Font("arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(1100, 30));

        JPanel genericButtons = new JPanel();
        genericButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 2));
        genericButtons.setPreferredSize(new Dimension(1400, 40));
        genericButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        genericButtons.setBackground(new Color(0, 0, 102));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50));
        contentPanel.setPreferredSize(new Dimension(1000, 600));
        contentPanel.setBackground(new Color(0, 0, 102));

        parent = frame;
        this.setPreferredSize(new Dimension(1400, 730));
        this.setLayout(new FlowLayout());
        this.setBackground(new Color(0, 0, 102));

        find.setPreferredSize(new Dimension(100, 27));
        exit.setPreferredSize(new Dimension(100, 27));

        genericButtons.add(titleLabel);
        genericButtons.add(find);
        genericButtons.add(exit);

        String[] columnNames = {"Id", "Total_Price"};

        tableContent = new DefaultTableModel();

        JTable table = new JTable(tableContent);
        table.setFont(new Font("arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 550));

        tableContent.setColumnIdentifiers(columnNames);

        contentPanel.add(scrollPane);

        this.add(genericButtons);
        this.add(contentPanel);
        parent.add(this);
    }

    public DefaultTableModel getTableContent() {
        return tableContent;
    }

    public JButton[] getButtons() {
        JButton[] buttons = new JButton[2];
        buttons[0] = find;
        buttons[1] = exit;
        return buttons;
    }

}
