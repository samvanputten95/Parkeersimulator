package model;

/*
 * Author Par-Kings
 * Version 2017.1.25
 */

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
* @return queue.add(car)
*/
    public Car removeCar() {
        return queue.poll();
    }
/* 
* @return queue.poll()
*/
    public int carsInQueue(){
    	return queue.size();
    }
/* 
* @return queue.size()
*/
}
