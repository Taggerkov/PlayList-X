package com.playlistx.model;

import com.playlistx.model.paths.AppData;
import com.playlistx.model.paths.CSS;
import com.playlistx.model.utils.FileHandler;
import com.playlistx.view.ViewHandler;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Settings extends FileHandler implements Serializable {
    private static Settings instance;
    private final StringBuilder txtFile = new StringBuilder();
    private Boolean isNeverSave = false;
    private Boolean isSaveLogin = false;
    private HomeChoice homeChoice = HomeChoice.HOME;

    private Settings() {
        readTXT();
    }

    public static Settings get() {
        if (instance == null) return instance = new Settings();
        else return instance;
    }

    private String readValue(@NotNull String line) {
        String[] extract = line.split("_");
        String[] value = extract[1].split("\t");
        return value[0];
    }

    private void writeLine(@NotNull TXTSettings setting, @NotNull String value) {
        switch (setting) {
            case NEVER_SAVE ->
                    txtFile.append("NeverSave: ---------> _").append(value).append("\t// Will always reset settings on read\n");
            case LOGIN ->
                    txtFile.append("Login: -------------> _").append(value).append("\t// Options: default = Type Credentials / save = Store Credentials\n");
            case THEME -> txtFile.append("Theme: -------------> _").append(value).append("\t// Options: light, dark\n");
            case HOME_PAGE ->
                    txtFile.append("Home Page: ---------> _").append(value).append("\t// Options: home, playlist, radio\n");
        }
    }

    public void readTXT() {
        String[] settings = readFromText(AppData.txtSettings);
        if (readValue(settings[1]).equalsIgnoreCase("TRUE")) reset();
        else {
            isNeverSave = readValue(settings[2]).equalsIgnoreCase("TRUE");
            isSaveLogin = readValue(settings[5]).equalsIgnoreCase("SAVE");
            switch (readValue(settings[6])) {
                case "light" -> CSS.setCSS(CSS.LIGHT);
                case "dark" -> CSS.setCSS(CSS.DARK);
            }
            switch (readValue(settings[7])) {
                case "home" -> homeChoice = HomeChoice.HOME;
                case "playlist" -> homeChoice = HomeChoice.PLAYLISTS;
                case "radio" -> homeChoice = HomeChoice.RADIO;
            }
            save();
        }
    }

    private void writeTXT() {
        String txt;
        String BEHAVIOUR = """
                # BEHAVIOUR !Warning ~~ ( Deletes Custom Settings )
                Reset: -------------> _false\t// Resets current settings to default.
                """;
        String LOCAL = "\n# LOCAL SETTINGS\n";
        String COPY = """


                PlayList X: TXT Settings
                Modify with caution!

                ©2023, PlayList X. All Rights Reserved.""";

        txtFile.append(BEHAVIOUR);
        writeLine(TXTSettings.NEVER_SAVE, isNeverSave.toString().toLowerCase());
        txtFile.append(LOCAL);
        if (isSaveLogin) txt = "save";
        else txt = "default";
        writeLine(TXTSettings.LOGIN, txt);
        writeLine(TXTSettings.THEME, CSS.getCSS().toString().toLowerCase());
        writeLine(TXTSettings.HOME_PAGE, homeChoice.toString().toLowerCase());
        txtFile.append(COPY);
        writeToText(AppData.txtSettings, txtFile.toString(), true);
    }

    private void reset() {
        if (ViewHandler.popUp(ViewHandler.Notify.CONFIRM, "Settings are about to get reset!")) {
            isNeverSave = false;
            isSaveLogin = false;
            CSS.setCSS(CSS.DARK);
            homeChoice = HomeChoice.HOME;
            save();
        }
    }

    private void save() {
        writeTXT();
    }
}

enum TXTSettings {
    RESET, NEVER_SAVE, LOGIN, THEME, HOME_PAGE;
}

enum HomeChoice {
    HOME, PLAYLISTS, RADIO;
}