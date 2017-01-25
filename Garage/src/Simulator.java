//package Parkeersimulator;

import java.util.Random;
/**
 * 
 * @author Sam
 * @versie 1
 * 
 * Klasse van object simulator.
 */
public class Simulator {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	
	
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private SimulatorView simulatorView;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    private int tickPause = 100;

    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute
    
    /**
     * Constructor voor objecten van klasse simulator.
     */

    public Simulator() {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        simulatorView = new SimulatorView(3, 6, 30);
    }
    /**
     * Main-methode. Deze wordt gebruikt om het programma te starten.
     * @param args
     */
    public static void main(String[] args){
    	Simulator mySimulator = new Simulator();
    	mySimulator.run();
    }
/**
 * methode run: gebruikt een for-loop om de tijd in minuten toe te doen nemen. Er wordt 10000 keer geloopt.
 */
    public void run() {
        for (int i = 0; i < 10000; i++) {
            tick();
        }
    }
/**
 * methode tick: deze maakt gebruik van methoden advanceTime, handleExit, updateViews en handleEntrance.
 * Heeft ook een pauze-optie middels een catch-e.
 * 
 */
    private void tick() {
    	advanceTime();
    	handleExit();
    	updateViews();
    	// Pause.
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	handleEntrance();
    }
/**
 * methode advanceTime: void-methode om tijd te doen toenemen. Gebruikt while-loops: 
 * Elke keer dat 'minuut' boven de 60 komt komt er een uur bij.
 * Wanneer het aantal uren groter is dan 24 komt er een dag bij en wordt het aantal uren gelijkgesteld aan nul.
 * Wanneer het aantal dagen groter wordt dan 7 wordt dat aantal gelijkgesteld aan nul.
 */
    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }

    }
/**
 * methode handleEntrance. Maakt gebruik van methoden carsArriving en carsEntering om te controleren 
 * hoeveel auto's er aankomen bij en vertrekken uit de parkeergarage.
 */
    private void handleEntrance(){
    	carsArriving();
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
    }
    
    /**
     * methode handleExit. Maakt gebruik van methoden carsReadyToLeave,
     * carsPaying en carsLeaving om bij te houden hoeveel auto's er vertrekken.
     */
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    /**
     * updateViews: maakt gebruik van methode tick om de view elke minuut te veranderen. Daarna wordt de methode
     * in het eigen lichaam aangeroepen.
     */
    private void updateViews(){
    	simulatorView.tick();
        // Update the car park view.
        simulatorView.updateView();	
    }
    /**
     * methode carsArriving. Houdt bij hoeveel auto's er van elk type zijn aangekomen door de methode 
     * addArrivingCars aan te roepen. Daarna wordt numberOfCars gelijkgesteld aan hoeveel auto's er per type
     * zijn aangekomen in doordeweekse dagen en het weekend.
     */
    private void carsArriving(){
    	int numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);    	
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);    	
    }
/**
 * methode carsEntering: houdt door methode carsInQueue bij hoeveel auto's er aankomen
 * en wijst deze toe aan lege plekken in de parkeergarage (hierbij wordt tegelijk de auto uit de queue verwijderd).
 * @param queue
 */
    private void carsEntering(CarQueue queue){
        int i=0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue()>0 && 
    			simulatorView.getNumberOfOpenSpots()>0 && 
    			i<enterSpeed) {
            Car car = queue.removeCar();
            Location freeLocation = simulatorView.getFirstFreeLocation();
            simulatorView.setCarAt(freeLocation, car);
            i++;
        }
    }
    /**
     * methode carsReadyToLeave. Door middel van een while loop wordt gecheckt of een auto vertrekt en nog moet betalen,
     * wanneer een auto niet betaald wordt deze toegevoegd aan de paymentCarQueue. Wanneer er is betaald
     * vertrekt de auto van de plek. Daarna wordt middels de simulatorview het gekleurde vierkantje verwijderd.
     */
    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        Car car = simulatorView.getFirstLeavingCar();
        while (car!=null) {
        	if (car.getHasToPay()){
	            car.setIsPaying(true);
	            paymentCarQueue.addCar(car);
        	}
        	else {
        		carLeavesSpot(car);
        	}
            car = simulatorView.getFirstLeavingCar();
        }
    }
/**
 * methode carsPaying: laat auto's betalen door middel van een while loop. 
 * Als een auto in de payMentQueue zit wordt die er middels deze methode uitgehaald.
 */
    private void carsPaying(){
        // Let cars pay.
    	int i=0;
    	while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){
            Car car = paymentCarQueue.removeCar();
            // TODO Handle payment.
            carLeavesSpot(car);
            i++;
    	}
    }
/**
 * Methode carsLeaving: maakt gebruik van while loop om auto's uit de exitCarQueue te verwijderen. 
 * 
 */
    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
    	}	
    }
    /**
     * methode getNumberOfCars: houdt bij hoeveel auto's er gemiddeld per uur en per minuut aankomen.
     * @param weekDay
     * @param weekend
     * @return aantal auto's dat per uur aankomt.
     */
    private int getNumberOfCars(int weekDay, int weekend){
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }
    /**
     * Methode addArriving. Door middel van een switch wordt bepaald hoeveel van elk type auto er binnenkomen.
     * De entranceQueue neemt met 1 toe per toegevoegde auto.
     * @param numberOfCars
     * @param type
     */
    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
    	switch(type) {
    	case AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	entranceCarQueue.addCar(new AdHocCar());
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar());
            }
            break;	            
    	}
    }
    /**
     * Methode carLeavesSpot. Haalt een object car weg uit een bepaalde locatie
     * en voegt deze toe aan de exitQueue (vertrekkende rij).
     * @param car
     */
    private void carLeavesSpot(Car car){
    	simulatorView.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
        //test commentline
    }

}
