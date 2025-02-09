package top.huazhiwan.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MultiLineCellEditor extends AbstractCellEditor implements TableCellEditor {
    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(textArea);

    public MultiLineCellEditor(boolean editable) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(editable);
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                stopCellEditing();
            }
        });
        scrollPane.setBorder(null);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textArea.setText((String) value);
        return scrollPane;
    }

    @Override
    public Object getCellEditorValue() {
        return textArea.getText();
    }
}
