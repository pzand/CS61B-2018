public interface Deque<T> {
    /**
     * Adds an item of type T to the front of the deque
     */
    void addFirst(T item);

    /**
     * Add an item of type T to the back of the deque
     */
    void addLast(T item);

    /**
     * Returns true if deque is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of item in the deque
     */
    int size();

    /**
     * prints the item int deque from first to last, separated by a space
     */
    void printDeque();

    /**
     * Removes and return the item at the front of the deque. if no such item exists, returns null
     */
    T removeFirst();

    /**
     * Removes and return the item at the back of the deque. if no such item exists, returns null
     */
    T removeLast();

    /**
     * Gets the item at the given index, where 0 is the front. if no such item exists, return null
     */
    T get(int index);
}
