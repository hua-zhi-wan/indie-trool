package top.huazhiwan.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Project {
    private Metadata metaData = new Metadata();
    private List<String> locales = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private List<Word> words = new ArrayList<>();

    public static Project getDefault() {
        Project project = new Project();
        project.metaData = Metadata.getDefault();
        project.locales = new ArrayList<>() {{
            add("zh_CN");
            add("en");
        }};
        project.types = new ArrayList<>() {{
            add("属性");
            add("道具");
        }};
        project.words = new ArrayList<>() {{
            add(Word.builder().raw("攻击").locale("zh_CN").type("属性")
                    .translations(new HashMap<>() {{
                        put("en", "Attack");
                    }}).build());
            add(Word.builder().raw("体力").locale("zh_CN").type("属性")
                    .translations(new HashMap<>() {{
                        put("en", "HitPoint");
                    }}).build());
            add(Word.builder().raw("苹果").locale("zh_CN").type("道具")
                    .translations(new HashMap<>() {{
                        put("en", "Apple");
                    }}).deprecated(true).build());
            add(Word.builder().raw("香蕉").locale("zh_CN").type("属性")
                    .translations(new HashMap<>() {{
                        put("en", "Banana");
                    }}).build());
        }};
        return project;
    }
}
