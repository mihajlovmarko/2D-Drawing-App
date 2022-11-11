package commands;

import java.awt.Color;

import shapes.Command;
import shapes.Line;
import shapes.Point;

public class CmdLineUpdate implements Command{
	private Line oldState;
	private Line newState;
	private Line originalState = new Line(new Point(), new Point());
	
	public CmdLineUpdate( Line oldState, Line newState) {
		this.oldState = oldState;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		originalState = oldState.clone();
//		originalState.getStartPoint().setX(oldState.getStartPoint().getX());
//		originalState.getStartPoint().setY(oldState.getStartPoint().getY());
//		originalState.getEndPoint().setX(oldState.getEndPoint().getX());
//		originalState.getEndPoint().setY(oldState.getEndPoint().getY());
//		originalState.setOutlineColor(oldState.getOutlineColor());
		oldState.getStartPoint().setX(newState.getStartPoint().getX());
		oldState.getStartPoint().setY(newState.getStartPoint().getY());
		oldState.getEndPoint().setX(newState.getEndPoint().getX());
		oldState.getEndPoint().setY(newState.getEndPoint().getY());
		if(newState.getOutlineColor() == Color.BLACK && originalState.getOutlineColor() != Color.BLACK) {
			oldState.setOutlineColor(originalState.getOutlineColor());
		} else {
			oldState.setOutlineColor(newState.getOutlineColor());
		}
//		AppModel.addShape(oldState);
	}

	@Override
	public void unexecute() {
		System.out.println(originalState.getStartPoint().getX());
		oldState.getStartPoint().setX(originalState.getStartPoint().getX());
		oldState.getStartPoint().setY(originalState.getStartPoint().getY());
		oldState.getEndPoint().setX(originalState.getEndPoint().getX());
		oldState.getEndPoint().setY(originalState.getEndPoint().getY());
		oldState.setOutlineColor(originalState.getOutlineColor());
	//	AppModel.removeShape(newState);
	}
	
	@Override
	public String toString() {
		return "Updated->" + oldState.toString() + "->" + newState.toString();
	}

}
