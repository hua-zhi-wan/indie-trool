package top.huazhiwan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    private String raw;
    private String locale;
    @Builder.Default
    private String type = "";
    @Builder.Default
    private Map<String, String> translations = new HashMap<>();
    private long time;
    @Builder.Default
    private boolean deprecated = false;

    public String getTranslation(String locale) {
        if (this.locale.equals(locale)) {
            return raw;
        }
        return translations.getOrDefault(locale, "");
    }

    public void setTranslation(String locale, String content) {
        if (this.locale.equals(locale)) {
            return;
        }
        translations.put(locale, content);
    }
}
