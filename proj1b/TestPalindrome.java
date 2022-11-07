import org.junit.Test;

import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    //Uncomment this class once you've created your Palindrome class.
    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));
        assertFalse(palindrome.isPalindrome("cat"));
        assertTrue(palindrome.isPalindrome("anona"));
        assertTrue(palindrome.isPalindrome("noon"));
        assertFalse(palindrome.isPalindrome("aaaaab"));
    }

    @Test
    public void testIsPalindrome_Use_OffByOne() {
        assertTrue(palindrome.isPalindrome("ab", new OffByOne()));
        assertTrue(palindrome.isPalindrome("ba", new OffByOne()));
        assertTrue(palindrome.isPalindrome("flake", new OffByOne()));
        assertFalse(palindrome.isPalindrome("asd", new OffByOne()));
        assertFalse(palindrome.isPalindrome("wer", new OffByOne()));
    }
}
