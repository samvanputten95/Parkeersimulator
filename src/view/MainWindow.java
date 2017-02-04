package view;

import javax.swing.*;
import controller.*;
import model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Klasse MainWindow
 *  
 */

public class MainWindow implements ActionListener {
	//extends JFrame
	
	private CarParkView carParkView;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private JFrame mainwindow;
    private Car[][][] cars;
    private Model model;
    private ActionEvent event;

    private JPanel buttonbar;
    private JPanel buttonbar2;
    private JPanel infobar;
    private JPanel geldbar;

    private JLabel Cars;
    private JLabel AdHoc;
    private JLabel Pass;
    private JLabel Abbo;

    private JLabel Opbrengst;
    private JLabel Tijd;

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

        this.buttonbar2 = new JPanel();
        buttonbar2.setLayout(new GridLayout(1, 0));
        
        this.buttonbar = new JPanel();
        buttonbar.setLayout(new GridLayout(1, 0));
        
        this.infobar = new JPanel();
        infobar.setLayout(new GridLayout(1,0));
        
        this.geldbar = new JPanel();
        geldbar.setLayout(new GridLayout(1, 0));

        mainwindow.getContentPane().add(buttonbar2);
        mainwindow.getContentPane().add(buttonbar);
        mainwindow.getContentPane().add(infobar);
        mainwindow.getContentPane().add(geldbar);
        
        
        Container contentPane = mainwindow.getContentPane();
        
       
        JButton start = new JButton("Start");
        start.addActionListener(this);
        
        JButton step = new JButton("Minuut");
        step.addActionListener(this);
        
        JButton steps = new JButton("Uur");
        steps.addActionListener(this);
        
        JButton pause = new JButton("Pauze");
        pause.addActionListener(this);
        
        JButton quit = new JButton("Stop");
        quit.addActionListener(this);
        
        buttonbar2.add(start);
        
        buttonbar.add(step);
        buttonbar.add(steps);
        buttonbar.add(pause);
        buttonbar.add(quit);
        
        
        
        Cars = new JLabel("Totaalaantal Cars");
        Cars.setHorizontalAlignment(SwingConstants.CENTER);
        Cars.setText(String.valueOf(model.getCars()));
        
        AdHoc = new JLabel("aantal AdHocCars");
        AdHoc.setHorizontalAlignment(SwingConstants.CENTER);
        AdHoc.setText(String.valueOf(model.getAdHoc()));
        
        Pass = new JLabel("Aantal ParkingPassCars");
        Pass.setHorizontalAlignment(SwingConstants.CENTER);
        Pass.setText(String.valueOf(model.getPass()));
        
        Abbo = new JLabel("Aantal AbboCars");
        Abbo.setHorizontalAlignment(SwingConstants.CENTER);
        Abbo.setText(String.valueOf(model.getAbbo()));
        
        Opbrengst = new JLabel("Totaal Opbrengst");
        Opbrengst.setHorizontalAlignment(SwingConstants.CENTER);
        Opbrengst.setText(String.valueOf(model.getOpbrengst()));
        
        Tijd = new JLabel("Tijd");
        Tijd.setHorizontalAlignment(SwingConstants.CENTER);
        Tijd.setText(String.valueOf(model.getOpbrengst()));
        
        infobar.add(Cars);
        infobar.add(AdHoc);
        infobar.add(Pass);
        infobar.add(Abbo);
        
        geldbar.add(Opbrengst);
        geldbar.add(Tijd);
        
        JPanel flow = new JPanel(new GridLayout(0,1));       
        flow.add(buttonbar2);
        flow.add(buttonbar);
        flow.add(infobar);
        flow.add(geldbar);
        contentPane.add(carParkView, BorderLayout.NORTH);
        contentPane.add(flow, BorderLayout.SOUTH);
        
        mainwindow.pack();
        mainwindow.setVisible(true);

        updateView();
    }
 
   
    
    public void setActionEvent(ActionEvent e) {
        event = e;
    }
    
    public ActionEvent getActionEvent() {
        return event;
    }
    
    public void actionPerformed(ActionEvent e) {
        setActionEvent(e);
        
        Thread newThread = new Thread() {
            public void run() {
            	
                ActionEvent event = getActionEvent();                
                String command = event.getActionCommand();
                
                if (command == "Start") {
                	model.start();                    
                }
                
                if(command == "Pauze") {
                	model.pause();                    
                }
                
                if(command == "Minuut") {
                	
                	model.step();                    
                }
                
                if(command == "Uur") {
                	
                	model.uur();                    
                }
                                                   
                if (command == "Stop") {
                	model.quit();                                        
                }                
            }          
        };        
        newThread.start();    
    }
    
    public void updateView() {
        carParkView.updateView();
        
        Cars.setText("Totaalaantal Cars: " + String.valueOf(model.getCars()));
        AdHoc.setText("Aantal AdHocCars: " + String.valueOf(model.getAdHoc()));
        Pass.setText("Aantal ParkingPassCars: " + String.valueOf(model.getPass()));
        Abbo.setText("Aantal AbboCars: " + String.valueOf(model.getAbbo()));
        Opbrengst.setText("Totaal Opbrengst: € " + String.valueOf(model.getOpbrengst())+ ",-");
        Tijd.setText("Aantal Minuten: " + String.valueOf(model.getTime()));
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
