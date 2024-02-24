package org.example.businessLayer;

import org.example.DAO.OrderDAO;
import org.example.model.Orders;
import org.example.presentation.BasicSubmenuFrame;
import org.example.presentation.Menu;
import org.example.presentation.Modifier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OrderPane implements ActionListener {
    private OrderDAO order;
    private BasicSubmenuFrame content;
    private Modifier modifier = null;
    private JButton[] buttons;
    private final Menu parent;
    private String[] fullColumns;
    private final String[] utilColumns;
    private final ArrayList<String> alter_data = new ArrayList<>();
    OrderDetails orderDetails;
    private String direction = "";

    public OrderPane(Menu frame) {
        parent = frame;

        content = new BasicSubmenuFrame(frame, "Order Menu");
        buttons = content.getButtons();

        for (JButton button : buttons) {
            button.addActionListener(this);
        }

        initialise();

        utilColumns = removeIdFromColumns();

        parent.add(content);
    }

    private String[] orderColumns(String[] str) {
        String[] res = new String[3];
        System.arraycopy(str, 1, res, 0, 3);
        return res;
    }

    private void initialise() {
        orderDetails = new OrderDetails();
        order = new OrderDAO();
        fullColumns = order.getHeader();
        content.getTableContent().setColumnIdentifiers(fullColumns);
        List<Orders> data = order.findAll();
        if (data != null) {
            for (Orders datum : data) {
                content.getTableContent().addRow(order.extractLine(datum));
            }
        }
    }

    private String[] removeIdFromColumns() {
        String[] res = new String[fullColumns.length - 1];
        System.arraycopy(fullColumns, 1, res, 0, fullColumns.length - 1);
        return res;
    }

    private String[] transform(ArrayList<String> str) {
        String[] res = new String[str.size()];
        for (int i = 0; i < str.size(); i++) {
            res[i] = str.get(i);
        }
        return res;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == buttons[0]) {
            // CREATE
            direction = "create";
            modifier = new Modifier(orderColumns(utilColumns));
            modifier.createOrder(alter_data,
                    orderDetails.getDisplayData("client"), orderDetails.getDisplayData("product"));
        } else if (source == buttons[1]) {
            // UPDATE
            direction = "update";

            modifier = new Modifier(fullColumns);
            modifier.createSimple(alter_data);
        } else if (source == buttons[2]) {
            // FIND
            String id = JOptionPane.showInputDialog(parent, "Enter the ID to Find");
            if (id != null) {
                if (!id.equals("")) {
                    String[] extractedFind = order.extractLine(order.findById(Integer.parseInt(id)));
                    if (extractedFind != null) {
                        JOptionPane.showMessageDialog(parent, inLineStrings(extractedFind));
                    }
                }
            }
        } else if (source == buttons[3]) {
            // DELETE
            String id = JOptionPane.showInputDialog(parent, "Enter the ID to Delete");
            if (id != null)
                if (!id.equals(""))
                    order.delete(Integer.parseInt(id));
        } else if (source == buttons[4]) {
            // EXIT
            parent.remove(content);
            parent.restore();
            parent.revalidate();
            parent.repaint();
        }

        if (modifier != null)
            commitment(source);
        else if (source != buttons[4])
            refresh();
    }

    private String inLineStrings(String[] str) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            res.append(fullColumns[i]).append(" - ").append(str[i]).append("\n");
        }
        return res.toString();
    }

    private void commitment(Object source) {
        JButton[] modifierButtons = modifier.getButtons();
        modifierButtons[0].addActionListener(this);
        modifierButtons[1].addActionListener(this);

        if (source == modifierButtons[0]) {
            modifier.uploadOrder();
            if (alter_data.size() != 0) {
                switch (direction) {
                    case "create" -> create();
                    case "update" -> order.update(Integer.parseInt(alter_data.get(0)), transform(alter_data));
                }

            }

            modifier.dispose();
            refresh();
        } else if (source == modifierButtons[1]) {
            modifier.dispose();
        }
    }

    private void create() {
        String[] extracted = orderDetails.createBill(alter_data);
        if (extracted == null) {
            JOptionPane.showMessageDialog(parent, "Out of Stock");
        } else
            order.insert(extracted);
    }

    private void refresh() {
        parent.remove(content);

        content = new BasicSubmenuFrame(parent, "Order Menu");
        buttons = content.getButtons();

        for (JButton button : buttons) {
            button.addActionListener(this);
        }

        modifier = null;

        content.getTableContent().setColumnIdentifiers(fullColumns);

        initialise();

        alter_data.clear();

        parent.add(content);
        parent.revalidate();
        parent.repaint();
    }

}
