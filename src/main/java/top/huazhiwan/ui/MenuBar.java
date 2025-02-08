package top.huazhiwan.ui;

import javax.swing.*;
import java.io.File;

public class MenuBar extends JMenuBar {

    public static final MenuBar instance = new MenuBar();

    private MenuBar() {

        JMenu fileMenu = new JMenu("文件");
        JMenuItem newProjectItem = new JMenuItem("新项目");
        fileMenu.add(newProjectItem);
        JMenuItem loadProjectItem = new JMenuItem("读取");
        fileMenu.add(loadProjectItem);
        JMenuItem saveProjectItem = new JMenuItem("保存");
        fileMenu.add(saveProjectItem);
        JMenuItem saveAsProjectItem = new JMenuItem("另存为");
        fileMenu.add(saveAsProjectItem);
        fileMenu.add(new JSeparator());
        JMenuItem importWords = new JMenuItem("导入词元");
        fileMenu.add(importWords);
        JMenuItem clearDeprecatedWords = new JMenuItem("清除弃用词元");
        fileMenu.add(clearDeprecatedWords);
        add(fileMenu);
        loadProjectItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                System.out.println(file);
            }
        });


        JMenu exportMenu = new JMenu("导出");
        JMenuItem exportCSVItem = new JMenuItem("导出csv");
        exportMenu.add(exportCSVItem);
        add(exportMenu);
    }
}
