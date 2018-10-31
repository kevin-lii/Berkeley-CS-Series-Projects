import java.util.HashSet;

public class Node {
    long id;
    double lon;
    double lat;
    HashSet<Long> connected;

    public Node(long id, double lon, double lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        connected = new HashSet<>();
    }
    public long getID() {
        return this.id;
    }
    public double getLon() {
        return this.lon;
    }
    public double getLat() {
        return this.lat;
    }
    public HashSet<Long> getConnection() {
        return connected;
    }
    public void addConnection(Long addedID) {
        if (!connected.contains(addedID) && this.id != addedID) {
            connected.add(addedID);
        }
    }
}