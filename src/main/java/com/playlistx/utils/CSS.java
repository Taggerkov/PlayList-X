package com.playlistx.utils;

public enum CSS {
    LIGHT, DARK;
    private static final String CSS_LIGHT = "/com/playlistx/css/light.css";
    private static final String CSS_DARK = "/com/playlistx/css/dark.css";
    private static String CURRENT;

    public static String getCURRENT() {
        if (CURRENT != null) return CURRENT;
        CSS.LIGHT.set();
        return CURRENT;
    }

    public String get() {
        switch (this) {
            default -> {
                return LookUp(CSS_LIGHT);
            }
            case DARK -> {
                return LookUp(CSS_DARK);
            }
        }
    }

    public void set() {
        staticSet(this);
    }

    private static void staticSet(CSS css) {
        switch (css) {
            case LIGHT -> CURRENT = LookUp(CSS_LIGHT);
            case DARK -> CURRENT = LookUp(CSS_DARK);
        }
    }

    private static String LookUp(String path) {
        return CSS.class.getResource(path).toExternalForm();
    }
}