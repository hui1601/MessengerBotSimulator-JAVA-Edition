package org.hui1601.emulator.managers;

import org.hui1601.emulator.platform.application.views.dialogs.ShowErrorDialog;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileManager {
    final public static File PROGRAM_FOLDER = new File(System.getProperty("user.dir"));
    final public static File DATA_FOLDER = new File(PROGRAM_FOLDER + "/data");
    final public static File BOTS_FOLDER = new File(PROGRAM_FOLDER + "/data/bots/");

    // ex.js -> ex
    public static String getFileBaseName(String name) {
        return (name.contains(".")) ? name.substring(0, name.lastIndexOf(".")) : name;
    }

    // ex.js -> js
    public static String getFileExtension(String name) {
        return (name.contains(".")) ? name.substring(name.lastIndexOf(".") + 1) : name;
    }

    public static File getDataFile(String name) {
        return new File(DATA_FOLDER + File.separator + name);
    }

    public static String[] getBotNames() {
        File[] files = BOTS_FOLDER.listFiles(File::isDirectory);
        assert files != null;
        String[] names = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }

        return names;
    }

    public static File getBotFolder(String name) {
        return new File(BOTS_FOLDER + File.separator + getFileBaseName(name));
    }

    public static File getBotScript(String name) {
        return new File(getBotFolder(name).getPath() + File.separator + "index.js");
    }

    public static File getBotSetting(String name) {
        return new File(getBotFolder(name).getPath() + File.separator + "bot.json");
    }

    public static File getBotLog(String name) {
        return new File(getBotFolder(name).getPath() + File.separator + "log.json");
    }

    public static String save(File file, String content) {
        try {
            file.createNewFile();

            if (content != null) {
                if (!content.substring(content.length() - 1).equals(System.lineSeparator())) {
                    content += System.getProperty("line.separator");
                }
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8));
            assert content != null;
            writer.write(content);
            writer.close();

            return content;
        } catch (Exception e) {
            new ShowErrorDialog(e).display();
        }

        return null;
    }

    public static String append(File file, String content) throws MalformedURLException {
        try {
            file.createNewFile();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
            writer.write(content);
            writer.close();

            return content;
        } catch (Exception e) {
            new ShowErrorDialog(e).display();
        }

        return null;
    }

    public static String read(File file) {
        try {
            if (!file.exists()) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line = "";
            StringBuilder text = new StringBuilder(reader.readLine());

            while ((line = reader.readLine()) != null) {
                text.append("\n").append(line);
            }

            reader.close();

            return text.toString();
        } catch (Exception e) {
            new ShowErrorDialog(e).display();
        }

        return null;
    }

    public static boolean remove(File file) {
        try {
            if (!file.exists()) {
                return false;
            }

            if (file.isDirectory()) {
                // 폴더는 안의 파일들 제거하고 폴더를 제거해야 함.
                for (File data : Objects.requireNonNull(file.listFiles())) {
                    data.delete();

                    if (data.isFile()) {
                        data.delete();
                    } else {
                        remove(data);
                    }
                }

                return file.delete();
            }

            return file.delete();
        } catch (Exception e) {
            new ShowErrorDialog(e).display();
        }

        return false;
    }
}