package lab14;

import lab14lib.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /** Your code here. */
//        // PS.
//        Generator generator = new StrangeBitwiseGenerator(1024);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(128000, 1000000);

        // 第三个任务的测试案例
        Generator generator = new StrangeBitwiseGenerator(512);
        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
        gav.drawAndPlay(4096, 1000000);

//        // 第二个任务的测试案例
//        Generator generator = new AcceleratingSawToothGenerator(200, 0.9);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);

//        // 第一个任务的测试案例
//        Generator generator = new SawToothGenerator(512);
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//        gav.drawAndPlay(4096, 1000000);

//        Generator g1 = new SineWaveGenerator(200);
//        Generator g2 = new SineWaveGenerator(201);
//
//        ArrayList<Generator> generators = new ArrayList<Generator>();
//        generators.add(g1);
//        generators.add(g2);
//        MultiGenerator mg = new MultiGenerator(generators);
//
//        GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(mg);
//        gav.drawAndPlay(500000, 1000000);

//		// 创建一个 440HZ 正弦波.
//		Generator generator = new SineWaveGenerator(440);
//		// 创建一个Player，去演奏generator
//		GeneratorPlayer gp = new GeneratorPlayer(generator);
//		// 告诉Player，把generator前1000000个样品 作为歌曲进行演奏
//		gp.play(1000000);

//		// 创建一个 440HZ 正弦波.
//		Generator generator = new SineWaveGenerator(200);
//		// 创建一个Drawer
//		GeneratorDrawer gd = new GeneratorDrawer(generator);
//		// 画出generator，前4096个样品
//		gd.draw(4096);

//		Generator generator = new SineWaveGenerator(200);
//		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//		// 你会看到前4096个样品被画出，前1000000个样品被播放
//		gav.drawAndPlay(4096, 1000000);

//		Generator generator = new SineWaveGenerator(200);
//		GeneratorAudioAnimator gaa = new GeneratorAudioAnimator(generator);
//		// 可以把输出绘成动画
//		gaa.drawAndPlay(1000, 100000);
    }
} 