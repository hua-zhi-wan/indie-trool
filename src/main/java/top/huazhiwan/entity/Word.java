package top.huazhiwan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Word {
    private String raw;
    private String locale;
    private String type;
    private Map<String, String> translations = new HashMap<>();
    private long time = System.currentTimeMillis();
    private boolean deprecated = false;

    public String getTranslation(String locale) {
        if (this.locale.equals(locale)) {
            return raw;
        }
        return translations.getOrDefault(locale, "");
    }
}
