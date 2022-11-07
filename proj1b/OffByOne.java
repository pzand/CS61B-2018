public class OffByOne implements CharacterComparator {
    /**
     * A class for off-by-1 comparators
     */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}
