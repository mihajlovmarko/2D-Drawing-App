package delete;

import modelViewController.ApplicationModel;
import shapes.Circle;
import shapes.Command;

public class CmdCircleDelete implements Command{

	private ApplicationModel AppModel;
	private Circle circle;
	
	
	
	public CmdCircleDelete(ApplicationModel appModel, Circle circle) {
		AppModel = appModel;
		this.circle = circle;
	}

	@Override
	public void execute() {
		AppModel.removeShape(circle);
	}

	@Override
	public void unexecute() {
		AppModel.addShape(circle);
	}

}
