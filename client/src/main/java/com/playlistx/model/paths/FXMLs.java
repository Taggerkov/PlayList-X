package com.playlistx.model.paths;

import com.playlistx.model.utils.exceptions.InputException;
import org.jetbrains.annotations.NotNull;

public enum FXMLs {
    LOGIN, CHAT, CHAT_ITEM, MSG_ITEM, CHOOSE_USER, NEW_CHAT;
    public static final String loginPath = "/app/chatty/fxml/login.fxml";
    public static final String chatPath = "/app/chatty/fxml/chat.fxml";
    public static final String chatItemPath = "/app/chatty/fxml/chatItem.fxml";
    public static final String msgItemPath = "/app/chatty/fxml/msgItem.fxml";
    public static final String chooseUserPath = "/app/chatty/fxml/chooseUser.fxml";
    public static final String newChatPath = "/app/chatty/fxml/newChat.fxml";

    public @NotNull String get() {
        return switch (this) {
            case LOGIN -> loginPath;
            case CHAT -> chatPath;
            case CHAT_ITEM -> chatItemPath;
            case MSG_ITEM -> msgItemPath;
            case CHOOSE_USER -> chooseUserPath;
            case NEW_CHAT -> newChatPath;
            case null -> throw new InputException("No such fxml found!");
        };
    }
}