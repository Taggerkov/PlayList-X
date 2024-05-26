package com.playlistx.model.utils;

import com.playlistx.model.utils.exceptions.FileException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * File reading and writing class for binary and text files.
 *
 * @author Sergiu Ionut Chirap
 * @version 1.1
 * @since 08/01/2024
 */
public abstract class FileHandler {
    /**
     * Class error message of incorrect file type.
     */
    protected static final String NOT_TXT = "Chosen file is not text (.txt)!", NOT_BIN = "Chosen file is not binary (.bin)!";

    /**
     * Check if a file exist or if application has access to path.
     *
     * @param path The path to the file to check.
     * @return If the operation was a success.
     */
    protected static boolean checkFile(String path) {
        try (FileInputStream test = new FileInputStream(path)) {
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Saves a String in a chosen .txt file.
     * If file exists it will overwrite or add to it and if it doesn't it will create a new file in that path.
     *
     * @param path      Path of the file.
     * @param text      String to be written in the file.
     * @param overwrite If file already exists, true to overwrite or false to add.
     * @throws FileException Exception if file has invalid extension.
     */
    protected static void writeToText(@NotNull String path, String text, boolean overwrite) {
        if (path.endsWith(".txt")) {
            PrintWriter convertToFile = null;
            try {
                FileOutputStream fileOut = new FileOutputStream(path, !overwrite);
                convertToFile = new PrintWriter(fileOut);
                convertToFile.println(text);
            } catch (FileNotFoundException e) {
                e.getStackTrace();
                System.out.println("Path error!");
            } finally {
                if (convertToFile != null) {
                    convertToFile.close();
                }
            }
        } else throw new FileException(NOT_TXT);
    }

    /**
     * Saves an Array of Strings in a chosen .txt file.
     * If file exists it will overwrite and if it doesn't it will create a new file in that path.
     *
     * @param path        Path of the file.
     * @param stringArray Array of Strings to be written in the file.
     * @throws FileException Exception if file has invalid extension.
     */
    protected static void writeToText(@NotNull String path, String[] stringArray) {
        if (path.endsWith(".txt")) {
            PrintWriter writeToFile = null;
            try {
                FileOutputStream fileOutStream = new FileOutputStream(path, false);
                writeToFile = new PrintWriter(fileOutStream);
                for (String string : stringArray) {
                    writeToFile.println(string);
                }
            } catch (FileNotFoundException e) {
                e.getStackTrace();
                System.out.println("Path error!");
            } finally {
                if (writeToFile != null) {
                    writeToFile.close();
                }
            }
        } else throw new FileException(NOT_TXT);
    }

    /**
     * Reads an Array of Strings of a chosen .txt file.
     *
     * @param path Path of the file.
     * @return Array of Strings extracted from the file.
     * @throws FileException Exception if file has invalid extension.
     */
    protected static String @NotNull [] readFromText(@NotNull String path) throws FileException {
        if (path.endsWith(".txt")) {
            Scanner readFromFile = null;
            ArrayList<String> stringArray = new ArrayList<>();
            try {
                FileInputStream fileInStream = new FileInputStream(path);
                readFromFile = new Scanner(fileInStream);
                while (readFromFile.hasNext()) {
                    stringArray.add(readFromFile.nextLine());
                }
            } catch (FileNotFoundException e) {
                e.getStackTrace();
                System.out.println("Path error!");
            } finally {
                if (readFromFile != null) {
                    readFromFile.close();
                }
            }
            return stringArray.toArray(new String[0]);
        } else throw new FileException(NOT_TXT);
    }

    /**
     * Saves any Object in a chosen .bin file.
     * If file exists it will overwrite and if it doesn't it will create a new file in that path.
     *
     * @param path Path of the file.
     * @param obj  Object to be written in the file.
     * @throws FileException Exception if file has invalid extension.
     */
    protected static boolean writeToBinary(@NotNull String path, Object obj) {
        if (path.endsWith(".bin")) {
            ObjectOutputStream convertToFile = null;
            try {
                FileOutputStream fileOut = new FileOutputStream(path);
                convertToFile = new ObjectOutputStream(fileOut);
                convertToFile.writeObject(obj);
                return true;
            } catch (FileNotFoundException e) {
                e.getStackTrace();
                System.out.println("Path error!");
                return false;
            } catch (IOException e) {
                e.getStackTrace();
                System.out.println("I/O error!");
                return false;
            } finally {
                if (convertToFile != null) {
                    try {
                        convertToFile.close();
                    } catch (IOException e) {
                        System.out.println("Unable to close: " + path);
                    }
                }
            }
        } else throw new FileException(NOT_BIN);
    }

    /**
     * Reads an Array of Strings of a chosen .txt file.
     *
     * @param path Path of the file.
     * @return Object extracted from the file.
     * @throws FileException Exception if file has invalid extension.
     */
    protected static Object readFromBinary(@NotNull String path) {
        if (path.endsWith(".bin")) {
            Object obj = null;
            ObjectInput getFromFile = null;
            try {
                FileInputStream fileIn = new FileInputStream(path);
                getFromFile = new ObjectInputStream(fileIn);
                obj = getFromFile.readObject();
            } catch (ClassNotFoundException e) {
                e.getStackTrace();
                System.out.println("Class not Found!");
            } catch (IOException e) {
                e.getStackTrace();
                System.out.println("I/O error!");
            } finally {
                if (getFromFile != null) {
                    try {
                        getFromFile.close();
                    } catch (IOException e) {
                        System.out.println("Unable to close: " + path);
                    }
                }
            }
            return obj;
        } else throw new FileException(NOT_BIN);
    }
}