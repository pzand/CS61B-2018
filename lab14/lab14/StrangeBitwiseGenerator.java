package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private final int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
    }

    // the range is [-1, 1)
    @Override
    public double next() {
        state = state + 1;
        int weirdState = state & (state >>> 3) % period;
//        weirdState = state & (state >> 7) % period;
//        weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState);
    }

    private double normalize(int state) {
        // 直线的斜率方程 y = kx + b, 其中k = (y2 - y1) / (x2 - x1)
        return 2.0 / period * state + (-1);
    }
}
