package model;

import java.util.Random;
import java.awt.*;

/**
 * Dit is de klasse ParkingPassCar
 * 
 * @author Zerin
 */

/**
 * 
 * Deze methode geeft de ParkinPassCar auto's een blauwe kleur.
 *
 */
public class ParkingPassCar extends Car {
	private static final Color COLOR=Color.blue;
	
	/**
	 * 
	 * Randomiseert de duur van het verblijf van de auto en geeft aan dat er niet betaald moet worden.
	 *
	 */
	
    public ParkingPassCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }
    
    /**
     * 
     * @return retourneert de kleur.
     */
    public Color getColor(){
    	return COLOR;
    }
}
