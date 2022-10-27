import org.junit.*;

import static org.junit.Assert.*;

public class ArrayTest {
//    @Test
//    public void TestResizeToZeroOrLength() {
//        ArrayDeque<Integer> a = new ArrayDeque<>();
//        assertEquals(a.resizingToZeroOrLength(8), 0);
//        assertEquals(a.resizingToZeroOrLength(-1), 7);
//        assertEquals(a.resizingToZeroOrLength(4 + 5), 1);
//    }
//
//    @Test
//    public void TestAddFirst() {
//        ArrayDeque<Integer> a = new ArrayDeque<>();
//        assertTrue(a.isEmpty());
//        a.addFirst(1);
//        assertArrayEquals(new Integer[]{1, null, null, null, null, null, null, null}, a.arr);
//        a.addFirst(8);
//        assertArrayEquals(new Integer[]{1, null, null, null, null, null, null, 8}, a.arr);
//        a.addFirst(7);
//        assertArrayEquals(new Integer[]{1, null, null, null, null, null, 7, 8}, a.arr);
//        assertEquals(3, a.size());
//        assertEquals((Integer) 7, a.get(1));
//        a.addLast(2);
//        assertArrayEquals(new Integer[]{1, 2, null, null, null, null, 7, 8}, a.arr);
//        a.addLast(3);
//        assertArrayEquals(new Integer[]{1, 2, 3, null, null, null, 7, 8}, a.arr);
//        a.addFirst(6);
//        assertArrayEquals(new Integer[]{1, 2, 3, null, null, 6, 7, 8}, a.arr);
//        assertEquals(6, a.size());
//        assertFalse(a.isEmpty());
//        assertEquals((Integer) 6, a.get(0));
//        assertEquals((Integer) 1, a.get(3));
//        assertEquals((Integer) 2, a.get(4));
//        assertEquals((Integer) 3, a.get(5));
//
//        assertEquals((Integer) 6, a.removeFirst());
//        assertEquals((Integer) 7, a.removeFirst());
//        assertEquals((Integer) 3, a.removeLast());
//
//        a.addFirst(99);
//        assertArrayEquals(new Integer[]{1, 2, 3, null, null, 6, 99, 8}, a.arr);
//        a.addLast(99);
//        assertArrayEquals(new Integer[]{1, 2, 99, null, null, 6, 99, 8}, a.arr);
//        a.addFirst(6);//这个被remove了
//
//        a.addFirst(11);
//        a.addFirst(22);
//        assertArrayEquals(new Integer[]{1, 2, 99, null, null, null, null, null, null, null, null, 22, 11, 6, 99, 8},
//                a.arr);
//        a.addFirst(44);
//        assertArrayEquals(new Integer[]{1, 2, 99, null, null, null, null, null, null, null, 44, 22, 11, 6, 99, 8},
//                a.arr);
//        assertEquals((Integer) 44, a.removeFirst());
//        assertEquals((Integer) 22, a.removeFirst());
//        assertEquals((Integer) 99, a.removeLast());
//    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        L.addLast(0);
        L.addLast(2);
        L.addLast(3);
        L.addFirst(4);
        int a = L.get(0);
        System.out.println(a);
    }
}