public class Planet {
    public static double Gravitaional = 6.67e-11;
    public double xxPos;       //its current x position
    public double yyPos;       //its current y position
    public double xxVel;       //its current velocity in x position
    public double yyVel;       //its current velocity in y position
    public double mass;        //its mass
    public String imgFileName; //the name of the file that corresponds to the image that depicts the planet
    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        xxPos = xP; yyPos = yP;
        xxVel = xV; yyVel = yV;
        mass = m;
        imgFileName = "./images/" + img;
    }
    public Planet(Planet P){
        this.xxPos = P.xxPos; this.yyPos = P.yyPos;
        this.xxVel = P.xxVel; this.yyVel = P.yyVel;
        this.mass = P.mass;
        this.imgFileName = P.imgFileName;
    }
    //To compare two planets is equals
    public Boolean equals(Planet p){
        return this == p;
    }
    //calculate the distance between two Planet
    public double calcDistance(Planet p){
        double x_distance = this.xxPos - p.xxPos;
        double y_distance = this.yyPos - p.yyPos;
        return Math.sqrt(x_distance * x_distance + y_distance * y_distance);
    }
    //calculate the Force between two Planet
    public double calcForceExertedBy(Planet p){
        double distance = this.calcDistance(p);
        return Planet.Gravitaional * (p.mass * this.mass) / (distance * distance);
    }
    //calculate the Force by X between two planet
    public double calcForceExertedByX(Planet p){
        double Dx = p.xxPos - this.xxPos;
        return this.calcForceExertedBy(p) * Dx / this.calcDistance(p);
    }
    //calculate the Force by Y between two planet
    public double calcForceExertedByY(Planet p){
        double Dy = p.yyPos - this.yyPos;
        return this.calcForceExertedBy(p) * Dy / this.calcDistance(p);
    }
    //calculate net Force X exerted by all planets in that array
    public double calcNetForceExertedByX(Planet[] allPlanet){
        double netForceByX = 0;
        for(Planet p : allPlanet){
            if(!this.equals(p)){
                netForceByX += this.calcForceExertedByX(p);
            }
        }
        return netForceByX;
    }
    public double calcNetForceExertedByY(Planet[] allPlanet){
        double netForceByY = 0;
        for(Planet p : allPlanet){
            if(!this.equals(p)){
                netForceByY += this.calcForceExertedByY(p);
            }
        }
        return netForceByY;
    }
    public void update(double time, double xxForce, double yyForce){
        //calculate the acceleration of X and Y
        double xxAcce = xxForce / this.mass;
        double yyAcce = yyForce / this.mass;

        //calculate the new velocity
        this.xxVel = this.xxVel + xxAcce * time;
        this.yyVel = this.yyVel + yyAcce * time;

        //calculate the new position
        this.xxPos = this.xxPos + this.xxVel * time;
        this.yyPos = this.yyPos + this.yyVel * time;
    }
    public void draw(){
        StdDraw.picture(this.xxPos, this.yyPos, this.imgFileName);
    }
}
