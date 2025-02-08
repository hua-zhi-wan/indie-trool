package top.huazhiwan.ui;

import top.huazhiwan.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetadataPanel extends JPanel {

    public static final MetadataPanel instance = new MetadataPanel();

    private DefaultTableModel tableModel;

    private MetadataPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("元数据"));

        String[] columns = new String[]{"元数据键", "值"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col > 0 && row < 2;
            }
        };

        init();
        JTable table = new JTable(tableModel);
        add(table);
    }

    private void init() {
        var metadata = Main.project.getMetaData();
        tableModel.addRow(new Object[]{"项目名称", metadata.getName()});
        tableModel.addRow(new Object[]{"词元语种", metadata.getDefaultLocale()});
        tableModel.addRow(new Object[]{"更新时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(metadata.getUpdatedTime()))});
        tableModel.addRow(new Object[]{"工具版本", metadata.getVersion()});
    }
}
