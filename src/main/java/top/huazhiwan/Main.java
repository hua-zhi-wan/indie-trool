package top.huazhiwan;


import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import top.huazhiwan.entity.Project;
import top.huazhiwan.ui.MainFrame;

import javax.swing.*;

public class Main extends JFrame {

    public static final String version = "0.1.1";
    public static Project project = Project.getDefault();


    public static void main(String[] args) {
        FlatArcDarkIJTheme.setup();
        SwingUtilities.invokeLater(() -> {
            MainFrame.instance.setVisible(true);
        });
    }
}