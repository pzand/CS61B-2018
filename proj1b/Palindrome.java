public class Palindrome {
    /**
     * A class for palindrome operation
     */
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindromeRecursion(deque);
    }

    /* helper method for the isPalindrome */
    private boolean isPalindromeRecursion(Deque<Character> deque) {
        if (deque.size() <= 1) {
            return true;
        }
        if (deque.removeFirst() != deque.removeLast()) {
            return false;
        }
        return isPalindromeRecursion(deque);
    }

    private boolean isPalindromeRecursion(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() <= 1) {
            return true;
        }
        char first = deque.removeFirst();
        char last = deque.removeLast();
        if (!cc.equalChars(first, last)) {
            return false;
        }
        return isPalindromeRecursion(deque, cc);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindromeRecursion(deque, cc);
    }
}
