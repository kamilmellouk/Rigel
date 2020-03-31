package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bastien Faivre (310929)
 * @author Kamil Mellouk (312327)
 */
public final class ObservedSky {

    private final StereographicProjection stereographicProjection;

    private final Sun sun;

    private final Map<CartesianCoordinates, CelestialObject> posObjectMap = new HashMap<>();

    public ObservedSky(ZonedDateTime obsTime, GeographicCoordinates obsPos, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {

        this.stereographicProjection = stereographicProjection;

        sun = SunModel.SUN.at(Epoch.J2010.daysUntil(obsTime), new EclipticToEquatorialConversion(obsTime));
        posObjectMap.put(
                stereographicProjection.apply((new EquatorialToHorizontalConversion(obsTime, obsPos)).apply(sun.equatorialPos())),
                sun
                );


    }

    public Sun sun() {
        return new Sun(sun.eclipticPos(), sun.equatorialPos(), (float)sun.angularSize(), sun.meanAnomaly());
    }

    public CartesianCoordinates sunPosition() {
        return null;
    }
}
