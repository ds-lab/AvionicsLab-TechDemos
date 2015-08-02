package geodesy;

public class MathUtils {
    /** Returns the haversine of the argument */
    public static double hav(double x) {
        return (1 - Math.cos(x)) / 2;
    }
}