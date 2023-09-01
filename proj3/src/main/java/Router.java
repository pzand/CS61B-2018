import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        // 获取起始位置 和 结束位置
        long start = g.closest(stlon, stlat);
        long destination = g.closest(destlon, destlat);

        // 用于优化，把不是最优的排除
        Map<Long, Double> map = new HashMap<>();
        PriorityQueue<NodeMap> heap = new PriorityQueue<>();

        // 初始化堆，填入一个初始状态
        double ed = g.distance(start, start);
        double h = g.distance(start, destination);
        NodeMap nodeMap = new NodeMap(start, null, 0.0, ed + h);
        heap.add(nodeMap);
        map.put(start, 0.0 + ed);

        while (nodeMap.getId() != destination) {
        nodeMap = heap.remove();
            // 把其邻居加入其中
            for (long neighborId : g.adjacent(nodeMap.getId())) {
                double d = nodeMap.getDistance();
                ed = g.distance(nodeMap.getId(), neighborId);
                h = g.distance(neighborId, destination);

                if (map.containsKey(neighborId) && map.get(neighborId) <= d + ed) {
                    continue;
                }

                NodeMap n = new NodeMap(neighborId, nodeMap, d + ed, d + ed + h);
                heap.add(n);
                map.put(n.getId(), d + ed);
            }

//            nodeMap = heap.remove();
        }
        // 将节点转为list
        return getPath(nodeMap); // FIXME
    }

    private static List<Long> getPath(NodeMap nodeMap) {
        LinkedList<Long> path = new LinkedList<>();

        path.addFirst(nodeMap.getId());

        while (!nodeMap.isRoot()) {
            nodeMap = nodeMap.getParent();
            path.addFirst(nodeMap.getId());
        }
        return path;
    }

    private static class NodeMap implements Comparable {
        private final long id;
        private final NodeMap parent;
        private final double distance;
        private final double priority;

        public NodeMap(long id, NodeMap parent, double distance, double priority) {
            this.id = id;
            this.parent = parent;
            this.distance = distance;
            this.priority = priority;
        }

        public long getId() {
            return id;
        }

        public NodeMap getParent() {
            return parent;
        }

        public double getDistance() {
            return distance;
        }

        public double getPriority() {
            return priority;
        }

        public boolean isRoot() {
            return this.parent == null;
        }

        @Override
        public int compareTo(Object o) {
            if (!this.getClass().equals(o.getClass())) {
                throw new IllegalArgumentException("the class is not equal");
            }
            NodeMap n = (NodeMap) o;
            return Double.compare(this.getPriority(), n.getPriority());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NodeMap nodeMap = (NodeMap) o;

            return id == nodeMap.id;
        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> navigationDirections = new LinkedList<>();

        return null; // FIXME
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            }
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
