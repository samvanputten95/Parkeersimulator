package view;

import javax.swing.*;

import model.*;

/**
 * A displaypane is a JPanel and is used to register an observable. Various generalized 
 * functionality is aggregated in this class.
 * 
 * @author ronaldvandijk
 * @version 01-02-2017
 */
public abstract class AbstractView extends JPanel implements Observer {
	private static final long serialVersionUID = -2767764579227738552L;
	private static Observable observable; // in this application all Panes use the same model.

	/**
	 * Reference to the object this display pane is observing
	 * 
	 * @return returning an observable
	 */
	public static Observable getObservable() {
		return observable;
	}
	
	/**
	 * Defines the object this pane should observe. Note that this
	 * method only registers an observable object. 
	 * @param observable
	 */
	public static void setObservable(Observable observable) {
		AbstractView.observable = observable;
	}

	/**
	 * Constructor for the class
	 * @param observable register the object we would like to observe
	 */
	public AbstractView(Observable observable) {
		observe(observable);
	}
	
	/**
	 * This method does two things. Firstly it registers the observable object.
	 * In this application this is the model. It does this to be able to retrieve
	 * information from the observable when a change in state occurs. 
	 * Secondly it registers this observer to the observable. 
	 * 
	 * @param observable the observable
	 */
	public void observe(Observable observable){
		setObservable(observable);
		getObservable().registerObserver(this);
	}
	
	/**
	 * Returns the model
	 * @return the model
	 */
	public Model getModel() {
		return (Model) getObservable();
	}
	
	/**
	 * Update the view 
	 */
	public void updateView() {
		repaint();
	}
	
	/**
	 * update method that is called by an observable when there is a change
	 * in state of the observed object.
	 */
	public void update(){
		updateView();
	}
}
