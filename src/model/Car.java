package model;

import java.awt.*;
/*
 * 
 *  dit is Car.java
 */

public abstract class Car {

    private Location location;
    private int minutesLeft;
    private boolean isPaying;
    private boolean hasToPay;

    /**
     * Constructor voor object class Car
     */
    public Car() {

    }
    /*
     *  laat zien de locaties van de auto
     */
    public Location getLocation() {
        return location;
    }
    	/*
    	 * geef een locatie aan een auto
    	 */
    public void setLocation(Location location) {
        this.location = location;
    }

	/*
	 *  returneert hoeveel minuten er nog overbleeft
	 */
    public int getMinutesLeft() {
        return minutesLeft;
    }
    /*
     * zet hoeveel minuten er nog over is
     */

    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }

    /*
     * returneert ja of nee
     */
    public boolean getIsPaying() {
        return isPaying;
    }
    /*
     *  het is een boolean die kijkt of hij betaalt ja of nee
     */

    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }
    /*
     * returneert hoeveel betalt moet worden
     */
    public boolean getHasToPay() {
        return hasToPay;
    }
    	/*
    	 * zet hoeveel er betaald moet worden
    	 */
    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }
    /*
     *  elke keer als het geroept wordt haalt het -1 
     */
    public void tick() {
        minutesLeft--;
    }
    
    /*
     *  haalt de kleur op
     */
    public abstract Color getColor();
}
