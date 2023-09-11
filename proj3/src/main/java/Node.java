import java.util.*;

public class Node {
    private final double lon;
    private final double lat;
    private final long id;
    private LinkedList<Long> adjacent;
    private Map<String, String> otherInformation;
    private Set<String> roadName;

    public Node(long id, double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
        this.id = id;
        adjacent = new LinkedList<>();
        otherInformation = new HashMap<>();
        roadName = new HashSet<>();
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
    public void setRoadName(String name) {
        roadName.add(name);
    }

    public Set<String> roadName() {
        return new HashSet<>(roadName);
    }

    public Iterable<Long> getAdjacent() {
        return adjacent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}