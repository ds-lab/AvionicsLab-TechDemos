package geodesy;

public class Problem7 {
    private static final double KM_PER_NM = 1.852;
    private static final double SEA_MAG_DEV = -16.1;

    public static void main(String[] args) {
        final GeoCoordinate SEA = new GeoCoordinate(47.435333, 122.309667);
        final GeoCoordinate BTG = new GeoCoordinate(45.747833, 122.591500);

        final double distanceKm = SEA.distanceTo(BTG);
        final double distanceNm = distanceKm / KM_PER_NM;
        
        final double trueBearing = SEA.initialBearingTo(BTG);
        final double magHdg = trueBearing + SEA_MAG_DEV;

        System.out.println("SEA VORTAC: " + SEA);
        System.out.println("BTG VORTAC: " + BTG);
        System.out.printf("Distance SEA-BTG: %.2f km = %.2f nm\n", distanceKm,
                distanceNm);
        System.out.printf("True Bearing SEA->BTG: %.1f°\n", trueBearing);
        System.out.printf("Mag Heading SEA->BTG: %.1f°\n", magHdg);
    }
}
