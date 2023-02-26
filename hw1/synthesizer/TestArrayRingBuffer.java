package synthesizer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the ArrayRingBuffer class.
 *
 * @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);
        arb.enqueue(5);
        arb.enqueue(6);
        arb.enqueue(7);
        arb.enqueue(8);
        arb.enqueue(9);
        arb.enqueue(10);
        for (Integer a : arb) {
            System.out.print(a + " ");
        }
        assertEquals(1, arb.peek().intValue());
        assertEquals(1, arb.peek().intValue());
        assertEquals(1, arb.dequeue().intValue());
        assertEquals(2, arb.dequeue().intValue());
        assertEquals(3, arb.dequeue().intValue());
        arb.dequeue();
        arb.dequeue();
        arb.dequeue();
        arb.dequeue();
        arb.dequeue();
        arb.dequeue();
        arb.dequeue();
        arb.enqueue(11);
        for (Integer a : arb) {
            System.out.print(a + " ");
        }
        assertEquals(11, arb.dequeue().intValue());
    }

    /**
     * Calls tests for ArrayRingBuffer.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
