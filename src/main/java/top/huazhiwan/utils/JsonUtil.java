package top.huazhiwan.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static List<String> readAsStrings(FileReader reader) {
        try {
            return objectMapper.readValue(reader, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            return List.of();
        }
    }
}
