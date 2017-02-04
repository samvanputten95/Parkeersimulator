package model;
import java.awt.*;

import java.util.Random;

import view.*;
import controller.Controller;

import javax.swing.*;

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

  
    
    public Model() {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        entranceAbboQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        simulatorView = new MainWindow(3, 6, 30, this);     
    }
    
    public int getOpbrengst() {
        return opbrengst;
    }
    
    public int getCars(){
    	aantalCars = aantalAdHoc + aantalPass + aantalAbbo;
    	return aantalCars;
    }
    
    public int getAdHoc(){
    	return aantalAdHoc;
    }
    
    public int getPass(){
    	return aantalPass;
    }
    
    public int getAbbo(){
    	return aantalAbbo;
    }
    
    public int getMinuut(){
    	return minute;
    }
    
    public int getDag(){
    	return day;
    }
    
    public int getUur(){
    	return hour;
    }
    public void start(){
    	
    	run = true;
    	run(10000);
    }

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
    
    public void pause(){
    	run = false;
    }
    
    public void step(){
    	
    	for(int i = 0; i < 1; i++){
    		tick();
    	}
    }
    
    public void uur(){
    	
    	for(int i = 0; i < 60; i++){
    		tick();
    	}
    }
    
    public void quit(){
    	System.exit(0);
    }

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
    
    private void handleEntrance(){
    	carsArriving();
    	carsEntering(entranceAbboQueue);
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
    }
    
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    
    private void updateViews(){
    	simulatorView.tick();
        // Update the car park view.
        simulatorView.updateView();	
    }
    
    private void carsArriving(){
    	int numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);    	
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);
        numberOfCars=getNumberOfCars(weekDayAbboArrivals, weekendAbboArrivals);
        addArrivingCars(numberOfCars, ABBO);
    }

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
    
    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
            aantalCars--;
    	}	
    }
    
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
    
    private void carLeavesSpot(Car car){
    	simulatorView.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }
    
    
}
