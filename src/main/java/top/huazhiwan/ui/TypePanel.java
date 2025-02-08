package top.huazhiwan.ui;

import lombok.Getter;
import top.huazhiwan.Main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Getter
public class TypePanel extends JPanel {
    private final DefaultListModel<String> typeModel = new DefaultListModel<>();

    public static TypePanel instance = new TypePanel();

    private TypePanel() {
        super(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder("分组管理"));

        // 分组列表
        JList<String> typeList = new JList<>(typeModel);
        this.add(new JScrollPane(typeList), BorderLayout.CENTER);
        init();

        // 添加/删除按钮
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton addButton = new JButton("添加");
        JButton editButton = new JButton("编辑");
        JButton removeButton = new JButton("删除");

        addButton.addActionListener(e -> {
            String newType = JOptionPane.showInputDialog("输入新分组名称：");
            if (newType != null && !newType.trim().isEmpty()) {
                typeModel.addElement(newType.trim());
            }
        });

        editButton.addActionListener(e -> {
            if (typeList.isSelectionEmpty())
                return;
            String newType = JOptionPane.showInputDialog("更改分组名称：", typeList.getSelectedValue());
            if (newType != null && !newType.trim().isEmpty()) {
                typeModel.set(typeList.getSelectedIndex(), newType.trim());
            }
        });

        removeButton.addActionListener(e -> {
            int selected = typeList.getSelectedIndex();
            if (selected != -1) {
                typeModel.remove(selected);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void init() {
        typeModel.clear();
        List<String> types = Main.project.getTypes();
        for (String type : types) {
            typeModel.addElement(type);
        }
    }
}
