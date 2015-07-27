package geodesy;

import java.util.function.Function;

/**
 * Represents a coordinate on Earth as specified by latitude, longitude, and on
 * optional altitudes above mean sea level and above ground level.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class GeoCoordinate {
    private static final double EARTH_RADIUS = 6378.14;

    /** Geographical latitude in deg, positive northwards */
    private final double latitude;

    /** Geographical longitude in deg, positive eastwards */
    private final double longitude;

    /**
     * Constructs a new GeoCoordinate consisting of only latitude and longitude.
     * Altitude information is not available in the returned instance.
     *
     * @param latitude
     *            the geographical latitude in deg, positive northwards
     * @param longitude
     *            the geographical longitude in deg, positive eastwards
     *
     * @throws IllegalArgumentException
     *             if the latitude or longitude values are outside their
     *             respective valid ranges (-90°..+90° for latitude,
     *             -180°..+180° for longitude)
     */
    public GeoCoordinate(double latitude, double longitude) {
        if (Math.abs(latitude) > 90) {
            throw new IllegalArgumentException(
                    "Latitude must be between -90 deg and +90 deg, was "
                            + latitude);
        }
        if (Math.abs(longitude) > 180) {
            throw new IllegalArgumentException(
                    "Longitude must be between -180 deg and +180 deg, was "
                            + longitude);
        }

        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude in deg stored in this instance. Positive values
     * indicate northern latitude.
     *
     * @return the latitude in deg
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude in deg stored in this instance. Positive values
     * indicate eastern longitude.
     *
     * @return the latitude in deg
     */
    public double getLongitude() {
        return longitude;
    }

    /** Returns the haversine of the argument */
    private double hav(double x) {
        return (1 - Math.cos(x)) / 2;
    }

    /** Converts the argument from deg to rad */
    private double deg2rad(double deg) {
        return Math.PI * deg / 180.0;
    }

    /** Converts the argument from rad to deg */
    private double rad2deg(double rad) {
        return rad / Math.PI * 180.0;
    }

    /**
     * Returns the distance to a second coordinate.
     * 
     * @param other
     *            the second coordinate
     * @return distance in kilometers to the second coordinate
     */
    public double distanceTo(GeoCoordinate other) {
        final double dPhi = this.latitude - other.latitude;
        final double dLambda = this.longitude - other.longitude;

        final double havTetha = hav(deg2rad(dPhi))
                + Math.cos(deg2rad(this.latitude))
                * Math.cos(deg2rad(other.latitude)) * hav(deg2rad(dLambda));

        return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(havTetha));
    }

    /**
     * Returns the initial bearing towards a second coordinate.
     * 
     * @param other
     *            the second coordinate
     * @return initial bearing in deg to the second coordinate
     */
    public double initialBearingTo(GeoCoordinate other) {
        final double dLambda = this.longitude - other.longitude;

        final double S = Math.cos(deg2rad(other.latitude))
                * Math.sin(deg2rad(dLambda));
        final double C = Math.cos(deg2rad(this.latitude))
                * Math.sin(deg2rad(other.latitude))
                - Math.sin(deg2rad(this.latitude))
                * Math.cos(deg2rad(other.latitude))
                * Math.cos(deg2rad(dLambda));

        double bearing = rad2deg(Math.atan2(S, C));
        if (bearing < 0) {
            bearing += 360;
        }
        return bearing;
    }

    @Override
    public String toString() {
        final Function<Double, String> angleFormatter = angle -> {
            double val = angle;
            int degs = (int) val;
            int arcmins = (int) ((val - degs) * 60);
            double arcsecs = (val - degs - arcmins / 60f) * 3600;
            return String.format("%d° %d' %f\"", degs, arcmins, arcsecs);
        };
        return String
                .format("[lat = %s, lon = %s]", angleFormatter.apply(latitude),
                        angleFormatter.apply(longitude));
    }
}
