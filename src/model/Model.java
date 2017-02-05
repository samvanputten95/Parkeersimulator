package model;
import java.awt.*;

import java.util.Random;

import view.*;
import controller.Controller;

import javax.swing.*;
/*
* Klasse model
* Author: Par-Kings
* Version: 4-2-2017
*/
public class Model {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	private static final String ABBO = "3";
	
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue entranceAbboQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private MainWindow simulatorView;
   
    private boolean run = false;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    
    private int aantalCars = 0;
    private int aantalAdHoc = 0;
    private int aantalPass = 0;
    private int aantalAbbo = 0;
    
    private int opbrengst = 0;

    private int tickPause = 300;

    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 10; // average number of arriving cars per hour
    int weekDayAbboArrivals = 25;//average number of arriving cars per hour
    int weekendAbboArrivals = 5;//average number of arriving cars per hour
    int enterSpeed = 2; // number of cars that can enter per minute
    int paymentSpeed = 5; // number of cars that can pay per minute
    int exitSpeed = 3; // number of cars that can leave per minute

  
     /**
     * Constructor voor objecten van klasse model.
     */
    
    public Model() {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        entranceAbboQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        simulatorView = new MainWindow(3, 6, 30, this);     
    }
    /*
    * Methode om opbrengst weer te geven.
    */
    public int getOpbrengst() {
        return opbrengst;
    }
    
    /**
     * methode om totaalaantal cars op te halen.
     * @return Cars
     */	
	
    public int getCars(){
    	aantalCars = aantalAdHoc + aantalPass + aantalAbbo;
    	return aantalCars;
    }
   
     /**
     * methode om aantal AdHocCars op te halen.
     * @return aantalAdHoc
     */
	
    public int getAdHoc(){
    	return aantalAdHoc;
    }
    
    /**
     * methode om aantal ParkingPassCars op te halen.
     * @return aantalPass
     */
	
    public int getPass(){
    	return aantalPass;
    }
    
    /**
     * methode om aantal AbboCars op te halen.
     * @return aantalAbbo
     */
	
    public int getAbbo(){
    	return aantalAbbo;
    }
    
    /**
    * methode om aantal minuten op te halen.
    * @return minute
    */	
	
    public int getMinuut(){
    	return minute;
    }
    
    /**
    * methode om aantal dagen op te halen.
    * @return dag
    */	
	
    public int getDag(){
    	return day;
    }
    
    /**
    * methode om aantal uren op te halen.
    * @return uur
    */	
	
    public int getUur(){
    	return hour;
    }
	
    /*
     * methode start: deze stelt de boolean run in op true.
     * Hierdoor runt de simulator.
     */
	
    public void start(){
    	
    	run = true;
    	run(10000);
    }

    /**
     * methode run: gebruikt een for-loop om de tijd in minuten toe te doen nemen. 
     * Er wordt 10000 keer geloopt zoals aangegeven in de startmethode.
     */
	
    public void run(int stappen) {
        for (int i = 0; i < stappen; i++) {
        	if(run == true){
            tick();
        	}
        	else{
        		break;
        	}
        }
    	
    }
	
    /*
     * methode pause: stelt boolean run in op false.
     * Hierdoor stopt de simulator, deze kan alleen lopen 
     * als run = true.
     */
    
    public void pause(){
    	run = false;
    }
    
    /*
     * methode step: zorgt ervoor dat de simulator met 1 tick verloopt.
     */
	
    public void step(){
    	
    	for(int i = 0; i < 1; i++){
    		tick();
    	}
    }
    /*
    *methode uur: zorgt ervoor dat de simulatie 60 ticks verder loopt.
    */
    public void uur(){
    	
    	for(int i = 0; i < 60; i++){
    		tick();
    	}
    }
    
    /*
     * methode quit: zorgt ervoor dat het simulatievenster wordt afgesloten.
     */
	
    public void quit(){
    	System.exit(0);
    }

    /**
     * methode tick: deze maakt gebruik van methoden advanceTime, handleExit, updateViews en handleEntrance.
     * Heeft ook een optie om snelheid in te snellen met tickPause.
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
    	carsEntering(entranceAbboQueue);
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
        numberOfCars=getNumberOfCars(weekDayAbboArrivals, weekendAbboArrivals);
        addArrivingCars(numberOfCars, ABBO);
    }
	
    /**
     * methode carsEntering: houdt door methode carsInQueue bij hoeveel auto's er aankomen
     * en wijst deze toe aan lege plekken in de parkeergarage (hierbij wordt tegelijk de auto uit de queue verwijderd).
     * @param queue
     */

    private void carsEntering(CarQueue queue){
        int i=0;
    	while (queue.carsInQueue()>0 && 
    			simulatorView.getNumberOfOpenSpots()>0 && 
    			i<enterSpeed) {
            Car car = queue.removeCar();
            
            if(car.getColor() == Color.red){
            	Location vrijePlek = simulatorView.getFirstFreeLocation();
            	simulatorView.setCarAt(vrijePlek, car);
            	aantalAdHoc++;
            }
            
            if(car.getColor() == Color.blue){
            	Location vrijePlek = simulatorView.getFirstFreeLocation();
            	simulatorView.setCarAt(vrijePlek, car);
            	aantalPass++;
            }
            
            if(car.getColor() == Color.green){
            	Location vrijePlek = simulatorView.getFirstFreeLocation();
            	simulatorView.setCarAt(vrijePlek, car);
            	aantalAbbo++;
            }
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
            if(car.getColor() == Color.blue){
                opbrengst += 5;
            }
            if(car.getColor() == Color.red){
                opbrengst += 7;
            }
            if(car.getColor() == Color.green){
            }
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
            aantalCars--;
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
     * Methode addArrivingCars. Door middel van een switch wordt bepaald hoeveel van elk type auto er binnenkomen.
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
    	case ABBO:
            for (int i = 0; i < numberOfCars; i++) {
            	entranceAbboQueue.addCar(new AbboCar());
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
    }
    
    
}
