public class LinkedListDeque<Item> implements Deque<Item> {
    /**
     * Add and Remove operations must not involve any looping and recursion
     * Get must use iteration, not recursion
     * Size must take constant time
     */
    private int size;
    private LinkedList sentinel;

    public LinkedListDeque() {
        this.sentinel = new LinkedList(2, null);
        this.sentinel.next = this.sentinel;
        this.sentinel.front = this.sentinel;
        this.size = 0;
    }

    private class LinkedList<Item> {
        private final Item item;
        private LinkedList front;
        private LinkedList next;

        public LinkedList(Item item, LinkedList nextItem) {
            this.item = item;
            this.next = nextItem;
        }
    }

    @Override
    public void addFirst(Item item) {
        this.size++;
        LinkedList p1 = this.sentinel;
        LinkedList p2 = new LinkedList(item, p1.next);
        p1.next.front = p2;
        p1.next = p2;
        p2.front = p1;
    }

    @Override
    public void addLast(Item item) {
        this.size++;
        LinkedList p1 = this.sentinel;
        LinkedList p2 = new LinkedList(item, p1);
        p2.front = p1.front;
        p2.front.next = p2;
        p1.front = p2;
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
        LinkedList p1 = this.sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(p1.item + " ");
            p1 = p1.next;
        }
    }

    @Override
    public Item removeFirst() {
        if (!this.isEmpty()) {
            this.size--;
            LinkedList p1 = this.sentinel.next;
            this.sentinel.next = p1.next;
            p1.next.front = this.sentinel;
            return (Item) p1.item;
        } else {
            return null;
        }
    }

    @Override
    public Item removeLast() {
        if (!this.isEmpty()) {
            this.size--;
            LinkedList p1 = this.sentinel.front;
            p1.front.next = this.sentinel;
            this.sentinel.front = p1.front;
            return (Item) p1.item;
        } else {
            return null;
        }
    }

    @Override
    public Item get(int index) {
        if (this.isEmpty() || index < 0 || this.size < index - 1) {
            return null;
        }
        LinkedList p1 = this.sentinel.next;
        for (int i = 0; i < index; i++) {
            p1 = p1.next;
        }
        return (Item) p1.item;
    }

    private Item getRecursive(int index, LinkedList<Item> p) {
        if (index == 0) {
            return p.item;
        }
        return (Item) getRecursive(index - 1, p.next);
    }

    public Item getRecursive(int index) {
        if (this.isEmpty() || index < 0 || index > this.size - 1) {
            return null;
        } else {
            return (Item) getRecursive(index, this.sentinel.next);
        }
    }
}
