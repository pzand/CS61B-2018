public class NBody {
    public static double readRadius(String fileName) {
        In file = new In(fileName);
        file.readInt();
        return file.readDouble();
    }

    public static Planet[] readPlanets(String fileName) {
        In file = new In(fileName);
        int n = file.readInt();
        file.readDouble();

        Planet[] planets = new Planet[n];
        for (int i = 0; i < n; i++) {
            planets[i] = new Planet(file.readDouble(), file.readDouble(), file.readDouble(),
                    file.readDouble(), file.readDouble(), file.readString());
        }
        return planets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        StdDraw.enableDoubleBuffering();        //enable double buffering
        StdDraw.setScale(-radius, radius);      //set radius of universe
        StdDraw.clear();

        StdDraw.picture(0, 0, "./images/starfield.jpg");    //drawing the background
        Planet[] planets = readPlanets(filename);

        for (Planet p : planets) {                //Drawing All of the planets
            p.draw();
        }
        StdDraw.show();

        for (double nowtime = 0; nowtime < T; nowtime += dt) {
            double[] xForce = new double[planets.length];
            double[] yForce = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                xForce[i] = planets[i].calcNetForceExertedByX(planets);
                yForce[i] = planets[i].calcNetForceExertedByY(planets);
            }

            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForce[i], yForce[i]);
            }

            StdDraw.picture(0, 0, "./images/starfield.jpg");    //drawing the background
            for (Planet p : planets) {                //Drawing All of the planets
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(1);
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
