package commands;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Shape;

public class CmdBringToBack implements Command{

	private ApplicationModel AppModel;
	private int index;
	private Shape shape;
	
	
	
	public CmdBringToBack(ApplicationModel appModel, int index, Shape shape) {
		AppModel = appModel;
		this.index = index;
		this.shape = shape;
	}

	@Override
	public void execute() {
		if(index != 0) {
			AppModel.getShapes().remove(shape);
			AppModel.getShapes().add(0, shape);
		}
	}

	@Override
	public void unexecute() {
		AppModel.getShapes().remove(shape);
		AppModel.getShapes().add(index, shape);
		
	}
	
	@Override
	public String toString() {
		return "Brought to back->" + shape.toString();
	}


}
