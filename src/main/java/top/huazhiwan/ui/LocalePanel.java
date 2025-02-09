package top.huazhiwan.ui;

import lombok.Getter;
import top.huazhiwan.Main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Getter
public class LocalePanel extends JPanel {
    private final DefaultListModel<String> localeModel = new DefaultListModel<>();

    public static LocalePanel instance = new LocalePanel();

    private LocalePanel() {
        super(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder("语种管理"));

        // 分组列表
        JList<String> locales = new JList<>(localeModel);
        this.add(new JScrollPane(locales), BorderLayout.CENTER);
        init();

        // 添加/删除按钮
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton addButton = new JButton("添加");
        JButton removeButton = new JButton("删除");

        addButton.addActionListener(e -> {
            String newLocale = JOptionPane.showInputDialog("添加新语种：");
            if (newLocale != null && !newLocale.trim().isEmpty()) {
                Main.project.getLocales().add(newLocale.trim());
                MainFrame.instance.edit(true);
                localeModel.addElement(newLocale.trim());
            }
        });

        removeButton.addActionListener(e -> {
            int selected = locales.getSelectedIndex();
            if (selected != -1) {
                Main.project.getLocales().remove(selected);
                MainFrame.instance.edit(true);
                localeModel.remove(selected);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }


    protected void init() {
        localeModel.clear();
        List<String> types = Main.project.getLocales();
        for (String type : types) {
            localeModel.addElement(type);
        }
    }
}
