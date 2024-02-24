package org.example.businessLayer;

import org.example.DAO.ClientDAO;
import org.example.model.Client;
import org.example.presentation.BasicSubmenuFrame;
import org.example.presentation.Menu;
import org.example.presentation.Modifier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ClientPane implements ActionListener {
    private ClientDAO client;
    private BasicSubmenuFrame content;
    private Modifier modifier = null;
    private JButton[] buttons;
    private final Menu parent;
    private String[] fullColumns;
    private final String[] utilColumns;
    private final ArrayList<String> alter_data = new ArrayList<>();

    public ClientPane(Menu frame) {
        parent = frame;

        content = new BasicSubmenuFrame(frame, "Client Menu");
        buttons = content.getButtons();

        for (JButton button : buttons) {
            button.addActionListener(this);
        }

        initialise();

        utilColumns = removeIdFromColumns();

        parent.add(content);
    }

    private void initialise() {
        client = new ClientDAO();
        fullColumns = client.getHeader();
        content.getTableContent().setColumnIdentifiers(fullColumns);
        List<Client> data = client.findAll();
        if (data != null) {
            for (Client datum : data) {
                content.getTableContent().addRow(client.extractLine(datum));
            }
        }
    }

    private String[] removeIdFromColumns() {
        String[] res = new String[fullColumns.length - 1];
        System.arraycopy(fullColumns, 1, res, 0, fullColumns.length - 1);
        return res;
    }

    private String[] transform(ArrayList<String> str, boolean hasId) {
        if (hasId) {
            alter_data.remove(0);
        }
        String[] res = new String[str.size()];
        for (int i = 0; i < str.size(); i++) {
            res[i] = str.get(i);
        }
        return res;
    }

    private String direction = "";

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == buttons[0]) {
            // CREATE
            direction = "create";
            modifier = new Modifier(utilColumns);
            modifier.createSimple(alter_data);
        } else if (source == buttons[1]) {
            // UPDATE
            direction = "update";

            modifier = new Modifier(fullColumns);
            modifier.createSimple(alter_data);
        } else if (source == buttons[2]) {
            String id = JOptionPane.showInputDialog(parent, "Enter the ID to Find");
            if (id != null) {
                if (!id.equals("")) {
                    String[] extractedFind = find(Integer.parseInt(id));
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
                    client.delete(Integer.parseInt(id));
        }
        if (source == buttons[4]) {
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

    private String[] find(int id) {
        return client.extractLine(client.findById(id));
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
            modifier.upload();

            if (alter_data.size() != 0) {
                switch (direction) {
                    case "create" -> client.insert(transform(alter_data, false));
                    case "update" -> client.update(Integer.parseInt(alter_data.get(0)), transform(alter_data, true));
                }
            }

            modifier.dispose();
            refresh();
        } else if (source == modifierButtons[1]) {
            modifier.dispose();
        }
    }

    private void refresh() {
        parent.remove(content);

        content = new BasicSubmenuFrame(parent, "Client Menu");
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
