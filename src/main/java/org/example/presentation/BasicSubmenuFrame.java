package org.example.presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BasicSubmenuFrame extends JPanel {
    private final JButton create = new JButton("CREATE");
    private final JButton update = new JButton("UPDATE");
    private final JButton find = new JButton("FIND");
    private final JButton delete = new JButton("DELETE");
    private final JButton exit = new JButton("EXIT");
    private final DefaultTableModel tableContent;

    public BasicSubmenuFrame(Menu frame, String title) {
        frame.getLayout().setVgap(30);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setPreferredSize(new Dimension(740, 30));

        JPanel genericButtons = new JPanel();
        genericButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 2));
        genericButtons.setPreferredSize(new Dimension(1400, 40));
        genericButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        genericButtons.setBackground(new Color(0, 0, 102));

        this.setPreferredSize(new Dimension(1400, 730));
        this.setLayout(new FlowLayout());
        this.setBackground(new Color(0, 0, 102));

        create.setPreferredSize(new Dimension(100, 27));
        update.setPreferredSize(new Dimension(100, 27));
        find.setPreferredSize(new Dimension(100, 27));
        delete.setPreferredSize(new Dimension(100, 27));
        exit.setPreferredSize(new Dimension(100, 27));

        genericButtons.add(titleLabel);
        genericButtons.add(create);
        genericButtons.add(update);
        genericButtons.add(find);
        genericButtons.add(delete);
        genericButtons.add(exit);


        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50));
        contentPanel.setPreferredSize(new Dimension(1000, 600));
        contentPanel.setBackground(new Color(0, 0, 102));

        tableContent = new DefaultTableModel();

        JTable table = new JTable(tableContent);
        table.setFont(new Font("arial", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 550));

        contentPanel.add(scrollPane);

        this.add(genericButtons);
        this.add(contentPanel);
    }
    public DefaultTableModel getTableContent() {
        return tableContent;
    }

    public JButton[] getButtons() {
        JButton[] buttons = new JButton[5];
        buttons[0] = create;
        buttons[1] = update;
        buttons[2] = find;
        buttons[3] = delete;
        buttons[4] = exit;
        return buttons;
    }

}
