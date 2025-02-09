package top.huazhiwan.ui;

import top.huazhiwan.Main;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class MainFrame extends JFrame {

    private final String title = "Indie-Trool: 独立游戏本地化工具";
    private String fileName = "";

    public MainFrame() {
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        null, "确定要退出吗？", "退出",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setJMenuBar(MenuBar.instance);

        // 分离面板
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(300);

        // 左侧面板（元数据 + 分组）
        JSplitPane leftSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane leftSplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftSplitPane1.setDividerLocation(150);
        leftSplitPane2.setDividerLocation(300);
        leftSplitPane1.setTopComponent(MetadataPanel.instance);
        leftSplitPane1.setBottomComponent(leftSplitPane2);
        leftSplitPane2.setTopComponent(TypePanel.instance);
        leftSplitPane2.setBottomComponent(LocalePanel.instance);

        // 右侧面板（筛选器 + 表格）
        mainSplitPane.setLeftComponent(leftSplitPane1);
        mainSplitPane.setRightComponent(TranslationPanel.instance);

        add(mainSplitPane);
    }

    public void init() {
        File file = MenuBar.instance.getFile();
        if (file != null) {
            fileName = file.getName();
        }
        edit(false);
        MetadataPanel.instance.init();
        TypePanel.instance.init();
        LocalePanel.instance.init();
        TranslationPanel.instance.init();
    }

    public void edit(boolean flag) {
        if (flag) {
            if (!Main.edit_flag) {
                Main.edit_flag = true;
                setTitle("*" + fileName + " - " + title);
            }
        } else {
            Main.edit_flag = false;
            setTitle(fileName + " - " + title);
        }
    }

    public static final MainFrame instance = new MainFrame();
}
