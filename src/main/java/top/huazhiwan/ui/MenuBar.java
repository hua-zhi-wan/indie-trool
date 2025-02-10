package top.huazhiwan.ui;

import lombok.Getter;
import top.huazhiwan.Main;
import top.huazhiwan.entity.Project;
import top.huazhiwan.entity.Word;
import top.huazhiwan.utils.CsvUtil;
import top.huazhiwan.utils.JsonUtil;
import top.huazhiwan.utils.YamlUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuBar extends JMenuBar {
    @Getter
    private File file;
    public static final MenuBar instance = new MenuBar();

    private MenuBar() {

        JMenu fileMenu = new JMenu("项目");
        JMenuItem newProjectItem = new JMenuItem("新项目");
        fileMenu.add(newProjectItem);
        newProjectItem.addActionListener(e -> {
            Main.project = Project.getDefault();
            MainFrame.instance.init();
        });

        JMenuItem loadProjectItem = new JMenuItem("读取");
        fileMenu.add(loadProjectItem);
        loadProjectItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("读取项目");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("项目文件（.trool）", "trool"));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                try (FileReader reader = new FileReader(file)) {
                    YamlUtil.load(reader);
                    MainFrame.instance.init();
                    MainFrame.instance.edit(false);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "错误：" + ex.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem saveProjectItem = new JMenuItem("保存");
        fileMenu.add(saveProjectItem);
        saveProjectItem.addActionListener(e -> {
            if (file == null) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setSelectedFile(new File("project1.trool"));
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                }
            }
            try (FileWriter writer = new FileWriter(file)) {
                YamlUtil.dump(writer);
                MetadataPanel.instance.init();
                MainFrame.instance.edit(false);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(this,
                        "错误：" + exception.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        });


        JMenuItem saveAsProjectItem = new JMenuItem("另存为");
        fileMenu.add(saveAsProjectItem);
        saveAsProjectItem.addActionListener(e -> {
            File tmpFile;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new File("project1.trool"));
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                tmpFile = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(tmpFile)) {
                    YamlUtil.dump(writer);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(this,
                            "错误：" + exception.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(fileMenu);

        JMenu wordMenu = new JMenu("词元");
        JMenuItem addSingleWord = new JMenuItem("添加词元");
        addSingleWord.addActionListener(e -> {
            String str = JOptionPane.showInputDialog("添加词元");
            if (str != null && !str.trim().isEmpty()) {
                String locale = Main.project.getMetaData().getDefaultLocale();
                Word newWord = Word.builder().raw(str).locale(locale).time(System.currentTimeMillis()).build();
                Main.project.getWords().add(newWord);
                TranslationPanel.instance.init();
            }
        });
        wordMenu.add(addSingleWord);

        JMenuItem importWordsFromJson = new JMenuItem("从JSON导入词元");
        importWordsFromJson.addActionListener(e -> {
            File tmpFile;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON文件", "json"));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                tmpFile = fileChooser.getSelectedFile();
                String locale = Main.project.getMetaData().getDefaultLocale();
                try (FileReader reader = new FileReader(tmpFile)) {
                    List<String> strings = JsonUtil.readAsStrings(reader);
                    Set<String> set = Main.project.getWords().stream().map(Word::getRaw).collect(Collectors.toSet());
                    List<Word> words = strings.stream()
                            .filter(s -> !set.contains(s))
                            .map(s -> Word.builder()
                                    .raw(s).locale(locale)
                                    .time(System.currentTimeMillis()).build())
                            .toList();
                    Main.project.getWords().addAll(words);
                    TranslationPanel.instance.init();
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(this,
                            "错误：" + exception.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        wordMenu.add(importWordsFromJson);

        JMenuItem clearDeprecatedWords = new JMenuItem("清除弃用词元");
        clearDeprecatedWords.addActionListener(e -> {
            List<Word> newWords = Main.project.getWords();
            newWords = newWords.stream().filter(w -> !w.isDeprecated()).toList();
            newWords = new ArrayList<>(newWords);
            Main.project.setWords(newWords);
            TranslationPanel.instance.init();
        });
        wordMenu.add(clearDeprecatedWords);
        add(wordMenu);


        JMenu exportMenu = new JMenu("生成");
        JMenuItem exportCSVItem = new JMenuItem("生成csv");
        exportCSVItem.addActionListener(e -> {
            File tmpFile;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new File(Main.project.getMetaData().getName() + ".csv"));
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                tmpFile = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(tmpFile)) {
                    CsvUtil.dump(writer);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(this,
                            "错误：" + exception.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        exportMenu.add(exportCSVItem);
        add(exportMenu);
    }
}
