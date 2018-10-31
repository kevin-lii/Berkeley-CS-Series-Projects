import javax.management.Query;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    int degreeToFeet = 288200;
    public Rasterer() {

    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        double uLLon = params.get("ullon");
        double lRLon = params.get("lrlon");
        double width = params.get("w");
        double height = params.get("h");
        double uLLat = params.get("ullat");
        double lRLat = params.get("lrlat");
        if (!checkQuery(uLLon, lRLon, uLLat, lRLat)) {
            return generateEmptyFalse();
        }
        uLLon = Math.max(params.get("ullon"), MapServer.ROOT_ULLON);
        lRLon = Math.min(params.get("lrlon"), MapServer.ROOT_LRLON);
        uLLat = Math.min(params.get("ullat"), MapServer.ROOT_ULLAT);
        lRLat = Math.max(params.get("lrlat"), MapServer.ROOT_LRLAT);
        double lonDPP = (lRLon - uLLon) / width;
        int depth;
        for (depth = 0; depth < 7; depth++) {
            double testLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / (Math.pow(2, depth) * MapServer.TILE_SIZE);
            if (lonDPP >= testLonDPP) {
                break;
            }
        }
        double lonHeight = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        double latWidth = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT ) / Math.pow(2, depth);
        int startX = (int) Math.floor((uLLon - MapServer.ROOT_ULLON) / lonHeight);
        int startY = (int) Math.floor((MapServer.ROOT_ULLAT - uLLat) / latWidth);

        double lonDist = lRLon - ((lonHeight * (double) (startX + 1)) + MapServer.ROOT_ULLON);
        double latDist = (MapServer.ROOT_ULLAT - (latWidth * (double) (startY + 1))) - lRLat;

        int endX = (int) Math.ceil(lonDist / lonHeight) + startX;
        int endY = (int) Math.ceil(latDist / latWidth) + startY;

        results.put("raster_ul_lon", (double) startX * lonHeight + MapServer.ROOT_ULLON);
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT - (double) startY * latWidth);
        results.put("raster_lr_lon", (double) (endX + 1) * lonHeight + MapServer.ROOT_ULLON);
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT - (double) (endY + 1) * latWidth);
        results.put("render_grid", generateImageTexts(startX, startY, endX, endY, depth));
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }
    private String[][] generateImageTexts(int sX, int sY, int eX, int eY, int d) {
        int xLength = eX - sX + 1;
        int yLength = eY - sY + 1;
        String[][] map = new String[yLength][xLength];

        for (int row = 0; row < yLength; row++) {
            for (int col = 0; col < xLength; col++) {
                int x = sX + col;
                int y = sY + row;
                String img = "d" + d + "_x" + x + "_y" + y + ".png";
                map[row][col] = img;
            }
        }

        return map;
    }
    private boolean checkQuery(double ulln, double lrln, double ullt, double lrlt) {
        if (lrln < ulln) {
            return false;
        }

        if (lrlt > ullt) {
            return false;
        }

        boolean start = (ulln >= MapServer.ROOT_ULLON) || (ullt <= MapServer.ROOT_ULLAT);
        boolean end = (lrln <= MapServer.ROOT_LRLON) || (lrlt >= MapServer.ROOT_LRLAT);
        return start && end;
    }
    private Map<String, Object> generateEmptyFalse() {
        Map<String, Object> results = new HashMap<>();
        results.put("query_success", false);
        results.put("render_grid", new String[1][1]);
        results.put("depth", 1);
        results.put("raster_ul_lon", 0.0);
        results.put("raster_ul_lat", 0.0);
        results.put("raster_lr_lon", 0.0);
        results.put("raster_lr_lat", 0.0);
        return results;
    }
}

