package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {

    private int state;
    private double period;
    private final double factor;
    // the range is [-1, 1). After resetting, the period should change by a factor of second argument


    public AcceleratingSawToothGenerator(double period, double factor) {
        this.period = period;
        this.factor = factor;
    }

    // 更新目前的周期时，将state归为初始状态，period周期乘上倍率
    @Override
    public double next() {
        state += 1;
        if (state >= period) {
            state = (int)(state % period);
            period = period * factor;
        }
        return normalize(state, period);
    }

    // 该周期直线的方程
    private double normalize(int state, double period) {
        return (2.0 / period) * state + (-1);
    }
}
