package top.huazhiwan.ui;

import top.huazhiwan.Main;
import top.huazhiwan.entity.Word;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TranslationPanel extends JPanel {

    private JComboBox<String> typeFilterCombo;
    private JComboBox<String> localeFilterCombo;
    private JCheckBox hideDeprecatedCheck;
    private DefaultTableModel tableModel;
    private TableRowSorter<TableModel> sorter;
    private DefaultComboBoxModel<String> typeComboBoxModel = new DefaultComboBoxModel<>();

    public static final TranslationPanel instance = new TranslationPanel();

    private TranslationPanel() {
        super(new BorderLayout());
        this.add(createFilterPanel(), BorderLayout.NORTH);
        this.add(new JScrollPane(createDataTable()), BorderLayout.CENTER);
    }

    // 新增筛选面板
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("筛选条件"));

        // 语种筛选
        panel.add(new JLabel("目标语种："));
        localeFilterCombo = new JComboBox<>();
        updateLocaleFilter();
        localeFilterCombo.addActionListener(e -> updateTranslations());
        localeFilterCombo.setSelectedIndex(1);
        panel.add(localeFilterCombo);

        // 类型筛选
        panel.add(new JLabel("类型筛选："));
        typeFilterCombo = new JComboBox<>();
        updateTypeFilter();
        typeFilterCombo.addActionListener(e -> updateAllFilter());
        panel.add(typeFilterCombo);

        // 弃用条目筛选
        hideDeprecatedCheck = new JCheckBox("隐藏弃用条目");
        hideDeprecatedCheck.addActionListener(e -> updateAllFilter());
        panel.add(hideDeprecatedCheck);

        return panel;
    }

    // 更新类型筛选选项
    private void updateTypeFilter() {
        typeFilterCombo.removeAllItems();
        typeFilterCombo.addItem("全部");
        typeFilterCombo.addItem("未分类");
        var typeModel = TypePanel.instance.getTypeModel();
        for (int i = 0; i < typeModel.size(); i++) {
            typeFilterCombo.addItem(typeModel.get(i));
        }
    }

    // 更新语种筛选选项
    private void updateLocaleFilter() {
        localeFilterCombo.removeAllItems();
        var localeModel = LocalePanel.instance.getLocaleModel();
        for (int i = 0; i < localeModel.size(); i++) {
            localeFilterCombo.addItem(localeModel.get(i));
        }
    }

    // 数据表格
    private JTable createDataTable() {
        String[] columms = {"ID", "词元", "译文", "分类", "弃用"};

        tableModel = new DefaultTableModel(columms, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return Set.of(1, 2, 3, 4).contains(col);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 4 -> Boolean.class;
                    default -> String.class;
                };
            }
        };

        // 模型更改同步到实体
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column < 0) {
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0) - 1;
            Object value = tableModel.getValueAt(row, column);
            switch (column) {
                case 2:
                    String locale = (String) localeFilterCombo.getSelectedItem();
                    Main.project.getWords().get(id).setTranslation(locale, (String) value);
                    MainFrame.instance.edit(true);
                    break;
                case 3:
                    Main.project.getWords().get(id).setType((String) value);
                    MainFrame.instance.edit(true);
                    break;
                case 4:
                    Main.project.getWords().get(id).setDeprecated((boolean) value);
                    MainFrame.instance.edit(true);
                    break;
            }
        });
        reloadData();

        JTable table = new JTable(tableModel);
        table.setShowGrid(true);
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        // 为翻译列设置多行编辑器与渲染器
        TableColumn column;
        column = table.getColumnModel().getColumn(2);
        column.setCellEditor(new MultiLineCellEditor(true));
        column.setCellRenderer(new MultiLineCellRenderer());
        column = table.getColumnModel().getColumn(1);
        column.setCellEditor(new MultiLineCellEditor(false));
        column.setCellRenderer(new MultiLineCellRenderer());


        // 使用共享的 comboBoxModel
        JComboBox<String> typeComboBox = new JComboBox<>(typeComboBoxModel);
        updateTypeComboBoxModel();
        TableColumn typeColumn = table.getColumnModel().getColumn(3);
        typeColumn.setCellEditor(new DefaultCellEditor(typeComboBox));

        // 监听分组变化
        var typeModel = TypePanel.instance.getTypeModel();
        typeModel.addListDataListener(new ListDataListener() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                updateComponents();
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                updateComponents();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                updateComponents();
            }

            private void updateComponents() {
                updateTypeFilter();
                typeColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(typeModel.toArray())));
                updateTypeComboBoxModel();
                // 触发表格刷新
                table.repaint();
            }
        });

        // 监听语种变化
        var localeModel = LocalePanel.instance.getLocaleModel();
        localeModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                updateComponent();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                updateComponent();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                updateComponent();
            }

            private void updateComponent() {
                updateLocaleFilter();
            }
        });

        return table;
    }

    private void updateTypeComboBoxModel() {
        // 同步更新下拉框模型
        typeComboBoxModel.removeAllElements();
        var typeModel = TypePanel.instance.getTypeModel();
        for (int i = 0; i < typeModel.size(); i++) {
            typeComboBoxModel.addElement(typeModel.getElementAt(i));
        }
    }

    // 更新筛选条件
    private void updateAllFilter() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // 类型筛选
        String selectedType = (String) typeFilterCombo.getSelectedItem();
        if (selectedType != null) {
            if (!selectedType.equals("全部")) {
                if (selectedType.equals("未分类")) {
                    filters.add(RowFilter.regexFilter("^$"));
                } else {
                    filters.add(RowFilter.regexFilter("^" + selectedType + "$", 3));
                }
            }
        }

        // 弃用筛选
        if (hideDeprecatedCheck.isSelected()) {
            filters.add(new RowFilter<>() {
                public boolean include(Entry<?, ?> entry) {
                    Object value = entry.getValue(4);
                    return value != null && !Boolean.TRUE.equals(value);
                }
            });
        }

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void updateTranslations() {
        String locale = (String) localeFilterCombo.getSelectedItem();
        if (tableModel == null) {
            return;
        }
        for (int i = 0; i < tableModel.getRowCount(); ++i) {
            Word word = Main.project.getWords().get(i);
            tableModel.setValueAt(word.getTranslation(locale), i, 2);
        }
    }


    private Object[] convertWord(int index) {
        Word word = Main.project.getWords().get(index);
        String locale = (String) localeFilterCombo.getSelectedItem();
        return new Object[]{
                index + 1, word.getRaw(),
                word.getTranslation(locale),
                word.getType(), word.isDeprecated(),
        };
    }

    private void reloadData() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < Main.project.getWords().size(); ++i) {
            tableModel.addRow(convertWord(i));
        }
    }

    protected void init() {
        reloadData();
    }
}
