package lab11.graphs;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean isCycle;
    private boolean temp;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        for (int i = 0; i < maze.V(); i++) {
            if (temp) {
                break;
            }

            if (marked[i]) {
                continue;
            }

            dfs(i, i);
        }
    }

    private void dfs(int node, int source) {
        if (marked[node]) {
            isCycle = true;
            marked[node] = false;
            edgeTo[node] = node;
            distTo[node] = 1;
            announce();
            return;
        }

        marked[node] = true;
        for (Integer adj : maze.adj(node)) {
            if (source == adj) {
                continue;
            }
            if (temp) {
                return;
            }

            dfs(adj, node);

            if (isCycle) {
                edgeTo[adj] = node;
                distTo[adj] = distTo[node] + 1;
                announce();
                break;
            }
        }

        if (!marked[node]) {
            marked[node] = true;
            temp = true;
        }
    }

    // Helper methods go here
}

