package commands;


import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Shape;

public class CmdSelect implements Command{
	private ApplicationModel AppModel;
	private Shape selectedShape;
	public CmdSelect(ApplicationModel appModel, Shape shape) {
		this.AppModel = appModel;
		this.selectedShape = shape;
	}

	@Override 
	public void execute() {
		selectedShape.setSelected(true);
		AppModel.addSelectedShape(selectedShape);
	}

	@Override 
	public void unexecute() { 
		selectedShape.setSelected(false);
		AppModel.getSelectedShapes().remove(selectedShape);
	}
	
	@Override
	public String toString() {
		return "Selected->" + selectedShape.toString();
	}

}
