import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestOffByN {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByN = new OffByN(4);

    // Your tests go here.
    @Test
    public void testEqualChars() {
        assertFalse(offByN.equalChars('a', 'a'));
        assertFalse(offByN.equalChars('b', 'a'));
        assertFalse(offByN.equalChars('a', 'b'));
        assertTrue(offByN.equalChars('a', 'e'));
        assertTrue(offByN.equalChars('b', 'f'));
        assertFalse(offByN.equalChars('B', 'f'));
    }
    //Uncomment this class once you've created your
    //CharacterComparator interface and OffByOne class.

}
