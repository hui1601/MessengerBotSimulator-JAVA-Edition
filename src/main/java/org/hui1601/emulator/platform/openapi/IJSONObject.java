package org.hui1601.emulator.platform.openapi;

import org.hui1601.emulator.managers.FileManager;
import org.hui1601.emulator.platform.application.views.dialogs.ShowErrorDialog;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

public class IJSONObject extends JSONObject {
    public IJSONObject() {
        super();
    }

    public IJSONObject(File file) throws MalformedURLException {
        try {
            this.putAll((JSONObject) new JSONParser().parse(FileManager.read(file)));
        } catch (Exception e) {
            new ShowErrorDialog(e).display();
        }
    }

    public JSONObject getJSONObject(String type) {
        return (JSONObject) this.get(type);
    }

    public double getDouble(String type) {
        return Double.valueOf("" + this.get(type));
    }

    public Object getObject(Object type) {
        return this.get(type);
    }

    public String getString(String type) {
        return String.valueOf("" + this.get(type));
    }

    public int getInt(String type) {
        return Integer.valueOf("" + get(type));
    }

    public boolean getBoolean(String type) {
        return Boolean.valueOf("" + get(type));
    }

    public void putString(String type, String data) throws MalformedURLException {
        this.put(type, data);
    }

    public void putDouble(String type, double data) {
        this.put(type, data);
    }

    public void putInt(String type, int data) throws MalformedURLException {
        this.put(type, data);
    }

    public void putBoolean(String type, boolean data) throws MalformedURLException {
        this.put(type, data);
    }

    public void putMap(Map map) throws MalformedURLException {
        this.putAll(map);
    }

    public String toBeautifyString() {
        String json = this.toJSONString();

        StringBuilder builder = new StringBuilder();
        int len = json.length(), i = 0;
        String tab = "";
        char data;
        boolean beginEnd = true;

        for (i = 0; i < len; i++) {
            data = json.charAt(i);

            switch (data) {
                case '{':
                case '[': {
                    builder.append(data);

                    if (beginEnd) {
                        tab += "\t";
                        builder.append("\n");
                        builder.append(tab);
                    }

                    break;
                }
                case '}':
                case ']': {
                    if (beginEnd) {
                        tab = tab.substring(0, tab.length() - 1);
                        builder.append("\n");
                        builder.append(tab);
                    }

                    builder.append(data);
                    break;
                }
                case '"': {
                    if (json.charAt(i - 1) != '\\') {
                        beginEnd = !beginEnd;
                    }

                    builder.append(data);
                    break;
                }
                case ',': {
                    builder.append(data);

                    if (beginEnd) {
                        builder.append("\n");
                        builder.append(tab);
                    }

                    break;
                }
                default: {
                    builder.append(data);
                }
            }
        }

        return builder.toString();
    }
}