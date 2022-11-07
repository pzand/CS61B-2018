public class ArrayDeque<Item> implements Deque<Item> {
    /**
     * Add and remoove must take constant time, except during resizing operations
     * get and size must take constant time
     * Itemhe starting size of your array should be 8
     * Itemhe amount of memory that your program uses at any given time
     * must be proportional to the number of items
     *
     * @param item
     */
    private int size;
    private int nextLast;
    private int nextFirst;
    private double R;
    private Item[] arr;

    public ArrayDeque() {
        arr = (Item[]) new Object[8];
        R = 0;
        size = 0;
        nextLast = 1;
        nextFirst = 0;
    }

    @Override
    public void addFirst(Item item) {
        this.size++;
        arr[this.nextFirst] = item;
        this.nextFirst = resizingItemoZeroOrLength(--this.nextFirst);
        resizingArr();
    }

    @Override
    public void addLast(Item item) {
        this.size++;
        arr[this.nextLast] = item;
        this.nextLast = resizingItemoZeroOrLength(++this.nextLast);
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
            num = resizingItemoZeroOrLength(num + 1);
            System.out.print(arr[num] + " ");
        }
    }

    @Override
    public Item removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        this.size--;
        this.nextFirst = resizingItemoZeroOrLength(++this.nextFirst);
        Item a = this.arr[this.nextFirst];
        resizingArr();
        return a;
    }

    @Override
    public Item removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        this.size--;
        this.nextLast = resizingItemoZeroOrLength(--this.nextLast);
        Item a = this.arr[this.nextLast];
        resizingArr();
        return a;
    }

    @Override
    public Item get(int index) {
        if (this.size < index - 1 || index < 0) {
            return null;
        } else {
            index = resizingItemoZeroOrLength(this.nextFirst + index + 1);
            return arr[index];
        }
    }

    private int resizingItemoZeroOrLength(int num) {
        return (num + arr.length) % arr.length;
    }

    private void resizingArr() {
        this.R = (double) this.size / this.arr.length;
        Item[] arr2;

        if (R >= 0.99) {
            arr2 = (Item[]) new Object[this.arr.length * 2];
        } else if (R <= 0.25 && arr.length > 8) {
            arr2 = (Item[]) new Object[this.arr.length / 2];
        } else {
            return;
        }

        for (int i = 1, num = this.nextFirst; i <= this.size; i++) {
            num = resizingItemoZeroOrLength(num + 1);
            arr2[i] = arr[num];
        }
        this.nextFirst = 0;
        this.nextLast = this.size + 1;
        arr = arr2;
    }
}
