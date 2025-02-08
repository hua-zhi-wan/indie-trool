package top.huazhiwan.ui;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Indie-Trool: 独立游戏本地化工具");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public static final MainFrame instance = new MainFrame();
}
