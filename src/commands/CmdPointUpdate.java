package commands;
//
import java.awt.Color;

import shapes.Command;
import shapes.Point;

public class CmdPointUpdate implements Command {
	private Point oldState;
	private Point newState;
	private Point originalState = new Point();
	
	public CmdPointUpdate( Point oldState, Point newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	public void execute() {
		originalState = oldState.clone();
		oldState.setX(newState.getX());
		oldState.setY(newState.getY());
		if(newState.getOutlineColor() == Color.BLACK && originalState.getOutlineColor() != Color.BLACK) {
			oldState.setOutlineColor(originalState.getOutlineColor());
		} else {
			oldState.setOutlineColor(newState.getOutlineColor());
		}
//		originalState.setX(oldState.getX());
//		originalState.setY(oldState.getY());
//		originalState.setOutlineColor(newState.getOutlineColor());
//		oldState.setX(newState.getX());
//		oldState.setY(newState.getY());
//		oldState.setOutlineColor(color);
////		AppModel.addShape(oldState);
	}
 
	@Override
	public void unexecute() {
		oldState.setX(originalState.getX());
		oldState.setY(originalState.getY());   	
		oldState.setOutlineColor(originalState.getOutlineColor());
	//	AppModel.removeShape(newState);
	}
	
	@Override
	public String toString() {
		return "Updated->"+oldState.toString() + "->" + newState.toString();
	}
	
}
