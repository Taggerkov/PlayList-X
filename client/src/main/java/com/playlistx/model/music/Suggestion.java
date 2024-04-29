package com.playlistx.model.music;

import java.util.List;

/**
 * The Suggestion class represents a suggestion for songs based on various criteria.
 */
public class Suggestion {
    private String label; // Label to indicate the type of suggestion (e.g., "Popular", "New", "Similar")
    private List<Song> suggestedSongs; // List of songs suggested

    /**
     * Constructor for Suggestion class.
     *
     * @param label The label indicating the type of suggestion.
     * @param suggestedSongs The list of songs suggested.
     */
    public Suggestion(String label, List<Song> suggestedSongs) {
        this.label = label;
        this.suggestedSongs = suggestedSongs;
    }

    /**
     * Gets the label of the suggestion.
     *
     * @return A string representing the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the suggestion.
     *
     * @param label A string representing the label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the list of suggested songs.
     *
     * @return A list of Song objects.
     */
    public List<Song> getSuggestedSongs() {
        return suggestedSongs;
    }

    /**
     * Sets the list of suggested songs.
     *
     * @param suggestedSongs A list of Song objects to be suggested.
     */
    public void setSuggestedSongs(List<Song> suggestedSongs) {
        this.suggestedSongs = suggestedSongs;
    }

    /**
     * Provides a string representation of the Suggestion instance.
     *
     * @return A string describing the Suggestion.
     */
    @Override
    public String toString() {
        return "Suggestion{" +
                "label='" + label + '\'' +
                ", suggestedSongs=" + suggestedSongs +
                '}';
    }
}
