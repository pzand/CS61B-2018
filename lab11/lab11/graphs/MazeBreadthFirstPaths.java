package lab11.graphs;

import java.util.LinkedList;

/**
 * @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int source;
    private int target;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        source = m.xyTo1D(sourceX, sourceY);
        target = m.xyTo1D(targetX, targetY);
    }

    /**
     * Conducts a breadth first search of the maze starting at the source.
     */
    private void bfs() {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        LinkedList<Integer> vertex = new LinkedList<>();
        vertex.add(source);
        distTo[source] = 1;
        edgeTo[source] = source;
        marked[source] = true;
        announce();

        while (!vertex.isEmpty()) {
            int node = vertex.removeFirst();

            if (target == node) {
                break;
            }

            for (Integer adj : maze.adj(node)) {
                if (!marked[adj]) {

                    vertex.addLast(adj);
                    distTo[adj] = distTo[node] + 1;
                    edgeTo[adj] = node;
                    marked[adj] = true;
                }
            }
            announce();
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

