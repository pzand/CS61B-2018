import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // D表述缩放
    // 在D级缩放中会有4^D个图片，其命名范围为：dD_x0_y0 到 dD_xk_yk，其中k为 2^D - 1
    // x从零增长到k，图像向东移。y从零增长到k，图像向南移
    private double lonDPP;
    private int sumPicture;
    private double scaleX;
    double scaleY;
    private final static int SL = 288200;
    private final double D0_RESOLUTION;
    private int depth;
    private double raster_ullon;
    private double raster_ullat;
    private double raster_lrlon;
    private double raster_lrlat;
    private boolean query_success;
    private String[][] grid;

    public Rasterer() {
        // YOUR CODE HERE
        init();
        D0_RESOLUTION = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) * SL / MapServer.TILE_SIZE;
    }

    private void init() {
        this.depth = 0;
        this.raster_lrlat = 0.0;
        this.raster_ullat = 0.0;
        this.raster_lrlon = 0.0;
        this.raster_ullon = 0.0;
        query_success = false;
        grid = null;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                + "your browser.");

        init();
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double w = params.get("w");
        double h = params.get("h");

        lonDPP = (lrlon - ullon) / w * SL;

        depth = computeDepth();
        Map<String, Integer> position = actualPosition(ullon, ullat, lrlon, lrlat);

        if (validPosition(position)) {
            int rightXPosition = position.get("rightXPosition");
            int lowerYPosition = position.get("lowerYPosition");
            int leftXPosition = position.get("leftXPosition");
            int upperYPosition = position.get("upperYPosition");

            raster_lrlon = MapServer.ROOT_ULLON + rightXPosition * scaleX;
            raster_ullon = MapServer.ROOT_ULLON + leftXPosition * scaleX;
            raster_lrlat = MapServer.ROOT_LRLAT + lowerYPosition * scaleY;
            raster_ullat = MapServer.ROOT_LRLAT + upperYPosition * scaleY;
            grid = getGrid(leftXPosition, rightXPosition, lowerYPosition, upperYPosition);
            query_success = true;
        }

        results.put("depth", depth);
        results.put("render_grid", grid);
        results.put("raster_ul_lon", raster_ullon);
        results.put("raster_ul_lat", raster_ullat);
        results.put("raster_lr_lon", raster_lrlon);
        results.put("raster_lr_lat", raster_lrlat);
        results.put("query_success", query_success);
        return results;
    }

    private boolean validPosition(Map<String, Integer> position) {
        if (position.get("leftXPosition") >= position.get("rightXPosition")) {
            return false;
        }
        if (position.get("lowerYPosition") >= position.get("upperYPosition")) {
            return false;
        }
        return true;
    }

    private Map<String, Integer> actualPosition(double ullon, double ullat, double lrlon, double lrlat) {
        // 该深度每张图片的经度比例 = (初始右边经度 - 初始左边进度) / sum(划分份数)，其中份数为2^depth
        // 单位：经度每张图
        // 理想的位置 * 比例 + 初始左侧经度 = 需要的经度 -> 理想位置 = (需求经度 - 初始经度) / 比例
        // 理想的位置 * 比例 + 初始下侧纬度 = 需要的纬度 -> 理想位置 = (需求经度 - 初始经度) / 比例
        // 左侧、下侧需要向上取整，右侧、上侧需要向上取整

//        您很可能至少会混淆一次纬度和经度或 x 和 y。您还可能会混淆 y 的向上方向和向下方向。请务必检查一下！
        scaleX = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / sumPicture;
        scaleY = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / sumPicture;

        int leftXPosition = (int) Math.floor((ullon - MapServer.ROOT_ULLON) / scaleX);
        int rightXPosition = (int) Math.ceil((lrlon - MapServer.ROOT_ULLON) / scaleX);
        int upperYPosition = (int) Math.ceil((ullat - MapServer.ROOT_LRLAT) / scaleY);
        int lowerYPosition = (int) Math.floor((lrlat - MapServer.ROOT_LRLAT) / scaleY);

        leftXPosition = handleOutOfBound(leftXPosition);
        rightXPosition = handleOutOfBound(rightXPosition);
        upperYPosition = handleOutOfBound(upperYPosition);
        lowerYPosition = handleOutOfBound(lowerYPosition);

        Map<String, Integer> map = new HashMap<>();
        map.put("leftXPosition", leftXPosition);
        map.put("rightXPosition", rightXPosition);
        map.put("upperYPosition", upperYPosition);
        map.put("lowerYPosition", lowerYPosition);
        return map;
    }

    private int handleOutOfBound(int position) {
        if (position < 0) {
            return 0;
        }
        return Math.min(position, sumPicture);
    }

    private int computeDepth() {
        // 对于第一层D0 其比例尺为，(-122.2339 + 122.256) * 288200 / 256 = 98.94 英尺每像素
        // 其中，288200为(经度转为英寸的比例常数)，256为(图片的像素). 下一层为98.94 / 2 = 49.47，以此类推
        // 要保证 提供图片的比例尺(最大深度为7) 小等于 目标的比例尺
        // 98.94 / 2^D <= lonDPP  -> D = log(98.9 / lonDPP)
        double exceptedDepth = Math.ceil(Math.log(D0_RESOLUTION / lonDPP) / Math.log(2));

        // 避免越界
        int actualDepth = Math.min((int) exceptedDepth, 7);
        actualDepth = Math.max(actualDepth, 0);

        // 同步计算需要的属性
        sumPicture = (int) Math.pow(2, actualDepth);

        return actualDepth;
    }

    private String[][] getGrid(int leftX, int rightX, int lowerY, int upperY) {
        String prefix = "d" + depth + "_x";
        int subX = rightX - leftX;
        int subY = upperY - lowerY;
        // 因为y轴是从上到下的，即从高纬到低纬
        // upperY代表的是 第upperY-1格，因此为 (sum - 1) - (upperY - 1)
        int startY = sumPicture - upperY;

        String[][] grid = new String[subY][subX];
        for (int i = 0; i < subY; i++) {
            for (int j = 0; j < subX; j++) {
                grid[i][j] = prefix + (leftX + j) + "_y" + (startY + i) + ".png";
            }
        }
        return grid;
    }

}
