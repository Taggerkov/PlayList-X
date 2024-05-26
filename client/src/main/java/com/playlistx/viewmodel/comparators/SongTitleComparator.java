package com.playlistx.viewmodel.comparators;

import com.playlistx.model.music.Song;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;

/**
 * UI Custom {@link Comparator<Song>} class for {@link Song songs}.
 * <br> Sorts songs by their title.
 * @author Sergiu Chirap
 * @version final
 * @since 0.5
 * @see java.util.Arrays#sort(Object[], Comparator) Arrays.sort(Object[], Comparator)
 */
public class SongTitleComparator implements Comparator<Song> {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p>
     * The implementor must ensure that {@link Integer#signum
     * signum}{@code (compare(x, y)) == -signum(compare(y, x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * compare(x, y)} must throw an exception if and only if {@code
     * compare(y, x)} throws an exception.)<p>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
     * {@code compare(x, z)>0}.<p>
     * <p>
     * Finally, the implementor must ensure that {@code compare(x,
     * y)==0} implies that {@code signum(compare(x,
     * z))==signum(compare(y, z))} for all {@code z}.
     *
     * @param firstSong the first object to be compared.
     * @param secondSong the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     * @apiNote It is generally the case, but <i>not</i> strictly required that
     * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."
     */
    @Override
    public int compare(@NotNull Song firstSong, @NotNull Song secondSong) {
        String[] comparator = {firstSong.getTitle(), secondSong.getTitle()};
        Arrays.sort(comparator);
        if (comparator[0].equalsIgnoreCase(firstSong.getTitle())) return -1;
        else return 1;
    }

    /**
     * Returns a comparator that imposes the reverse ordering of this
     * comparator.
     *
     * @return a comparator that imposes the reverse ordering of this
     * comparator.
     * @since 1.8
     */
    @Override
    public Comparator<Song> reversed() {
        return Comparator.super.reversed();
    }
}