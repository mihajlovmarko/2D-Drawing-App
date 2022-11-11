package commands;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Shape;

public class CmdBringToFront implements Command{
	
	private ApplicationModel AppModel;
	private Shape shape;
	private int indexBefore;
	
	
	public CmdBringToFront(ApplicationModel appModel, Shape shape) {
		AppModel = appModel;
		this.shape = shape;
	}

	@Override
	public void execute() {
		indexBefore=AppModel.getIndexOfShape(shape);
		AppModel.getShapes().remove(shape);
		AppModel.getShapes().add(shape);
//		if(index < AppModel.getShapes().size()) {
//			AppModel.getShapes().remove(shape);
//			AppModel.getShapes().add(shape);
//		}
//		this.indexBefore = index;
	}

	@Override
	public void unexecute() {
		AppModel.getShapes().remove(shape);
		AppModel.getShapes().add(indexBefore, shape);
	}

	@Override
	public String toString() {
		return "Brought to front->" + shape.toString();
	}

}
