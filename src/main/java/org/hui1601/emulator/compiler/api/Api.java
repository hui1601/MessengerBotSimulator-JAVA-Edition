package org.hui1601.emulator.compiler.api;

import org.hui1601.emulator.compiler.engine.ScriptEngine;
import org.hui1601.emulator.compiler.engine.ScriptManager;
import org.hui1601.emulator.managers.BotManager;
import org.hui1601.emulator.managers.FileManager;
import org.hui1601.emulator.platform.application.views.actions.AddChatMessageAction;
import org.hui1601.emulator.platform.application.views.actions.ShowNotificationAction;
import org.hui1601.emulator.platform.application.views.actions.ShowToastMessageAction;
import org.hui1601.emulator.settings.Settings;
import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.JSFunction;

import java.io.File;
import java.util.ArrayList;

public class Api extends ScriptableObject {
    final String bot_name;

    public Api(ScriptableObject object, String name) {
        super(object, object.getPrototype());

        this.bot_name = name;
    }

    private String getScriptName(String name) {
        return name.endsWith(".js") ? name.substring(0, name.lastIndexOf(".")) : name;
    }

    @Override
    public String getClassName() {
        return "Api";
    }

    @JSFunction
    public Object getContext() {
        return null;
    }

    @JSFunction
    public Boolean on(Object name) {
        if (!Undefined.isUndefined(name)) { //'undefined' 작업완료
            String name_string = getScriptName((String) name);
            try {
                BotManager.setPower(name_string, true);
            } catch (Exception e) {
                return false;
            }
        } else {
            for (String script : FileManager.getBotNames()) {
                BotManager.setPower(script, true);
            }
        }

        return true;
    }

    @JSFunction
    public Boolean off(Object name) {
        if(!Undefined.isUndefined(name)) { //'undefined' 작업완료
            String name_string = getScriptName((String) name);
            try {
                BotManager.setPower(name_string, false);
            } catch (Exception e) {
                return false;
            }
        } else {
            for (String script : FileManager.getBotNames()) {
                BotManager.setPower(script, false);
            }
        }
        return true;
    }

    @JSFunction
    public Boolean reload(Object name, Object stopOnError) {
        if(Undefined.isUndefined(stopOnError)) stopOnError = true; //'undefined' 작업완료
        if(!(stopOnError instanceof Boolean)) return false;
        if(!Undefined.isUndefined(name)) { //'undefined' 작업완료
            String name_string = getScriptName((String) name);
            if (!FileManager.getBotScript(name_string).exists()) {
                return false;
            }
            return ScriptManager.setInitialize(name_string, false, !((Boolean) stopOnError));
        } else {
            ScriptManager.allInitialize(false);
            return true;
        }
    }

    @JSFunction
    public Boolean compile(Object name, Object stopOnError) {
        return reload(name, stopOnError);
    }

    @JSFunction
    public int prepare(String name) {
        return 0;
    }

    @JSFunction
    public Boolean unload(Object name) {
        if (Undefined.isUndefined(name)) { //'undefined' 작업완료
            return false;
        }
        String name_string = getScriptName((String) name);

        if (!ScriptEngine.container.containsKey(name_string)) {
            return false;
        }

        ScriptEngine.container.remove(name_string);

        return true;
    }

    @JSFunction
    public Boolean isOn(Object name) {
        if(Undefined.isUndefined(name)) //'undefined' 작업완료
            Settings.getScriptSetting(bot_name).getBoolean("power");
        return Settings.getScriptSetting((String) name).getBoolean("power");
    }

    @JSFunction
    public Boolean isCompiled(Object name) {
        if(Undefined.isUndefined(name))//'undefined' 작업완료
            return ScriptEngine.container.get(getScriptName(bot_name)) != null;
        return ScriptEngine.container.get(getScriptName((String) name)) != null;
    }

    @JSFunction
    public Boolean isCompiling(Object name) {
        if (Undefined.isUndefined(name)) { //'undefined' 작업완료
            for (String script : FileManager.getBotNames()) {
                if (ScriptEngine.compiling.get(script) != null) {
                    return true;
                }
            }
            return false;
        }
        String name_string = getScriptName((String) name);

        if (ScriptEngine.compiling.get(name_string) == null) {
            return false;
        }
        return ScriptEngine.compiling.get(name_string);
    }

    @JSFunction
    public Scriptable getScriptNames() {
        File[] files = FileManager.BOTS_FOLDER.listFiles(File::isDirectory);

        ArrayList<String> list = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                list.add(file.getName());
            }
        }

        return Context.enter().newArray(ScriptEngine.execScope, list.toArray());
    }

    @JSFunction
    public Boolean replyRoom(String room, String message, Boolean hideToast) {
        AddChatMessageAction.update(message, true);

        return true;
    }

    @JSFunction
    public Boolean canReply(String room) {
        return true;
    }

    @JSFunction
    public void showToast(String content, int length) {
        new Thread(() -> new ShowToastMessageAction(content, 0, 0).showToast()).start();
    }

    @JSFunction
    public String papagoTranslate(String sourceLanguage, String targetLanguage, String data, Boolean errorToString) {
        return null;
    }

    @JSFunction
    public Boolean makeNoti(String title, String content, int id) {
        ShowNotificationAction.update(title, content);

        return true;
    }

    @JSFunction
    public void gc() {
        System.gc();
    }

    @JSFunction
    public void UIThread(Function function, Function onComplete) {
    }
}