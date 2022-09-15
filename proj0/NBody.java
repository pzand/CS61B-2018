public class NBody {
    public static Double readRadius(String fileName){
        In file = new In(fileName);
        file.readInt();
        return file.readDouble();
    }
    public static Planet[] readPlanets(String fileName){
        In file = new In(fileName);
        int n = file.readInt();
        double a = file.readDouble();

        Planet[] planets = new Planet[n];
        for(int i = 0;i < n;i++){
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

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();

        StdDraw.picture(0, 0, "./images/starfield.jpg");
        Planet[] planets = readPlanets(filename);

        for(Planet p : planets){
            p.draw();
        }
    }
}
