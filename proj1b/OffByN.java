public class OffByN implements CharacterComparator{
    /**
     * A class for off-by-N comparators
     */
    private final int N;
    public OffByN(int N) {
        this.N = N;
    }
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == this.N;
    }
}
