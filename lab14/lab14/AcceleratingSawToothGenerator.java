package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {

    private int state;
    private double period;
    // the range is [-1, 1). After resetting, the period should change by a factor of second argument
    @Override
    public double next() {
        return 0;
    }
}
