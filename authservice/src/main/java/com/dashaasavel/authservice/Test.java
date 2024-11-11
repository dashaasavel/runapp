package com.dashaasavel.authservice;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    private static final Pattern FILE_PATTERN = Pattern.compile("(\\d+)_");
    static List<Path> paths = Arrays.asList(
            Paths.get("1_3DS/1_positive/2_xxx.txt"),
            Paths.get("1_3DS/1_positive/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/1_checking/2_pareq/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/1_checking/1_vereq/2_xxx.txt"),
            Paths.get("1_3DS/2_negative/1_checking/1_vereq/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/1_checking/2_pareq/2_xxx.txt"),
            Paths.get("1_3DS/2_negative/2_incorrect/2_xxx.txt"),
            Paths.get("1_3DS/2_negative/2_incorrect/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/3_incorrect/1_tag/2_xxx.txt"),
            Paths.get("1_3DS/2_negative/3_incorrect/1_tag/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/3_incorrect/1_tag/3_xxx.txt"),
            Paths.get("1_3DS/2_negative/3_incorrect/2_in_values/2_xxx.txt"),
            Paths.get("1_3DS/2_negative/3_incorrect/2_in_values/3_xxx.txt"),
            Paths.get("1_3DS/2_negative/3_incorrect/2_in_values/1_xxx.txt"),
            Paths.get("2_3DS/1_positive/2_xxx.txt"),
            Paths.get("13_3DS/1_positive/2_xxx.txt"),
            Paths.get("2_3DS/1_positive/1_xxx.txt"),
            Paths.get("2_3DS/2_negative/1_checking/2_pareq/1_xxx.txt"),
            Paths.get("2_3DS/2_negative/1_checking/1_vereq/2_xxx.txt"),
            Paths.get("2_3DS/2_negative/1_checking/1_vereq/1_xxx.txt"),
            Paths.get("2_3DS/2_negative/1_checking/2_pareq/2_xxx.txt"),
            Paths.get("2_3DS/2_negative/2_incorrect/2_xxx.txt"),
            Paths.get("2_3DS/2_negative/2_incorrect/1_xxx.txt"),
            Paths.get("2_3DS/2_negative/3_incorrect/1_tag/2_xxx.txt"),
            Paths.get("2_3DS/2_negative/3_incorrect/1_tag/1_xxx.txt"),
            Paths.get("2_3DS/2_negative/3_incorrect/1_tag/3_xxx.txt"),
            Paths.get("2_3DS/2_negative/3_incorrect/2_in_values/2_xxx.txt"),
            Paths.get("2_3DS/2_negative/3_incorrect/2_in_values/3_xxx.txt"),
            Paths.get("2_3DS/2_negative/3_incorrect/2_in_values/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/10_checking/1_vereq/3_xxx.txt"),
            Paths.get("1_3DS/2_negative/10_checking/1_vereq/2_xxx.txt"),
            Paths.get("1_3DS/2_negative/10_checking/1_vereq/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/11_checking/2_pareq/10_xxx.txt"),
            Paths.get("1_3DS/2_negative/11_checking/2_pareq/1_xxx.txt"),
            Paths.get("1_3DS/2_negative/11_checking/2_pareq/15_xxx.txt"),
            Paths.get("1_3DS/2_negative/20_incorrect/30_xxx.txt"),
            Paths.get("1_3DS/2_negative/20_incorrect/40_xxx.txt"),
            Paths.get("1_3DS/2_negative/20_incorrect/4_xxx.txt"),
            Paths.get("1_3DS/2_negative/21_incorrect/3_xxx.txt"),
            Paths.get("2_3DS/2_negative/10_checking/1_vereq/3_xxx.txt"),
            Paths.get("2_3DS/2_negative/11_checking/2_pareq/3_xxx.txt"),
            Paths.get("2_3DS/2_negative/20_incorrect/3_xxx.txt"),
            Paths.get("2_3DS/2_negative/21_incorrect/3_xxx.txt"),
            Paths.get("2_3DS/2_negative/30_incorrect/3_xxx.txt")
    );

    public static void main(String[] args) {
        // Компаратор для сортировки по префиксу (например, 1_3DS перед 2_3DS)
        Comparator<Path> prefixComparator = Comparator.comparing(path -> {
            String fileName = path.getName(0).toString();
            return fileName.split("_")[0];  // Берем только первую часть (например, "1" или "2")
        });

        // Компаратор для сортировки по частям пути (лексикографическое сравнение директорий)
        Comparator<Path> pathPartsComparator = Comparator.comparing(Path::toString);

        // Компаратор для сортировки по числовым значениям в имени файла
        Comparator<Path> fileNameComparator = Comparator.comparingInt(path -> {
            String fileName = path.getFileName().toString();
            Matcher matcher = FILE_PATTERN.matcher(fileName);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
            return Integer.MAX_VALUE; // если нет числа, ставим в конец
        });

        // Компаратор, который сначала сортирует по префиксу, затем по частям пути, а затем по имени файла
//        Comparator<Path> pathComparator = prefixComparator
        Comparator<Path> pathComparator = prefixComparator
                .thenComparing(pathPartsComparator) // Добавляем сортировку по частям пути
                .thenComparing(fileNameComparator);

        // Сортировка путей
        List<Path> sortedPaths = paths.stream()
                .sorted(pathComparator)
                .toList();

        // Вывод отсортированных путей
//        sortedPaths.forEach(System.out::println);

        paths.sort((p1, p2) -> {
            // Разбиваем пути на части (директории и файл)
            String[] parts1 = p1.toString().split("/");
            String[] parts2 = p2.toString().split("/");

            int minLength = Math.min(parts1.length, parts2.length);

            // Сравниваем части поочередно
            for (int i = 0; i < minLength; i++) {
                String part1 = parts1[i];
                String part2 = parts2[i];

                // Если это папка, сравниваем по числовым префиксам
                if (isNumericPrefix(part1) && isNumericPrefix(part2)) {
                    int num1 = Integer.parseInt(part1.split("_")[0]);
                    int num2 = Integer.parseInt(part2.split("_")[0]);
                    if (num1 != num2) {
                        return Integer.compare(num1, num2);
                    }
                } else {
                    // Сравниваем строки для других случаев (например, имен файлов)
                    int result = part1.compareTo(part2);
                    if (result != 0) {
                        return result;
                    }
                }
            }

            // Если одна строка короче другой (например, один путь — это папка, а другой — файл)
            return Integer.compare(parts1.length, parts2.length);
        });
        paths.forEach(System.out::println);
    }
    private static boolean isNumericPrefix(String part) {
        return part.matches("^\\d+_.*");
    }

}
