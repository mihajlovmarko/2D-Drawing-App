package commands;
//

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Point;

public class CmdPointAdd implements Command {
	private ApplicationModel AppModel;
	private Point point;
	public CmdPointAdd(ApplicationModel model, Point point) {
		this.AppModel = model;
		this.point = point;
	}
	@Override
	public void execute() {
		AppModel.addShape(point); 
	}

	@Override
	public void unexecute() {
		AppModel.removeShape(point);
	}
	
	@Override
	public String toString() {
		return "Added->" + point.toString();
	}
}
