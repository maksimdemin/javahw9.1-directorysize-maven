import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class DirectorySize {

    private static File folder;
    private static final String DIR_PATH = "/Volumes/Transcend/Java/Books Java";
    public static void main(String[] args) throws IOException {

        folder = new File(DIR_PATH);
        long sizeDir; // переменная для вычисления размера папки в байтах


        // Вариант №1
        System.out.println("Version №1");
        sizeDir = getDirSize(folder);
        try {
            System.out.println(humanFileSize(sizeDir));
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        }


        // Вариант №2 (используя commons-io)
        System.out.println("\nVersion №2 (use commons-io)");
        try {
            System.out.println(humanFileSize(FileUtils.sizeOfDirectory(folder)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // этот вариант сразу возвращает размер папки в удобочитаемом формате, но преобразован через 1024
        System.out.println("This version immediately returns a readable format: " + FileUtils.byteCountToDisplaySize(getDirSize(folder)));


        // Вариант №3 (используя класс Files, используем библиотеку java.nio.file.Files)
        System.out.println("\nVersion №3 (use java.nio.file.Files)");


        try {
            sizeDir = Files.walk(Paths.get(DIR_PATH)).map(Path::toFile).filter(File::isFile).mapToLong(File::length).sum();
            System.out.println(humanFileSize(sizeDir));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private static String humanFileSize(long sizeDir) {
        String[] lettersForSize = {"B", "kB", "MB", "GB", "TB"};
        int i = (int) Math.floor(Math.log10(sizeDir) / Math.log10(1000));
        if (i < 0 || i > lettersForSize.length) {  // если папка не существует или путь некорректный
            throw new ArrayIndexOutOfBoundsException("Directory does not exist");
        }
        return String.format("Directory <%s> has size: %.2f %s", folder.getName(), sizeDir / Math.pow(1000, i), lettersForSize[i]);

    }


    private static boolean isValidDir(File dir) {  // метод проверки на валидность каталога
        return dir != null && dir.exists() && dir.isDirectory();
    }


    private static long getDirSize(File dir) { // вычисление размера каталога
        if (!isValidDir(dir)) {
//            System.out.println("This folder is invalid.");
            return 0L;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return 0L;
        } else {
            long size = 0;
            for (File file : files) {
                if (file.canRead()) {
                    if (file.isFile()) {
                        size += file.length();
                    } else {
                        size += getDirSize(file);
                    }
                } else System.out.println("This directory not readable -> " + file.getAbsolutePath());
            }
            return size;
        }
    }
}