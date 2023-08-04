import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /**
     * return a Queue of Queues that contain one item from Item
     * like take in ("Alice" > "Vanessa" > "Ethan")
     * return (("Alice") > ("Vanessa") > ("Ethan"))
     */
    private static <Item extends Comparable> Queue<Queue<Item>> makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> queue = new Queue<>();
        for (Item item : items) {
            Queue<Item> q = new Queue<>();
            q.enqueue(item);
            queue.enqueue(q);
        }
        return queue;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        // Your code here!
        Queue<Item> queue = new Queue<>();
        while(!q1.isEmpty() || !q2.isEmpty()) {
            Item item = getMin(q1, q2);
            queue.enqueue(item);
        }
        return queue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        // Your code here!
        if (items == null) {
            return null;
        }
        if (items.size() == 0) {
            return items;
        }
        Queue<Queue<Item>> queue = makeSingleItemQueues(items);
        Queue<Item> q1, q2;
        while (queue.size() >= 2) {
            q1 = queue.dequeue();
            q2 = queue.dequeue();
            queue.enqueue(mergeSortedQueues(q1, q2));
        }
        return queue.dequeue();
    }


    public static void main(String[] args) {
        Queue<String> students = new Queue<>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");
        System.out.println(students);
        System.out.println(mergeSort(students));
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(2);
        queue.enqueue(2);
        queue.enqueue(1);
        queue.enqueue(3);
        queue.enqueue(6);
        queue.enqueue(4);
        System.out.println(mergeSort(queue));
    }
}
