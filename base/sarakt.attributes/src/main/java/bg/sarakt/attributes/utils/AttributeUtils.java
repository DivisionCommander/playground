/*
 * AttributeUtils.java
 *
 * created at 2024-02-11 by Roman Tsonev <roman.tsonev@yandex.ru>
 * 
 * Copyright (c) Roman Tsonev
 */

package bg.sarakt.attributes.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import bg.sarakt.base.Pair;
import bg.sarakt.base.Pair.PairImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Utility class
 */
@Component
public final class AttributeUtils {
    
    private static final String LEVEL_FILE = System.getProperty("user.dir") + "/src/main/resources/levels";
    
    @Bean
    public static Map<Integer, Long> fileLevelMap() {
        List<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(LEVEL_FILE))) {
            lines = br.lines().toList();
        } catch (IOException ex) {
            // fallback:
            return defaultLevelMap();
        }
        Map<Integer, Long> map = new HashMap<>();
        for (String line : lines) {
            Optional<Pair<Integer, Long>> parsed = safelyParse(line);
            if (parsed.isPresent()) {
                map.put(parsed.get().left(), parsed.get().right());
            }
        }
        return map;
    }
    
    private static Optional<Pair<Integer, Long>> safelyParse(String line) {
        if (line == null || line.trim().isBlank() || line.indexOf('=') == -1) {
            return Optional.empty();
        }
        try {
            String[] split = line.split("=");
            if (split.length < 2) {
                return Optional.empty();
            }
            int level = Integer.parseInt(split[0].trim());
            long exp = Long.parseLong(split[1].trim());
            return Optional.of(new PairImpl<>(level, exp));
        } catch (Exception e) {
            return Optional.empty();
            
        }
    }
    
    public static Map<Integer, Long> defaultLevelMap() {
        Map<Integer, Long> map = new HashMap<>();
        map.put(1, 0L);
        map.put(2, 10L);
        map.put(3, 20L);
        map.put(4, 35L);
        map.put(5, 50L);
        map.put(6, 74L);
        map.put(7, 100L);
        map.put(8, 143L);
        map.put(9, 180L);
        map.put(10, 250L);
        map.put(11, 350L);
        map.put(12, 500L);
        map.put(13, 700L);
        map.put(14, 1000L);
        map.put(15, 1800L);
        return map;
    }
}
