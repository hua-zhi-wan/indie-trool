package top.huazhiwan.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MultiLineCellRenderer implements TableCellRenderer {

    private final JTextArea textField = new JTextArea();

    public MultiLineCellRenderer() {
        textField.setBorder(null);
        textField.setBorder(new EmptyBorder(6, 0, 6, 0));
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        textField.setText(value.toString());
        table.setRowHeight(row, Math.max(textField.getPreferredSize().height + 8, table.getRowHeight(row)));
        textField.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        textField.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        return textField;
    }
}
