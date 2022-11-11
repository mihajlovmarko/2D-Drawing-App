package delete;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Rectangle;

public class CmdRectangleDelete implements Command{
	private ApplicationModel AppModel;
	private Rectangle rectangle;
	
	public CmdRectangleDelete(ApplicationModel appModel, Rectangle rectangle) {
		AppModel = appModel;
		this.rectangle = rectangle;
	}

	@Override
	public void execute() {
		AppModel.removeShape(rectangle);
	}

	@Override
	public void unexecute() {
		AppModel.addShape(rectangle);
	}

}
