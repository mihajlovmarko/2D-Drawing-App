package delete;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Shape;

public class CmdDeleteShape implements Command{
	
	private ApplicationModel appModel;
	private Shape shape;
	
	public CmdDeleteShape(ApplicationModel appModel, Shape shape) {
		this.appModel = appModel;
		this.shape = shape;
	}

	@Override
	public void execute() {
		appModel.removeShape(shape);
		appModel.getSelectedShapes().remove(shape);
	}

	@Override
	public void unexecute() {
		appModel.getShapes().add(shape);
	}

	@Override
	public String toString() {
		return "Deleted->" + shape.toString();
	}

}
