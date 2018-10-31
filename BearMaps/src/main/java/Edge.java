import java.util.ArrayList;

public class Edge {
    long id;
    boolean flag;
    String maxSpeed;
    String name;
    ArrayList nodes = new ArrayList();

    public Edge(long id) {
        this.id = id;
    }
    public void addNode(long ref) {
        if (ref != id) {
            nodes.add(ref);
        }
    }
    public String getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public boolean getFlag() {
        return this.flag;
    }
    public void setName(String name) {
        this.name = name;
    }
}
