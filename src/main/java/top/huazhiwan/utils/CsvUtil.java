package top.huazhiwan.utils;

import top.huazhiwan.Main;
import top.huazhiwan.entity.Word;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static void dump(FileWriter writer) {
        try (BufferedWriter writer1 = new BufferedWriter(writer)) {
            // 写入表头
            var locales = Main.project.getLocales();
            writer1.write("id," + String.join(",", locales));
            writer1.newLine();
            for (Word word : Main.project.getWords()) {
                List<String> list = new ArrayList<>();
                list.add(word.getRaw());
                for (String locale : locales) {
                    list.add(word.getTranslation(locale));
                }
                writer1.write(String.join(",", list.stream().map(s -> '"' + s + '"').toList()));
                writer1.newLine();
            }
        } catch (IOException exception) {
            return;
        }
    }
}
