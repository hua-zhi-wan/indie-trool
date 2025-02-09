package top.huazhiwan.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MultiLineCellRenderer implements TableCellRenderer {

    private JTextArea textField = new JTextArea();

    public MultiLineCellRenderer() {
        textField.setBorder(null);
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        textField.setText(value.toString());
        table.setRowHeight(row, textField.getPreferredSize().height);
        textField.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        textField.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        return textField;
    }
}
