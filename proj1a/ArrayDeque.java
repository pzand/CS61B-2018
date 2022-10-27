public class ArrayDeque<T> implements Deque<T> {
    /**
     * Add and remoove must take constant time, except during resizing operations
     * get and size must take constant time
     * The starting size of your array should be 8
     * The amount of memory that your program uses at any given time
     * must be proportional to the number of items
     *
     * @param item
     */
    private int size;
    private int nextLast;
    private int nextFirst;
    private double R;
    private T[] arr;

    public ArrayDeque() {
        arr = (T[]) new Object[8];
        R = 0;
        size = 0;
        nextLast = 1;
        nextFirst = 0;
    }

    @Override
    public void addFirst(T item) {
        this.size++;
        arr[this.nextFirst] = item;
        this.nextFirst = resizingToZeroOrLength(--this.nextFirst);
        resizingArr();
    }

    @Override
    public void addLast(T item) {
        this.size++;
        arr[this.nextLast] = item;
        this.nextLast = resizingToZeroOrLength(++this.nextLast);
        resizingArr();
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void printDeque() {
        for (int i = 1, num = this.nextFirst; i <= this.size; i++) {
            num = resizingToZeroOrLength(num + 1);
            System.out.print(arr[num] + " ");
        }
    }

    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        this.size--;
        this.nextFirst = resizingToZeroOrLength(++this.nextFirst);
        T a = this.arr[this.nextFirst];
        resizingArr();
        return a;
    }

    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        this.size--;
        this.nextLast = resizingToZeroOrLength(--this.nextLast);
        T a = this.arr[this.nextLast];
        resizingArr();
        return a;
    }

    @Override
    public T get(int index) {
        if (this.size < index - 1 || index < 0) {
            return null;
        } else {
            int new_index = resizingToZeroOrLength(this.nextFirst + index + 1);
            return arr[new_index];
        }
    }

    private int resizingToZeroOrLength(int num) {
        return (num + arr.length) % arr.length;
    }

    private void resizingArr() {
        this.R = (double) this.size / this.arr.length;
        T[] arr2;

        if (R >= 0.99) {
            arr2 = (T[]) new Object[this.arr.length * 2];
        } else if (R <= 0.25 && arr.length > 8) {
            arr2 = (T[]) new Object[this.arr.length / 2];
        } else {
            return;
        }

        for (int i = 1, num = this.nextFirst; i <= this.size; i++) {
            num = resizingToZeroOrLength(num + 1);
            arr2[i] = arr[num];
        }
        this.nextFirst = 0;
        this.nextLast = this.size + 1;
        arr = arr2;
    }
}