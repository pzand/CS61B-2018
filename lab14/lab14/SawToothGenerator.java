package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {

    private final int period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    // the range is [-1, 1)
    @Override
    public double next() {
        state += 1;
        return normalize(state % period);
    }

    private double normalize(int state) {
        // 直线的斜率方程 y = kx + b, 其中k = (y2 - y1) / (x2 - x1)
        return 2.0 / period * state + (-1);
    }
}
