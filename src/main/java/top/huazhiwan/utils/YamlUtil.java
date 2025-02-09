package top.huazhiwan.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import top.huazhiwan.Main;
import top.huazhiwan.entity.Project;

import java.io.FileReader;
import java.io.FileWriter;

public class YamlUtil {
    private static final Yaml yaml;

    static {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setPrettyFlow(true);
        Representer representer = new Representer(dumperOptions);
        representer.addClassTag(Project.class, Tag.MAP);
        yaml = new Yaml(representer);
    }

    public static void dump(FileWriter writer) {
        Main.project.getMetaData().setUpdatedTime(System.currentTimeMillis());
        yaml.dump(Main.project, writer);
    }

    public static void load(FileReader reader) {
        Main.project = yaml.loadAs(reader, Project.class);
    }
}
