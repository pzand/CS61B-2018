import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Node {
    private final double lon;
    private final double lat;
    private final long id;
    LinkedList<Long> adjacent;
    Map<String, String> otherInformation;

    public Node(long id, double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
        this.id = id;
        adjacent = new LinkedList<>();
        otherInformation = new HashMap<>();
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public long getId() {
        return id;
    }

    public void setAdjacent(long v) {
        adjacent.add(v);
    }

    public boolean haveAdjacent() {
        return !adjacent.isEmpty();
    }
    public void setInformation(String name, String value) {
        otherInformation.put(name, value);
    }

    public Iterable<Long> getAdjacent() {
        return adjacent;
    }
}