package view;

import javax.swing.*;
import controller.*;
import model.Car;
import model.Location;
import model.Model;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Klasse MainWindow
 *  
 */

public class MainWindow {
	//extends JFrame
	
	private CarParkView carParkView;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private JFrame mainwindow; 
    private JPanel toolbar;
    private Car[][][] cars;
    private Model model;
    

    public MainWindow(int numberOfFloors, int numberOfRows, int numberOfPlaces, Model model) {
    	mainwindow=new JFrame("GarageSimulator");
    	
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        this.numberOfOpenSpots =numberOfFloors*numberOfRows*numberOfPlaces;
        
        this.model = model;
        Controller controller = new Controller(model);
        
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        
        carParkView = new CarParkView(this);

        
        this.toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(1, 0));
		mainwindow.getContentPane().add(toolbar);
        
        
        
        Container contentPane = mainwindow.getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
       
        
        
       
        
        JButton start = new JButton("start");
        JButton step = new JButton("step");
        JButton pause = new JButton("pause");
        JButton quit = new JButton("quit");
        
        start.addActionListener(controller);
        toolbar.add(start);
        toolbar.add(step);
        toolbar.add(pause);
        toolbar.add(quit);
        
        //JPanel flow = new JPanel();       
        //flow.add(toolbar);
        contentPane.add(toolbar, BorderLayout.SOUTH);
        
        mainwindow.pack();
        mainwindow.setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
    }
    
	public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getNumberOfOpenSpots(){
    	return numberOfOpenSpots;
    }
    
    public Car getCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        return cars[location.getFloor()][location.getRow()][location.getPlace()];
    }

    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (oldCar == null) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            numberOfOpenSpots--;
            return true;
        }
        return false;
    }

    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    public Location getFirstFreeLocation() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public Car getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    public void tick() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }
    
    
}
