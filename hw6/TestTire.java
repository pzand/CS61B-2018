import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;

public class TestTire {
    private static final String[] testStrings = {
            "a", "abridge", "abridged", "abridges", "abridging", "abroad", "abrupt", "abrupter", "abruptest",
            "aa", "aaa", "b", "bbb", "abc", "bac", "bba", "cc"
    };
    @Test
    public void testAllKeys() {
        Tire tire = new Tire();
        for (String s : testStrings) {
            tire.insertStringToTire(s);
        }
        Set<String> set = tire.allKeys();

        assertEquals(testStrings.length, set.size());
        for (String s : testStrings) {
            assertTrue(set.contains(s));
        }
    }
    @Test
    public void testKeyWithPrefix() {
        Tire tire = new Tire();
        for (String s : testStrings) {
            tire.insertStringToTire(s);
        }
        Set<String> set = tire.keysWithPrefix("ab");
        String[] prefixString = {
                "abridge", "abridged", "abridges", "abridging",
                "abroad", "abrupt", "abrupter", "abruptest", "abc"
        };

        assertEquals(prefixString.length, set.size());
        for (String s : prefixString) {
            assertTrue(set.contains(s));
        }

        set = tire.keysWithPrefix("abc");
        assertEquals(1, set.size());
        assertTrue(set.contains("abc"));
    }
}
