import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;
import java.util.List;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    private static final Map<Long, Node> map = new LinkedHashMap<>();
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public void addNode(Node node) {
        map.put(node.getID(), node);
    }
    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Object[] mapper = map.values().toArray();
        for (int i = 0; i < mapper.length; i++) {
            Node node = (Node) mapper[i];
            if (node.getConnection().size() == 0) {
                map.remove(node.getID());
            }
        }
    }


    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return map.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return map.get(v).getConnection();
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }
    static double distance(Node start, Node end) {
        return distance(start.getLon(), start.getLat(), end.getLon(), end.getLat());
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        Node node = null;
        ArrayList<Long> list = new ArrayList<>();
        list.addAll(map.keySet());
        KdTree tree = new KdTree(list , true);
        return tree.convert(tree.nearestNeighbor(tree.root, lon, lat));
//        double maxDistance = Double.MAX_VALUE;
//        for (long vertex : vertices()) {
//            Node testNode = map.get(vertex);
//            if (maxDistance > distance(lon, lat, testNode.getLon(), testNode.getLat())) {
//                maxDistance = distance(lon, lat, testNode.getLon(), testNode.getLat());
//                node = testNode;
//            }
//        }
//        return node.getID();
    }
    public class KdTree {

        public class KDNode {
            KDNode right;
            KDNode left;
            boolean xsided;
            Point2D.Double point;

            public KDNode(boolean xsided, Point2D.Double point) {
                this.xsided = xsided;
                this.point = point;
            }
        }
        private KDNode root;
        private int size;
        private HashMap<Point2D.Double, Node> map = new HashMap<>();

        public KdTree(List<Long> nodes, boolean xsided) {
            List<Point2D.Double> list = new ArrayList<>();
            for (Long node : nodes) {
                double x = getNode(node).getLon();
                double y = getNode(node).getLat();
                Point2D.Double p = new Point2D.Double(x, y);
                map.put(p, getNode(node));
                list.add(p);
            }
            Comparator<Point2D.Double> comparator = Comparator.comparingDouble((point) -> point.getX());
            list.sort(comparator);
            Point2D.Double median = list.get(list.size() / 2);
            root = new KDNode(xsided, median);
            root.left = constructNode(list.subList(0, list.size() / 2), !xsided);
            root.right = constructNode(list.subList(list.size() / 2 + 1, list.size()), !xsided);
        }
        private KDNode constructNode(List<Point2D.Double> list, boolean xsided) {
            if (list.size() == 0) {
                return null;
            } else {
                Comparator<Point2D.Double> comparator;
                if (xsided) {
                    comparator = Comparator.comparingDouble((point) -> point.getX());
                } else {
                    comparator = Comparator.comparingDouble((point) -> point.getY());
                }
                list.sort(comparator);
                Point2D.Double median = list.get(list.size() / 2);
                KDNode node = new KDNode(xsided, median);
                node.left = constructNode(list.subList(0, list.size() / 2), !xsided);
                node.right = constructNode(list.subList(list.size() / 2 + 1, list.size()), !xsided);
                return node;
            }
        }
        public KDNode nearestNeighbor(KDNode testNode, double x, double y) {
            if (testNode.right == null && testNode.left == null) {
                return testNode;
            }
            KDNode best = null;
            boolean goesLeft = false;
            if (testNode.xsided) {
                if (x < testNode.point.getX() && testNode.left != null) {
                    best = nearestNeighbor(testNode.left, x, y);
                    goesLeft = true;
                } else if ( x > testNode.point.getX() && testNode.right != null){
                    best = nearestNeighbor(testNode.right, x, y);
                    goesLeft = false;
                }
            } else {
                if (y < testNode.point.getY() && testNode.left != null) {
                    best = nearestNeighbor(testNode.left, x, y);
                    goesLeft = true;
                } else if (y > testNode.point.getY() && testNode.right != null){
                    best = nearestNeighbor(testNode.right, x, y);
                    goesLeft = false;
                }
            }
            if (best != testNode) {
                if (best != null && distance(testNode.point.getX(), x, testNode.point.getY(), y) < distance(best.point.getX(), x, best.point.getY(), y)) {
                    best = testNode;
                }
            }
            double crossSplitLine;
            KDNode testBest = null;
            if (testNode.xsided) {
                crossSplitLine = Math.abs(x - testNode.point.getX());
            } else {
                crossSplitLine = Math.abs(y - testNode.point.getY());
            }
            if (best != null && crossSplitLine < distance(best.point.getX(), x, best.point.getY(), y)) {
                if (!goesLeft && testNode.left != null) {
                    testBest = nearestNeighbor(testNode.left, x, y);
                } else if (goesLeft && testNode.right != null) {
                    testBest = nearestNeighbor(testNode.right, x, y);
                } if (testBest != null && distance(testBest.point.getX(), x, testBest.point.getY(), y) < distance(best.point.getX(), x, best.point.getY(), y)) {
                    best = testBest;
                }
            }
            return best;
        }
        public long convert(KDNode node) {
            return map.get(node.point).getID();
        }
    }
    Node getNode(long id) {
        return map.get(id);
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return map.get(v).getLon();
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return map.get(v).getLat();
    }

    public void addConnections(Edge edge) {
        ArrayList nodes = edge.nodes;
        if (nodes.size() == 1) {
            return;
        }
        if (nodes.size() == 2) {
            Node first = map.get(nodes.get(0));
            Node second = map.get(nodes.get(1));
            first.addConnection(second.getID());
            second.addConnection(first.getID());
        }
        for (int i = 1; i < nodes.size() - 1; i++) {
            Node curr = map.get(nodes.get(i));
            Node prev = map.get(nodes.get(i - 1));
            Node next = map.get(nodes.get(i + 1));
            curr.addConnection(prev.getID());
            curr.addConnection(next.getID());
            prev.addConnection(curr.getID());
            next.addConnection(curr.getID());
        }
    }
}
