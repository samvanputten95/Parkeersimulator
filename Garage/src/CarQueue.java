/*
 * Author Par-Kings
 * Version 2017.1.25
 */
//package Parkeersimulator;
import java.util.LinkedList;
import java.util.Queue;

public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();
/* 
* @return queue.add(car)
*/
    public boolean addCar(Car car) {
        return queue.add(car);
    }
/* 
* @return queue.poll()
*/
    public Car removeCar() {
        return queue.poll();
    }
/* 
* @return queue.size()
*/
    public int carsInQueue(){
    	return queue.size();
    }
}
