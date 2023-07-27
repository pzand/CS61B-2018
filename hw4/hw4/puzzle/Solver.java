package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.HashMap;
import java.util.LinkedList;

public class Solver {
    private MinPQ<WorldStateMap> minPQ;
    private WorldStateMap minWSM;

    public Solver(WorldState inital) {
        minPQ = new MinPQ<>();

        minPQ.insert(new WorldStateMap(inital, 0, null));
        solute();
    }

    public int moves() {
        return minWSM.getDepth();
    }

    public Iterable<WorldState> solution() {
        return getSolutions();
    }

    // 根据 worldState树 获得正确的路径
    private LinkedList<WorldState> getSolutions() {
        LinkedList<WorldState> list = new LinkedList<>();

        WorldStateMap wsm = minWSM;
        list.addFirst(wsm.getWorldState());

        while (wsm.getPreWorldState() != null) {
            wsm = wsm.getPreWorldState();
            list.addFirst(wsm.getWorldState());
        }
        return list;
    }

    private void solute() {
        // 判断之前是否已经存有该WorldState，同时映射该WorldState的深度，用于排除进堆
        HashMap<WorldState, Integer> map = new HashMap<>();

        minWSM = minPQ.delMin();
        WorldState minPriority = minWSM.getWorldState();
        map.put(minPriority, minWSM.getDepth());

        while (!minPriority.isGoal()) {
            int neighborDepth = minWSM.getDepth() + 1;
            for (WorldState neighbor : minPriority.neighbors()) {
                if (map.containsKey(neighbor) && map.get(neighbor) <= neighborDepth) {
                    continue;
                }

                WorldStateMap worldStateMap = new WorldStateMap(neighbor, neighborDepth, minWSM);
                map.put(neighbor, neighborDepth);
                minPQ.insert(worldStateMap);
            }

            minWSM = minPQ.delMin();
            minPriority = minWSM.getWorldState();
        }
    }

    // 包含父WorldState节点 深度depth 当前WorldState节点
    private class WorldStateMap implements Comparable {
        private final WorldState worldState;
        private final int depth;
        private final WorldStateMap preWorldState;
        private final int priority;

        public WorldStateMap(WorldState worldState, int depth, WorldStateMap preWorldState) {
            this.worldState = worldState;
            this.depth = depth;
            this.preWorldState = preWorldState;
            this.priority = worldState.estimatedDistanceToGoal() + depth;
        }

        public int getDepth() {
            return depth;
        }

        public WorldState getWorldState() {
            return worldState;
        }

        public WorldStateMap getPreWorldState() {
            return preWorldState;
        }

        private int priority() {
            return this.priority;
        }

        @Override
        public int compareTo(Object o) {
            if (!this.getClass().equals(o.getClass())) {
                throw new IllegalArgumentException("the class is not equal");
            }
            WorldStateMap O = (WorldStateMap) o;
            return this.priority() - O.priority();
        }
    }

}
