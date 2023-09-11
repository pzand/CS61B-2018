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

            // 意味着找不到路
            if (heap.isEmpty()) {
                return new LinkedList<>();
            }

            nodeMap = heap.remove();
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
        RouteDirectionHelper rdh = new RouteDirectionHelper(g, route);
        return rdh.solve(); // FIXME
    }

    private static class RouteDirectionHelper {
        private final GraphDB g;
        private final List<Long> route;
        // 划分后的道路
        private List<List<Long>> list;
        // 划分后的道路名字
        private List<String> roadName;

        public RouteDirectionHelper(GraphDB g, List<Long> route) {
            this.g = g;
            this.route = route;
            this.list = new LinkedList<>();
            this.roadName = new LinkedList<>();
        }

        // 求解实际的
        public List<NavigationDirection> solve() {
            List<NavigationDirection> routeDirections = new LinkedList<>();

            divideRode();

            // 根据划分，计算distance并添加名字
            Iterator<String> name = roadName.iterator();
            for (List<Long> road : list) {
                NavigationDirection navigationDirection = new NavigationDirection();
                navigationDirection.distance = computeDistance(road);
                navigationDirection.way = name.next();

                routeDirections.add(navigationDirection);
            }

            // 计算bearing，需要上一个道路最后的两节点 和 该道路的最开始两节点
            // 其中最后节点和最开始节点是同一个节点
            Iterator<NavigationDirection> navigationDirection = routeDirections.iterator();
            Iterator<List<Long>> road = list.iterator();

            // 初始化最开始的NavigationDirection
            NavigationDirection navDir = navigationDirection.next();
            navDir.direction = NavigationDirection.START;
            List<Long> currentRoad = road.next();
            double previousBearing = g.bearing(currentRoad.get(currentRoad.size() - 2), currentRoad.get(currentRoad.size() - 1));

            while (navigationDirection.hasNext()) {
                currentRoad = road.next();
                navDir = navigationDirection.next();

                // 下一个节点的相对方向
                double currentBearing = g.bearing(currentRoad.get(0), currentRoad.get(1));

                // 获取实际转向
                double sub = (currentBearing - previousBearing);
                sub += (currentBearing - previousBearing) <= -180 ? 360 : 0;
                sub += (currentBearing - previousBearing) >= 180 ? -360 : 0;

                navDir.direction = bearingToDirection(sub);
                // 上一个节点的相对方向
                previousBearing = g.bearing(currentRoad.get(currentRoad.size() - 2), currentRoad.get(currentRoad.size() - 1));
            }
            return routeDirections;
        }

        // 划分道路
        // 并未通过测试5，不知为什么选择43rd Street道路，已经放弃修复。FIXME
        private void divideRode() {
            // 道路迭代器
            Iterator<Long> routeIterator = route.iterator();
            List<Long> road;

            // previousNode前一个节点， thisNode当前节点
            long previousNode = routeIterator.next();
            long thisNode = routeIterator.next();

            // 初始化第一次情况
            road = new LinkedList<>();
            String name = commonName(previousNode, thisNode);
            roadName.add(name);
            road.add(previousNode);
            road.add(thisNode);
            previousNode = thisNode;

            while (routeIterator.hasNext()) {
                thisNode = routeIterator.next();

                // 如果该节点有该道路名字，则添加到road。否则进行划分
                Set<String> nodeName = g.getRoadName(thisNode);
                if (nodeName.contains(name)) {
                    road.add(thisNode);
                } else {
                    // 添加到list中，进行划分
                    list.add(road);

                    // 初始化，寻找下一个最长的道路名字，并添加岔路节点和下一节点到road中
                    road = new LinkedList<>();
                    name = commonName(previousNode, thisNode);
                    roadName.add(name);
                    road.add(previousNode);
                    road.add(thisNode);
                }

                previousNode = thisNode;
            }

            // 把最后的road添加到其中
            list.add(road);
        }

        // 根据节点v和节点w，找到 最长的 共同的道路name。
        // 在寻找中，可能会全部移除，FIXME
        private String commonName(long v, long w) {
            Set<String> commonRoadName = new HashSet<>();

            // 根据节点v和节点w，找到共同的道路name
            Set<String> wRoadName = g.getRoadName(w);
            for (String s : g.getRoadName(v)) {
                if (wRoadName.contains(s)) {
                    commonRoadName.add(s);
                }
            }

            // 将route迭代器移到节点w的位置
            Iterator<Long> routeIterator = route.iterator();
            while (routeIterator.hasNext()) {
                if (routeIterator.next() == w) {
                    break;
                }
            }

            // 根据筛选出来的道路名字，接着往下寻找。寻找最长的道路名字
            while (commonRoadName.size() > 1) {
                Iterator<String> crnIterator = commonRoadName.iterator();
                Set<String> road = g.getRoadName(routeIterator.next());

                while (crnIterator.hasNext()) {
                    if (!road.contains(crnIterator.next())) {
                        crnIterator.remove();
                    }
                }
            }

            // 实际可能会全部移除
            if (commonRoadName.size() == 1) {
                return commonRoadName.iterator().next();
            }

            throw new IllegalArgumentException("have not same name, node: " + v + "," + w);
        }

        // 根据list的节点，计算长度
        private double computeDistance(List<Long> road) {
            double thisRodeDistance = 0;

            long beforeNode = road.get(0);
            for (long node : road) {
                thisRodeDistance += g.distance(beforeNode, node);
                beforeNode = node;
            }
            return thisRodeDistance;
        }

        // 将bearing转为 NavigationDirection的具体方向
        private int bearingToDirection(double bearing) {
            if (bearing < -100) {
                return NavigationDirection.SHARP_LEFT;
            } else if (bearing < -30) {
                return NavigationDirection.LEFT;
            } else if (bearing < -15) {
                return NavigationDirection.SLIGHT_LEFT;
            } else if (bearing <= 15) {
                return NavigationDirection.STRAIGHT;
            } else if (bearing <= 30) {
                return NavigationDirection.SLIGHT_RIGHT;
            } else if (bearing <= 100) {
                return NavigationDirection.RIGHT;
            } else {
                return NavigationDirection.SHARP_RIGHT;
            }
        }

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
