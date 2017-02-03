package model;

import java.awt.Color;
import java.util.Random;

public class AbboCar extends Car {
	private static final Color COLOR=Color.green;
	
    public AbboCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 4 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }
    
    public Color getColor(){
    	return COLOR;
    }
}
