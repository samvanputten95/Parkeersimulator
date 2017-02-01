//package Parkeersimulator;
/*
 * Author Par-Kings
 * Version 2017.1.25
 */
import java.util.Random;
import java.awt.*;

public class AdHocCar extends Car {
	private static final Color COLOR=Color.red;
	
    public AdHocCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
/*
* @param stayMinutes
*/
    	this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
    }
    
    public Color getColor(){
/*
 * @return COLOR
 */
    	return COLOR;
    }
}
