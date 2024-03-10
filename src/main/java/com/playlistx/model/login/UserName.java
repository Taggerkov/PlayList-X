package com.playlistx.model.login;

import com.playlistx.model.utils.exceptions.InvalidInput;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Random;

public class UserName implements Serializable {
    private static final String ERROR_MESSAGE = "Username length should be within 5 and 100 characters!";
    private static final KeyChain keyChain = KeyChain.get();
    private static final Random randomizer = new Random();
    public static final String[] defaultNouns = {"Sun", "Moon", "Sky", "Clouds", "Player", "Response", "Difference", "Establishment", "Drawer", "Girl", "Camera",
            "Method", "Customer", "Dinner", "Agency", "Passion", "Awareness", "Wood", "Requirement", "Event", "Internet", "Highway", "Activity", "Drama",
            "Topic", "Efficiency", "People", "Ladder", "Disaster", "Ability", "Literature", "Personality", "Poem", "Policy"};
    public static final String[] defaultVerbs = {"Pop", "Debate", "Believe", "Grant", "Approach", "Welcome", "Die", "Accumulate", "Enhance", "Copy", "Fulfil",
            "Accept", "Scatter", "Obtain", "Inherit", "Compile", "Predict", "Dance", "Invent", "Restrict", "Claim", "Wave", "Spoil", "Slide", "Fear",
            "Activate", "Defeat", "Communicate", "Tell", "Survive", "Differentiate", "Rain", "Engage", "Hit"};
    public static final String[] defaultAdjectives = {"Tense", "Upbeat", "Wakeful", "Additional", "Guarded", "Righteous", "Nutritious", "Dramatic", "Vivacious",
            "Wanting", "Shiny", "Materialistic", "Boorish", "Left", "Same", "Encouraging", "Good", "Languid", "Popular", "Venomous", "Merciful", "Fixed",
            "Flagrant", "Quickest", "Daily", "Nutty", "Irate", "Clean", "Angry", "Wild", "Faulty", "Damaging", "Lame", "Warlike"};
    public static final String[] defaultExtended = {"Obtuse", "Lysin", "Unworshipping", "Amala", "Mainmortable", "Nonmucilaginous", "Clambered", "Criminogenic",
            "Ciphered", "Frugged", "Unpurgatively", "Abnormalize", "Apterygidae", "Escarpments", "Acestes", "Carriageless", "Nonaffection", "Anterevolutional",
            "Preamplifier", "Dendritiform", "Wormfishes", "Muzzlewood", "Patriarchship", "Peul", "Postdiluvial", "Hypochondriacally", "Photoreduction", "Juans",
            "Coccoids", "Bamboozles", "Graduated", "Sacrificant", "Certifiableness", "Fjorded"};
    private final String userName;

    private UserName(String userName) {
        this.userName = userName;
    }

    @Contract("_ -> new")
    public static @NotNull UserName fresh(String userName) throws InvalidInput {
        if (userName == null || userName.isEmpty()) {
            String generatedName = generateName();
            while (!keyChain.isAvailable(generatedName)) generatedName = generateName();
            return new UserName(generatedName);
        } else {
            if (userName.length() > 2 && userName.length() < 40) return new UserName(userName);
            else throw new InvalidInput(ERROR_MESSAGE);
        }
    }

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

    private static String randomNoun() {
        return defaultNouns[randomizer.nextInt(defaultNouns.length)];
    }

    private static String randomVerb() {
        return defaultVerbs[randomizer.nextInt(defaultVerbs.length)];
    }

    private static String randomAdjective() {
        return defaultAdjectives[randomizer.nextInt(defaultAdjectives.length)];
    }

    private static String randomExtended() {
        return defaultExtended[randomizer.nextInt(defaultExtended.length)];
    }

    public String getUserName() {
        return userName;
    }

    public boolean equals(String userName) {
        return this.userName.equalsIgnoreCase(userName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserName otherName)) return false;
        return userName.equalsIgnoreCase(otherName.userName);
    }
}
