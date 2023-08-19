import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    //    (i, j)      (width, height)
    //    (0, 0)  	  (1, 0)  	  (2, 0)
    //    (0, 1)  	  (1, 1)  	  (2, 1)
    //    (0, 2)  	  (1, 2)  	  (2, 2)
    //    (0, 3)  	  (1, 3)  	  (2, 3)
    //    width-->i  height-->j
    private Picture picture;
    double[][] pictureEnergy;
    private boolean havenCalculateEnergy;

    // current picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        havenCalculateEnergy = false;
        pictureEnergy = new double[width()][height()];
    }
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        isOutOfBound(x, y);

        if (pictureEnergy[x][y] == 0) {
            int deltaX = getDeltaX(x, y);
            int deltaY = getDeltaY(x, y);
            pictureEnergy[x][y] = deltaX * deltaX + deltaY * deltaY;
        }
        return pictureEnergy[x][y];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (!havenCalculateEnergy) {
            calculateEnergy();
        }
        FindSeamHelper fsh = new FindSeamHelper(pictureEnergy);
        return fsh.findSeam();
    }

    // sequence of indices for horizontal seam，动态规划求解最小能量路径
    public int[] findHorizontalSeam() {
        if (!havenCalculateEnergy) {
            calculateEnergy();
        }

        double[][] transposition = new double[height()][width()];
        for (int i = 0;i < width();i++) {
            for (int j = 0;j < height();j++) {
                transposition[j][i] = pictureEnergy[i][j];
            }
        }

        FindSeamHelper fsh = new FindSeamHelper(transposition);
        return fsh.findSeam();
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        isIllegalArgument(seam, width());

        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        isIllegalArgument(seam, height());

        picture = SeamRemover.removeVerticalSeam(picture, seam);
    }

    private void calculateEnergy() {
        for (int i = 0;i < width();i++) {
            for (int j = 0;j < height();j++) {
                pictureEnergy[i][j] = energy(i, j);
            }
        }
        havenCalculateEnergy = true;
    }

    private int getDeltaX(int x, int y) {
        int leftX = (width() + x - 1) % width();
        int rightX = (width() + x + 1) % width();
        Color leftPixelColor = picture.get(leftX, y);
        Color rightPixelColor = picture.get(rightX, y);
        return getDelta(leftPixelColor, rightPixelColor);
    }

    private int getDeltaY(int x, int y) {
        int topY = (height() + y - 1) % height();
        int bottomY = (height() + y + 1) % height();
        Color topPixelColor = picture.get(x, topY);
        Color bottomPixelColor = picture.get(x, bottomY);
        return getDelta(topPixelColor, bottomPixelColor);
    }

    private int getDelta(Color color1, Color color2) {
        int R = Math.abs(color1.getRed() - color2.getRed());
        int G = Math.abs(color1.getGreen() - color2.getGreen());
        int B = Math.abs(color1.getBlue() - color2.getBlue());
        return R + G + B;
    }

    private void isOutOfBound(int i, int j) {
        if (i < 0 || i >= width()) {
            throw new IndexOutOfBoundsException();
        }

        if (j < 0 || j >= height()) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void isIllegalArgument(int[] seam, int length) {
        if (seam.length != length) {
            throw new IllegalArgumentException();
        }

        for (int i = 1;i < length;i++) {
            if (Math.abs(seam[i - 1] - seam[i]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private class FindSeamHelper {
        private final double[][] pictureEnergy;
        private final int width;
        private final int height;
        private double[][] dp;
        private int[][] direction;
        public FindSeamHelper(double[][] pictureEnergy) {
            this.pictureEnergy = pictureEnergy;
            this.width = pictureEnergy.length;
            this.height = pictureEnergy[0].length;
            dp = new double[width][height];
            direction = new int[width][height];

            solve();
        }

        private int width() {
            return this.width;
        }

        private int height() {
            return this.height;
        }

        // 动态规划求解dp数组
        private void solve() {
            // dp[i][j] = energy[i][j] + min(dp[i - 1][j + 1], dp[i][j + 1], dp[i + 1][j + 1])
            // dp[i][height - 1] = energy[i][height -1];
            // 初始化dp数组
            for (int i = 0;i < width();i++) {
                dp[i][height() - 1] = getEnergy(i, height() - 1);
            }
            // 迭代求解
            for (int j = height() - 2;j >= 0;j--) {
                for (int i = 0; i < width; i++) {
                    double[] chooseEnergy = {
                            getEnergy(i - 1, j + 1),
                            getEnergy(i, j + 1),
                            getEnergy(i + 1, j + 1)
                    };
                    int minEnergyIndex = getMinIndexForArray(chooseEnergy) + i - 1;
                    direction[i][j] = minEnergyIndex;
                    dp[i][j] = getEnergy(i, j) + getEnergy(i + 1, minEnergyIndex);
                }
            }
        }
        // 是否越界
        private boolean isOutOfBound(int i, int j) {
            if (i < 0 || i >= width()) {
                return true;
            }
            if (j < 0 || j >= height()) {
                return true;
            }
            return false;
        }

        // 获取energy，越界返回无穷大
        private double getEnergy(int i, int j) {
            if (isOutOfBound(i, j)) {
                return Integer.MAX_VALUE;
            }
            return pictureEnergy[i][j];
        }

        // 根据数组获取最小下标
        private int getMinIndexForArray(double[] arr) {
            int minIndex = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] < arr[minIndex]) {
                    minIndex = i;
                }
            }
            return minIndex;
        }

        // 根据记录下来的数据进行回溯，寻找下标
        public int[] findSeam() {
            int[] minEnergy = new int[height()];

            double[] arr = new double[width()];
            for (int i = 0;i < width();i++) {
                arr[i] = dp[i][0];
            }
            minEnergy[0] = getMinIndexForArray(arr);

            for (int j = 1; j < height(); j++) {
                minEnergy[j] = direction[minEnergy[j - 1]][j - 1];
            }

            isIllegalArgument(minEnergy, minEnergy.length);

            return minEnergy;
        }
    }
}
