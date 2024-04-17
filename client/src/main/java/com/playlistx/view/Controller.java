package com.playlistx.view;

import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

interface Controller {
    void init(@NotNull Scene scene);

    String getFXML();

    Scene getScene();
}