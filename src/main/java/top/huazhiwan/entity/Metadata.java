package top.huazhiwan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.huazhiwan.Main;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metadata {
    private String name;
    private long updatedTime;
    private String version;
    private String defaultLocale;

    public static Metadata getDefault() {
        return Metadata.builder()
                .name("未命名")
                .defaultLocale("zh_CN")
                .updatedTime(System.currentTimeMillis())
                .version(Main.version)
                .build();
    }
}
