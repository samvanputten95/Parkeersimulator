package controller;

import java.awt.event.ActionEvent;

import model.Model;

public class Controller extends AbstractController {
	
	
	public Controller(){
		
	}
	
	/**
	 * Constructor for the controller.
	 * @param model the model
	 */
	public Controller(Model model) {
		setModel(model);		
	}

	@Override
	
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getActionCommand().equals("start")){
			System.out.println("Start has been pressed");
		}
		if(e.getActionCommand().equals("pause")){
			
		}
		if(e.getActionCommand().equals("quit")){
			
		}
		if(e.getActionCommand().equals("step")){
			
		}
	}
	
}
