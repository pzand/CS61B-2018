public class test {
    public static void main(String[] args) {
        LinkedListDeque<Integer>p = new LinkedListDeque<>();
        p.addLast(44);
        p.addFirst(33);
        p.addFirst(22);
        p.addLast(55);
        p.printDeque();

        System.out.println(p.getrecursion(5));
    }
}
