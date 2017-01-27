//package Parkeersimulator;

public class Location {

    private int floor;
    private int row;
    private int place;

    /**
     * Constructor voor objects van de klasse class Location
     */
    public Location(int floor, int row, int place) {
        this.floor = floor;
        this.row = row;
        this.place = place;
    }

    /**
     * Implementeert inhoud qua gelijkheid.
     */
    public boolean equals(Object obj) {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return floor == other.getFloor() && row == other.getRow() && place == other.getPlace();
        }
        else {
            return false;
        }
    }

    /**
     * retouneert een string van  floor,row,place.
     * @return Een string gemaakt om de locatie te weergeven.
     */
    public String toString() {
        return floor + "," + row + "," + place;
    }

    /**
     * gebrukt 10 bits voor elke floor, row and place
     *  behalve voor grote pakeerplekken, dit geeft een unique hashcode voor (floor, row, place)
     * @return een hashcode van de locatie.
     */
    public int hashCode() {
        return (floor << 20) + (row << 10) + place;
    }

    /**
     * @return Retouneeert de floor.
     */
    public int getFloor() {
        return floor;
    }

    /**
     * @return Retouneeert de row.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return Retouneeert de place.
     */
    public int getPlace() {
        return place;
    }

}