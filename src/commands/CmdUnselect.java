package commands;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Shape;

public class CmdUnselect implements Command{
	
	private ApplicationModel AppModel;
	private Shape selectedShape;
	private int sender;
	
	public CmdUnselect(ApplicationModel appModel, Shape selectedShape, int sender) {
		this.AppModel = appModel;
		this.selectedShape = selectedShape;
		this.sender = sender;
	}

	@Override
	public void execute() {
		AppModel.getShapes().get(AppModel.getShapes().indexOf(selectedShape)).setSelected(false);
//		AppModel.getSelectedShapes().remove(selectedShape);
	}

	@Override
	public void unexecute() {
		AppModel.getShapes().get(AppModel.getShapes().indexOf(selectedShape)).setSelected(true);
		AppModel.getSelectedShapes().add(selectedShape);
	}
	
	@Override
	public String toString() {
		return "Unselected->" + selectedShape.toString();
	}
	
	

}
