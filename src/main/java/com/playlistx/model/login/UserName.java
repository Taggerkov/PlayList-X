package com.playlistx.model.login;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Random;

/**
 * The {@code UserName} is a serializable package-wide class that holds the 'user' username across the package while ensuring its proper use.
 * <p>
 * It features a 'username' requirement checker and a local username generator.
 * <p>
 * Some methods may throw a {@link LoginException} based on unmet requirements.
 *
 * @author Sergiu Chirap
 * @see LoginException
 * @see java.io.Serializable
 * @since 0.1
 */

public class UserName implements Serializable {
    /**
     * This {@link java.lang.String} stores the length error message.
     */
    private static final String ERROR_LENGTH = "Username length should be within 3 and 100 characters!";
    /**
     * This class is used for 'username' availability.
     */
    private static final KeyChain keyChain = KeyChain.get();
    /**
     * This class is used for the random factor of the 'username' generator.
     */
    private static final Random randomizer = new Random();
    /**
     * This {@link java.lang.String} array is used to store nouns for the 'username' generator.
     */
    public static final String[] defaultNouns = {"Sun", "Moon", "Sky", "Clouds", "Player", "Response", "Difference", "Establishment", "Drawer", "Girl", "Camera",
            "Method", "Customer", "Dinner", "Agency", "Passion", "Awareness", "Wood", "Requirement", "Event", "Internet", "Highway", "Activity", "Drama",
            "Topic", "Efficiency", "People", "Ladder", "Disaster", "Ability", "Literature", "Personality", "Poem", "Policy"};
    /**
     * This {@link java.lang.String} array is used to store verbs for the 'username' generator.
     */
    public static final String[] defaultVerbs = {"Pop", "Debate", "Believe", "Grant", "Approach", "Welcome", "Die", "Accumulate", "Enhance", "Copy", "Fulfil",
            "Accept", "Scatter", "Obtain", "Inherit", "Compile", "Predict", "Dance", "Invent", "Restrict", "Claim", "Wave", "Spoil", "Slide", "Fear",
            "Activate", "Defeat", "Communicate", "Tell", "Survive", "Differentiate", "Rain", "Engage", "Hit"};
    /**
     * This {@link java.lang.String} array is used to store adjectives for the 'username' generator.
     */
    public static final String[] defaultAdjectives = {"Tense", "Upbeat", "Wakeful", "Additional", "Guarded", "Righteous", "Nutritious", "Dramatic", "Vivacious",
            "Wanting", "Shiny", "Materialistic", "Boorish", "Left", "Same", "Encouraging", "Good", "Languid", "Popular", "Venomous", "Merciful", "Fixed",
            "Flagrant", "Quickest", "Daily", "Nutty", "Irate", "Clean", "Angry", "Wild", "Faulty", "Damaging", "Lame", "Warlike"};
    /**
     * This {@link java.lang.String} array is used to store extended variables for the 'username' generator.
     */
    public static final String[] defaultExtended = {"Obtuse", "Lysin", "Unworshipping", "Amala", "Mainmortable", "Nonmucilaginous", "Clambered", "Criminogenic",
            "Ciphered", "Frugged", "Unpurgatively", "Abnormalize", "Apterygidae", "Escarpments", "Acestes", "Carriageless", "Nonaffection", "Anterevolutional",
            "Preamplifier", "Dendritiform", "Wormfishes", "Muzzlewood", "Patriarchship", "Peul", "Postdiluvial", "Hypochondriacally", "Photoreduction", "Juans",
            "Coccoids", "Bamboozles", "Graduated", "Sacrificant", "Certifiableness", "Fjorded"};
    /**
     * This {@link java.lang.String} is used to store the provided or generated 'username'.
     */
    private final String username;

    /**
     * Initializes an instance with the provided or generated 'username' based on {@link #fresh(String)}.
     * <p>
     * This is a private constructor, due to personalized initialization.
     *
     * @param username A {@link java.lang.String} which represents the 'username'.
     */
    private UserName(String username) {
        this.username = username;
    }

    /**
     * A {@code static} method which is responsible for {@code UserName} proper creation.
     * <p>
     * If {@code username} is either {@code null} or empty it will be generated.
     *
     * @param username A {@link java.lang.String} which represents the 'username'.
     * @return A {@code UserName} instance based on the provided {@code username}.
     * @throws LoginException An {@link java.lang.Exception} that occurs when requirements are unmet.
     */
    @Contract("_ -> new")
    public static @NotNull UserName fresh(String username) throws LoginException {
        if (username == null || username.isEmpty()) {
            String generatedName = generateName();
            while (!keyChain.isAvailable(generatedName)) generatedName = generateName();
            return new UserName(generatedName);
        } else {
            if (username.length() > 2 && username.length() < 40) return new UserName(username);
            else throw new LoginException(ERROR_LENGTH);
        }
    }

    /**
     * Private method to generate an 'username'.
     * <p>
     * This method is divided into another sub-methods.
     *
     * @return A {@link java.lang.String} which holds the generated 'username'.
     */
    private static @NotNull String generateName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < randomizer.nextInt(2) + 1; i++) {
            switch (randomizer.nextInt(3)) {
                case 0 -> stringBuilder.append(randomVerb());
                case 1 -> stringBuilder.append(randomAdjective());
                case 2 -> stringBuilder.append(randomExtended());
            }
        }
        stringBuilder.append(randomNoun());
        return stringBuilder.toString();
    }

    /**
     * A {@link #generateName()} sub-method which extracts a random noun from {@link #defaultNouns}.
     *
     * @return A {@link java.lang.String} holding the extracted random noun.
     */
    private static String randomNoun() {
        return defaultNouns[randomizer.nextInt(defaultNouns.length)];
    }

    /**
     * A {@link #generateName()} sub-method which extracts a random verb from {@link #defaultVerbs}.
     *
     * @return A {@link java.lang.String} holding the extracted random verb.
     */
    private static String randomVerb() {
        return defaultVerbs[randomizer.nextInt(defaultVerbs.length)];
    }

    /**
     * A {@link #generateName()} sub-method which extracts a random adjectives from {@link #defaultAdjectives}.
     *
     * @return A {@link java.lang.String} holding the extracted random adjective.
     */
    private static String randomAdjective() {
        return defaultAdjectives[randomizer.nextInt(defaultAdjectives.length)];
    }

    /**
     * A {@link #generateName()} sub-method which extracts a random extended variable from {@link #defaultExtended}.
     *
     * @return A {@link java.lang.String} holding the extracted random extended variable.
     */
    private static String randomExtended() {
        return defaultExtended[randomizer.nextInt(defaultExtended.length)];
    }

    /**
     * An {@code override} method of {@link java.lang.Object#toString()}.
     *
     * @return A {@link java.lang.String} which represents the 'username'.
     */
    @Override
    public String toString() {
        return username;
    }

    /**
     * An {@code override} method of {@link java.lang.Object#equals(Object)}.
     *
     * @return A {@code boolean} which states if the provided {@code Object} is equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserName otherName)) return false;
        return username.equalsIgnoreCase(otherName.username);
    }
}